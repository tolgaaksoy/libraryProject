package com.troia.libraryproject.service;

import com.troia.libraryproject.model.Library;
import com.troia.libraryproject.response.APIResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface LibraryService {
    ResponseEntity<APIResponse> getLibrary(String id);

    ResponseEntity<APIResponse> getAllLibrary(Pageable pageable);

    ResponseEntity<APIResponse> createLibrary(Library library);

    ResponseEntity<APIResponse> updateLibrary(Library library);

    ResponseEntity<APIResponse> deleteLibrary(String id);
}
