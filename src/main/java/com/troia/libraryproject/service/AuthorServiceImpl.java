package com.troia.libraryproject.service;

import com.troia.libraryproject.model.Author;
import com.troia.libraryproject.model.Book;
import com.troia.libraryproject.repository.AuthorRepository;
import com.troia.libraryproject.response.APIResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthorServiceImpl extends BaseService implements AuthorService {

    private final AuthorRepository authorRepo;

    @Override
    public ResponseEntity<APIResponse> getAllAuthors() {
        List<Author> authorList = authorRepo.findAll();
        if (authorList.isEmpty()) {
            APIResponse res = APIResponse.builder()
                    .message("Yazar bulunamadı.")
                    .status(404)
                    .build();
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        APIResponse res = APIResponse.builder()
                .data(authorList)
                .status(200)
                .message("Yazar bulundu.")
                .build();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> findAuthorById(String id) {
        Optional<Author> optionalAuthor = authorRepo.findById(UUID.fromString(id));
        if (optionalAuthor.isPresent()) {
            return new ResponseEntity<>(APIResponse.builder()
                    .data(optionalAuthor.get())
                    .message("Yazar bulundu.")
                    .status(200)
                    .build(), HttpStatus.OK);
        }
        APIResponse res = APIResponse.builder()
                .message("Yazar bulunamadı.")
                .status(404)
                .build();
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<APIResponse> createAuthor(Author author) {
        Author createdAuthor = authorRepo.save(author);
        return new ResponseEntity<>(APIResponse.builder()
                .data(createdAuthor)
                .message("Yazar kaydedildi.")
                .status(200)
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> updateAuthor(Author author) {
        Optional<Author> optionalAuthor = authorRepo.findById(author.getId());
        if (optionalAuthor.isPresent()) {
            copyNonNullProperties(author, optionalAuthor.get());
            Author savedAuthor = authorRepo.save(optionalAuthor.get());
            return new ResponseEntity<>(APIResponse.builder()
                    .data(savedAuthor)
                    .message("Yazar güncellendi.")
                    .status(200)
                    .build(), HttpStatus.OK);
        }
        APIResponse res = APIResponse.builder()
                .message("Yazar bulunamadı.")
                .status(404)
                .build();

        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<APIResponse> deleteAuthor(String id) {
        Optional<Author> optionalAuthor = authorRepo.findById(UUID.fromString(id));
        if (optionalAuthor.isPresent()) {
            authorRepo.deleteById(UUID.fromString(id));
            return new ResponseEntity<>(APIResponse.builder()
                    .message("Yazar başarıyla silindi...")
                    .status(200)
                    .build(), HttpStatus.OK);
        }
        APIResponse res = APIResponse.builder()
                .message("Yazar bulunamadı.")
                .status(404)
                .build();
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

}
