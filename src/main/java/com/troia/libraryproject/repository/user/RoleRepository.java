package com.troia.libraryproject.repository.user;

import com.troia.libraryproject.model.user.ERole;
import com.troia.libraryproject.model.user.Role;
import com.troia.libraryproject.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends BaseRepository<Role, UUID> {
    Optional<Role> findByRole(ERole role);

    boolean existsByRole(ERole role);
}
