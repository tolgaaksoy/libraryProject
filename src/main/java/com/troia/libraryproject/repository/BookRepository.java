package com.troia.libraryproject.repository;

import com.troia.libraryproject.model.Book;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends BaseRepository<Book, UUID> {
}