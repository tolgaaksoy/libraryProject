package com.troia.libraryproject.repository.user;

import com.troia.libraryproject.model.user.User;
import com.troia.libraryproject.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByUsername(String username);

    Optional<User> findByUsernameOrEmailOrPhone(String username, String email, String phone);

    @Transactional
    void deleteByUsername(String username);
}
