package com.troia.libraryproject.service;

import com.troia.libraryproject.model.Book;
import com.troia.libraryproject.repository.BookRepository;
import com.troia.libraryproject.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepo;

    public BookServiceImpl(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public ResponseEntity<APIResponse> getAllBooks() {
        List<Book> bookList = bookRepo.findAll();
        if (bookList.isEmpty()) {
            APIResponse res = APIResponse.builder()
                    .message("Kitaplar bulunamadı.")
                    .status(404)
                    .build();
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        APIResponse res = APIResponse.builder()
                .data(bookList)
                .status(200)
                .message("Kitaplar bulundu.")
                .build();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> findBookById(String id) {
        Optional<Book> optionalBook = bookRepo.findById(UUID.fromString(id));
        if (optionalBook.isPresent()) {
            return new ResponseEntity<>(APIResponse.builder()
                    .data(optionalBook.get())
                    .message("Kitap bulundu")
                    .status(200)
                    .build(), HttpStatus.OK);
        }
        APIResponse res = APIResponse.builder()
                .message("Kitap bulunamadı.")
                .status(404)
                .build();
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<APIResponse> createBook(Book book) {
        Book createdBook = bookRepo.save(book);
        return new ResponseEntity<>(APIResponse.builder()
                .data(createdBook)
                .message("Kitap kaydedildi")
                .status(200)
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> updateBook(Book book) {
        return null;
    }

    @Override
    public ResponseEntity<APIResponse> deleteBook(String id) {
        return null;
    }
}