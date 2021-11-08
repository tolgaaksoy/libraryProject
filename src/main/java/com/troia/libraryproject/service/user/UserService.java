package com.troia.libraryproject.service.user;

import com.troia.libraryproject.dto.request.user.LoginRequestDto;
import com.troia.libraryproject.dto.response.user.LoginResponseDto;
import com.troia.libraryproject.dto.request.user.SingUpRequestDto;
import com.troia.libraryproject.exception.CustomException;
import com.troia.libraryproject.mapper.UserMapper;
import com.troia.libraryproject.model.user.ERole;
import com.troia.libraryproject.model.user.Role;
import com.troia.libraryproject.model.user.User;
import com.troia.libraryproject.repository.user.RoleRepository;
import com.troia.libraryproject.repository.user.UserRepository;
import com.troia.libraryproject.response.APIResponse;
import com.troia.libraryproject.security.JwtUtils;
import com.troia.libraryproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public ResponseEntity<APIResponse> login(LoginRequestDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(userDetails);
            List<String> roles = userDetails
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            Optional<User> optionalUser = userRepository.findById(userDetails.getId());
            if (optionalUser.isEmpty()) {
                return new ResponseEntity<>(APIResponse.builder()
                        .message("User not found!")
                        .status(404)
                        .build(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(APIResponse.builder()
                    .data(LoginResponseDto.builder()
                            .token(jwt)
                            .refreshToken(refreshTokenService.createRefreshToken(optionalUser.get()).getToken())
                            .roles(roles)
                            .id(userDetails.getId())
                            .username(userDetails.getUsername())
                            .email(userDetails.getUsername())
                            .build())
                    .message("Success")
                    .status(200)
                    .build(), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(APIResponse.builder()
                    .message("Invalid username or password. Please try again.")
                    .status(401)
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<APIResponse> register(SingUpRequestDto singupRequestDto) {
        if (userRepository.existsByUsername(singupRequestDto.getUsername())) {
            return new ResponseEntity<>(APIResponse.builder()
                    .message("Error: Username is already taken!")
                    .status(409)
                    .build(), HttpStatus.CONFLICT);
        }
        if (userRepository.existsByEmail(singupRequestDto.getEmail())) {
            return new ResponseEntity<>(APIResponse.builder()
                    .message("Error: Email is already in use!")
                    .status(409)
                    .build(), HttpStatus.CONFLICT);
        }
        //NEW METHOD
        if (singupRequestDto.getRoles() == null || singupRequestDto.getRoles().isEmpty()) {
            Role userRole = new Role(ERole.ROLE_MEMBER);
            roleRepository.save(userRole);
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            singupRequestDto.setRoles(roles);
        } else {
            Set<Role> roles = new HashSet<>();
            for (Role role : singupRequestDto.getRoles()) {
                Optional<Role> optionalRole = roleRepository.findByRole(role.getRole());
                if (optionalRole.isPresent()) {
                    roles.add(optionalRole.get());
                } else {
                    log.error(role.getRole() + " not found!!!");
                }
            }
            singupRequestDto.setRoles(roles);
        }
        // Create new user's account
        User user = userMapper.singUpRequestDtoToUser(singupRequestDto);
        userRepository.save(user);

        return new ResponseEntity<>(APIResponse.builder()
                .data(userMapper.userToSingUpResponseDto(user))
                .message("Success")
                .status(200)
                .build(), HttpStatus.OK);
    }

    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }

    public User search(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return user;
    }

}