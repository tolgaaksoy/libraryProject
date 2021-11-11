package com.troia.libraryproject.controller;

import com.troia.libraryproject.dto.request.user.*;
import com.troia.libraryproject.response.APIResponse;
import com.troia.libraryproject.service.user.RefreshTokenService;
import com.troia.libraryproject.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@RequestBody SingUpRequestDto singupRequestDto) {
        return userService.register(singupRequestDto);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<APIResponse> refreshToken(@RequestBody TokenRefreshRequestDto request) {
        return refreshTokenService.refreshToken(request);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody LoginRequestDto loginDto) {
        return userService.login(loginDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<APIResponse> logoutUser(@RequestBody LogOutRequestDto logOutRequest) {
        return refreshTokenService.logoutUser(logOutRequest);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<APIResponse> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(changePasswordDto);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<APIResponse> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return userService.forgotPassword(forgotPasswordDto);
    }

    @PostMapping("/forgotPassword/validatePasswordResetToken")
    public ResponseEntity<APIResponse> validatePasswordResetToken(@RequestParam("token") String token) {
        return userService.validatePasswordResetToken(token);
    }

    @PostMapping("/forgotPassword/changePassword")
    public ResponseEntity<APIResponse> forgotPasswordChangePassword(@RequestBody ForgotPasswordChangePasswordDto changePasswordDto) {
        return userService.forgotPasswordChangePassword(changePasswordDto);
    }
}