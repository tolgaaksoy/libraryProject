package com.troia.libraryproject.service;

import com.troia.libraryproject.model.Book;
import com.troia.libraryproject.repository.BookRepository;
import com.troia.libraryproject.response.APIResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImpl extends BaseService implements BookService {

    private final BookRepository bookRepo;

    public BookServiceImpl(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public ResponseEntity<APIResponse> getAllBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepo.findAll(pageable);
        return new ResponseEntity<>(APIResponse.builder()
                .status(200)
                .message("Success")
                .data(bookPage)
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> findBookById(String id) {
        Optional<Book> optionalBook = bookRepo.findById(UUID.fromString(id));
        if (optionalBook.isEmpty()) {
            APIResponse res = APIResponse.builder()
                    .message("Book not found.")
                    .status(404)
                    .build();
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(APIResponse.builder()
                .data(optionalBook.get())
                .message("Success")
                .status(200)
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> createBook(Book book) {
        Book createdBook = bookRepo.save(book);
        return new ResponseEntity<>(APIResponse.builder()
                .data(createdBook)
                .message("Success")
                .status(200)
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> updateBook(Book book) {
        Optional<Book> optionalBook = bookRepo.findById(book.getId());
        if (optionalBook.isEmpty()) {
            return new ResponseEntity<>(APIResponse.builder()
                    .message("Book not found.")
                    .status(404)
                    .build(), HttpStatus.NOT_FOUND);
        }
        copyNonNullProperties(book, optionalBook.get());
        Book savedBook = bookRepo.save(optionalBook.get());
        return new ResponseEntity<>(APIResponse.builder()
                .data(savedBook)
                .message("Success.")
                .status(200)
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> deleteBook(String id) {
        Optional<Book> optionalBook = bookRepo.findById(UUID.fromString(id));
        if (optionalBook.isEmpty()) {
            return new ResponseEntity<>(APIResponse.builder()
                    .message("Book not found.")
                    .status(404)
                    .build(), HttpStatus.NOT_FOUND);
        }
        bookRepo.deleteById(UUID.fromString(id));
        return new ResponseEntity<>(APIResponse.builder()
                .message("Success")
                .status(200)
                .build(), HttpStatus.OK);
    }
}