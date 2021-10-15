package com.troia.libraryproject.repository;

import com.troia.libraryproject.model.User.User;

import javax.transaction.Transactional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, UUID> {
    boolean existsByUsername(String username);

    User findByUsername(String username);

    @Transactional
    void deleteByUsername(String username);
}
