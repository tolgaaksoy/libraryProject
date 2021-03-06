package com.troia.libraryproject.service;

import com.troia.libraryproject.model.Book;
import com.troia.libraryproject.response.APIResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BookService {
    ResponseEntity<APIResponse> getAllBooks(Pageable pageable);
    ResponseEntity<APIResponse> findBookById(String id);
    ResponseEntity<APIResponse> createBook(Book book);
    ResponseEntity<APIResponse> updateBook(Book book);
    ResponseEntity<APIResponse> deleteBook(String id);
}
