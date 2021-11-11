package com.troia.libraryproject.service.user;

import com.troia.libraryproject.dto.request.user.*;
import com.troia.libraryproject.dto.response.user.LoginResponseDto;
import com.troia.libraryproject.mapper.UserMapper;
import com.troia.libraryproject.model.user.ERole;
import com.troia.libraryproject.model.user.PasswordResetToken;
import com.troia.libraryproject.model.user.Role;
import com.troia.libraryproject.model.user.User;
import com.troia.libraryproject.repository.user.PasswordTokenRepository;
import com.troia.libraryproject.repository.user.RoleRepository;
import com.troia.libraryproject.repository.user.UserRepository;
import com.troia.libraryproject.response.APIResponse;
import com.troia.libraryproject.security.JwtUtils;
import com.troia.libraryproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${security.resetToken.expirationMs}")
    private int resetTokenExpirationMs;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordTokenRepository passwordTokenRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

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

    public ResponseEntity<APIResponse> changePassword(ChangePasswordDto changePasswordDto) {
        Optional<User> optionalUser = userRepository.findById(changePasswordDto.getUserId());
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(APIResponse.builder()
                    .status(404)
                    .message("User not found...")
                    .build(), HttpStatus.NOT_FOUND);
        }
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getNewPasswordVerification())) {
            return new ResponseEntity<>(APIResponse.builder()
                    .status(403)
                    .message("The passwords entered do not match.")
                    .build(), HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), optionalUser.get().getPassword())) {
            return new ResponseEntity<>(APIResponse.builder()
                    .status(403)
                    .message("The entered password does not match the old password.")
                    .build(), HttpStatus.FORBIDDEN);
        }
        optionalUser.get().setPassword(changePasswordDto.getNewPassword());
        refreshTokenService.logoutUser(new LogOutRequestDto(optionalUser.get().getId()));
        userRepository.save(optionalUser.get());
        return new ResponseEntity<>(APIResponse.builder()
                .status(200)
                .message("Success")
                .build(), HttpStatus.OK);
    }

    public ResponseEntity<APIResponse> forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        Optional<User> optionalUser = userRepository
                .findByUsernameOrEmailOrPhone(forgotPasswordDto.getUsername(),
                        forgotPasswordDto.getEmail(),
                        forgotPasswordDto.getPhone());
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(APIResponse.builder()
                    .status(404)
                    .message("User not found...")
                    .build(), HttpStatus.NOT_FOUND);
        }
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(optionalUser.get(), token);
        //TODO: send to token with mail or sms
        System.out.println(token);
        return new ResponseEntity<>(APIResponse.builder()
                .status(200)
                .message("Success")
                .build(), HttpStatus.OK);
    }

    public ResponseEntity<APIResponse> forgotPasswordChangePassword(ForgotPasswordChangePasswordDto changePasswordDto) {
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getNewPasswordVerification())) {
            return new ResponseEntity<>(APIResponse.builder()
                    .status(403)
                    .message("The passwords entered do not match.")
                    .build(), HttpStatus.FORBIDDEN);
        }
        //TODO: Find better solution
        if (!findAndValidatePasswordResetToken(changePasswordDto.getToken()).equalsIgnoreCase("validate")) {
            return validatePasswordResetToken(changePasswordDto.getToken());
        }
        Optional<PasswordResetToken> optionalPasswordResetToken = passwordTokenRepo.findByToken(changePasswordDto.getToken());
        User user = optionalPasswordResetToken.get().getUser();
        user.setPassword(changePasswordDto.getNewPassword());
        userRepository.save(user);
        refreshTokenService.logoutUser(new LogOutRequestDto(user.getId()));
        return new ResponseEntity<>(APIResponse.builder()
                .status(200)
                .message("Success")
                .build(), HttpStatus.OK);
    }

    public ResponseEntity<APIResponse> validatePasswordResetToken(String token) {
        String validateToken = findAndValidatePasswordResetToken(token);
        if (validateToken.equalsIgnoreCase("notFound")) {
            log.info("Token not found... Token: {}", token);
            return new ResponseEntity<>(APIResponse.builder()
                    .status(404)
                    .message("Token not found...")
                    .build(), HttpStatus.NOT_FOUND);
        }
        if (validateToken.equalsIgnoreCase("expired")) {
            log.info("The token has expired... Token: {}", token);
            return new ResponseEntity<>(APIResponse.builder()
                    .status(403)
                    .message("The token has expired...")
                    .build(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(APIResponse.builder()
                .status(400)
                .message("The token is validate...")
                .build(), HttpStatus.OK);
    }

    private void createPasswordResetTokenForUser(User user, String token) {
        Instant expiredDate = Instant.now().plusMillis(resetTokenExpirationMs);
        PasswordResetToken myToken = new PasswordResetToken(token, user, expiredDate);
        passwordTokenRepo.save(myToken);
    }

    private String findAndValidatePasswordResetToken(String token) {
        Optional<PasswordResetToken> optionalToken = passwordTokenRepo.findByToken(token);
        if (optionalToken.isEmpty()) {
            log.info("Token not found... Token: {}", token);
            return "notFound";
        }
        if (optionalToken.get().getExpiryDate().isBefore(Instant.now())) {
            log.info("The token has expired... Token: {}", token);
            return "expired";
        }
        return "validate";
    }


//    public void delete(String username) {
//        userRepository.deleteByUsername(username);
//    }
//
//    public User search(String username) {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
//        }
//        return user;
//    }
}