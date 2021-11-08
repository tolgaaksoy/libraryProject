package com.troia.libraryproject.controller;

import com.troia.libraryproject.dto.request.user.LogOutRequestDto;
import com.troia.libraryproject.dto.request.user.LoginRequestDto;
import com.troia.libraryproject.dto.request.user.SingUpRequestDto;
import com.troia.libraryproject.dto.request.user.TokenRefreshRequestDto;
import com.troia.libraryproject.response.APIResponse;
import com.troia.libraryproject.service.user.RefreshTokenService;
import com.troia.libraryproject.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody LoginRequestDto loginDto) {
        return userService.login(loginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@RequestBody SingUpRequestDto singupRequestDto) {
        return userService.register(singupRequestDto);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<APIResponse> refreshToken(@RequestBody TokenRefreshRequestDto request) {
        return refreshTokenService.refreshToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<APIResponse> logoutUser(@RequestBody LogOutRequestDto logOutRequest) {
        return refreshTokenService.logoutUser(logOutRequest);
    }

}