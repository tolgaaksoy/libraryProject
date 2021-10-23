package com.troia.libraryproject.repository;

import com.troia.libraryproject.model.Library;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LibraryRepository extends BaseRepository<Library, UUID> {
}
