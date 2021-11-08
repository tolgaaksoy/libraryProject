package com.troia.libraryproject.service.user;

import com.troia.libraryproject.dto.request.user.LogOutRequestDto;
import com.troia.libraryproject.dto.request.user.TokenRefreshRequestDto;
import com.troia.libraryproject.dto.response.user.TokenRefreshResponseDto;
import com.troia.libraryproject.model.user.RefreshToken;
import com.troia.libraryproject.model.user.User;
import com.troia.libraryproject.repository.user.RefreshTokenRepository;
import com.troia.libraryproject.repository.user.UserRepository;
import com.troia.libraryproject.response.APIResponse;
import com.troia.libraryproject.security.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${security.jwt.token.refreshExpirationMs}")
    private int refreshTokenDurationMs;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<APIResponse> refreshToken(TokenRefreshRequestDto request) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(request.getRefreshToken());
        if (optionalRefreshToken.isPresent()) {
            RefreshToken refreshToken = optionalRefreshToken.get();
            if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
                refreshTokenRepository.delete(refreshToken);
                return new ResponseEntity<>(APIResponse.builder()
                        .message("Refresh token was expired. Please make a new SignIn request")
                        .data(refreshToken.getToken())
                        .status(401)
                        .build(), HttpStatus.UNAUTHORIZED);
            }
            User user = refreshToken.getUser();
            String token = jwtUtils.generateTokenFromUsername(user.getUsername());
            return new ResponseEntity<>(APIResponse.builder()
                    .message("Success")
                    .data(TokenRefreshResponseDto.builder()
                            .accessToken(token)
                            .refreshToken(request.getRefreshToken())
                            .build())
                    .status(200)
                    .build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(APIResponse.builder()
                .message("Refresh token is not in database!")
                .data(request.getRefreshToken())
                .status(404)
                .build(), HttpStatus.NOT_FOUND);
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .token(UUID.randomUUID().toString())
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public ResponseEntity<APIResponse> logoutUser(LogOutRequestDto logOutRequestDto) {
        Optional<User> optionalUser = userRepository.findById(logOutRequestDto.getUserId());
        if (optionalUser.isPresent()) {
            refreshTokenRepository.deleteByUser(optionalUser.get());
            return new ResponseEntity<>(APIResponse.builder()
                    .message("Log out successful!")
                    .status(200)
                    .build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(APIResponse.builder()
                .status(404)
                .message("User not found!")
                .build(), HttpStatus.NOT_FOUND);
    }
}