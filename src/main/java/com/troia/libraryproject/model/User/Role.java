package com.troia.libraryproject.model.User;

import com.troia.libraryproject.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7759103393086576483L;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private ERole role;
}
