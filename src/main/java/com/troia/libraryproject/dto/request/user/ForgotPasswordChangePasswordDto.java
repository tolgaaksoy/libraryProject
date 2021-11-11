package com.troia.libraryproject.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordChangePasswordDto {
    private String token;
    private String newPassword;
    private String newPasswordVerification;
}
