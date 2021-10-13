package com.troia.libraryproject.repository;

import com.troia.libraryproject.model.Author;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorRepository extends BaseRepository<Author, UUID> {
}