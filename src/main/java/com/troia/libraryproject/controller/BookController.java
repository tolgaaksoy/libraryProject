package com.troia.libraryproject.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.troia.libraryproject.model.Book;
import com.troia.libraryproject.response.APIResponse;
import com.troia.libraryproject.service.BookServiceImpl;
import com.troia.libraryproject.view.Views;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @JsonView(Views.BookListing.class)
    public ResponseEntity<APIResponse> getALlBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    @GetMapping("/{id}")
    @JsonView(Views.BookListing.class)
    public ResponseEntity<APIResponse> findBookById(@PathVariable String id) {
        return bookService.findBookById(id);
    }

    @PostMapping
    public ResponseEntity<APIResponse> createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @PutMapping
    public ResponseEntity<APIResponse> updateBook(@RequestBody Book book) {
        return bookService.updateBook(book);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<APIResponse> deleteBook(@PathVariable String id) {
        return bookService.deleteBook(id);
    }
}