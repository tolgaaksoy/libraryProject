package com.troia.libraryproject.service;

import com.troia.libraryproject.model.Library;
import com.troia.libraryproject.repository.LibraryRepository;
import com.troia.libraryproject.response.APIResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LibraryServiceImpl extends BaseService implements LibraryService {

    private final LibraryRepository libraryRepository;

    public LibraryServiceImpl(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    @Override
    public ResponseEntity<APIResponse> getLibrary(String id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(UUID.fromString(id));
        if (optionalLibrary.isEmpty()) {
            return new ResponseEntity<>(APIResponse.builder()
                    .status(404)
                    .message("Library Not Found.")
                    .build(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(APIResponse.builder()
                .status(200)
                .message("Success")
                .data(optionalLibrary.get())
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> getAllLibrary(Pageable pageable) {
        Page<Library> libraryPage = libraryRepository.findAll(pageable);
        return new ResponseEntity<>(APIResponse.builder()
                .status(200)
                .message("Success")
                .data(libraryPage)
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> createLibrary(Library library) {
        Library savedLibrary = libraryRepository.save(library);
        return new ResponseEntity<>(APIResponse.builder()
                .status(200)
                .message("Success")
                .data(savedLibrary)
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> updateLibrary(Library library) {
        Optional<Library> optionalLibrary = libraryRepository.findById(library.getId());
        if (optionalLibrary.isEmpty()) {
            return new ResponseEntity<>(APIResponse.builder()
                    .status(404)
                    .message("Library not found.")
                    .build(), HttpStatus.NOT_FOUND);
        }
        copyNonNullProperties(library, optionalLibrary.get());
        Library updatedLibrary = libraryRepository.save(optionalLibrary.get());
        return new ResponseEntity<>(APIResponse.builder()
                .status(200)
                .message("Success")
                .data(updatedLibrary)
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<APIResponse> deleteLibrary(String id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(UUID.fromString(id));
        if (optionalLibrary.isEmpty()) {
            return new ResponseEntity<>(APIResponse.builder()
                    .status(404)
                    .message("Library not found.")
                    .build(), HttpStatus.NOT_FOUND);
        }
        libraryRepository.delete(optionalLibrary.get());
        return new ResponseEntity<>(APIResponse.builder()
                .status(200)
                .message("Success")
                .build(), HttpStatus.OK);
    }
}
