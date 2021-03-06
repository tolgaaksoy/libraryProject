package com.troia.libraryproject.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.troia.libraryproject.model.Author;
import com.troia.libraryproject.response.APIResponse;
import com.troia.libraryproject.service.AuthorServiceImpl;
import com.troia.libraryproject.view.Views;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/author")
public class AuthorController {

    private final AuthorServiceImpl authorService;

    @GetMapping
    @JsonView(Views.AuthorListing.class)
    public ResponseEntity<APIResponse> getALlAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    @JsonView(Views.AuthorListing.class)
    public ResponseEntity<APIResponse> findAuthorById(@PathVariable String id) {
        return authorService.findAuthorById(id);
    }

    @PostMapping
    public ResponseEntity<APIResponse> createAuthor(@RequestBody Author author) {
        return authorService.createAuthor(author);
    }

    @PutMapping
    public ResponseEntity<APIResponse> updateAuthor(@RequestBody Author author) {
        return authorService.updateAuthor(author);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<APIResponse> deleteAuthor(@PathVariable String id) {
        return authorService.deleteAuthor(id);
    }
}
