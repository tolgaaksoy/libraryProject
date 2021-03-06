package com.troia.libraryproject.dto.request.user;

import com.troia.libraryproject.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingUpRequestDto {
    private String username;
    private String password;

    private String name;
    private String surname;

    private String email;
    private String phone;

    private Set<Role> roles;

}
