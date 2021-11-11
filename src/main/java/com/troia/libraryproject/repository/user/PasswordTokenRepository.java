package com.troia.libraryproject.repository.user;

import com.troia.libraryproject.model.user.PasswordResetToken;
import com.troia.libraryproject.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordTokenRepository extends BaseRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);
}
