package com.troia.libraryproject.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
    private UUID userId;
    private String currentPassword;
    private String newPassword;
    private String newPasswordVerification;
}
