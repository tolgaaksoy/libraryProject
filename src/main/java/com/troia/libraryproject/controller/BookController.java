package com.troia.libraryproject.controller;

import com.troia.libraryproject.model.Book;
import com.troia.libraryproject.response.APIResponse;
import com.troia.libraryproject.service.BookServiceImpl;
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
    public ResponseEntity<APIResponse> getALlBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/api/book/{id}")
    public ResponseEntity<APIResponse> findBookById(@PathVariable String id) {
        return bookService.findBookById(id);
    }

    @PostMapping
    public ResponseEntity<APIResponse> createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

}