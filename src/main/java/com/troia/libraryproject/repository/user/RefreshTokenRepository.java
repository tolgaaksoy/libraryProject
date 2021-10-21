package com.troia.libraryproject.repository.user;

import com.troia.libraryproject.model.User.RefreshToken;
import com.troia.libraryproject.model.User.User;
import com.troia.libraryproject.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    void deleteByUser(User user);
}
