package com.troia.libraryproject.model.User;

import com.troia.libraryproject.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -996900163196618397L;

    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roleList;

    private String name;
    private String surname;

    private String email;
    private String phone;

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
