package com.troia.libraryproject.service;

import com.troia.libraryproject.model.Author;
import com.troia.libraryproject.response.APIResponse;
import org.springframework.http.ResponseEntity;

public interface AuthorService {
    ResponseEntity<APIResponse> getAllAuthors();
    ResponseEntity<APIResponse> findAuthorById(String id);
    ResponseEntity<APIResponse> createAuthor(Author author);
    ResponseEntity<APIResponse> updateAuthor(Author author);
    ResponseEntity<APIResponse> deleteAuthor(String id);
}
