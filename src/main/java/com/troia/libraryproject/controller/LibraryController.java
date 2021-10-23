package com.troia.libraryproject.controller;

import com.troia.libraryproject.model.Library;
import com.troia.libraryproject.response.APIResponse;
import com.troia.libraryproject.service.LibraryServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private final LibraryServiceImpl libraryService;

    public LibraryController(LibraryServiceImpl libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getLibrary(@PathVariable String id) {
        return libraryService.getLibrary(id);
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllLibrary(Pageable pageable) {
        return libraryService.getAllLibrary(pageable);
    }

    @PostMapping
    public ResponseEntity<APIResponse> createLibrary(@RequestBody Library library) {
        return libraryService.createLibrary(library);
    }

    @PutMapping
    public ResponseEntity<APIResponse> updateLibrary(@RequestBody Library library) {
        return libraryService.updateLibrary(library);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteLibrary(@PathVariable String id) {
        return libraryService.deleteLibrary(id);
    }
}
