package com.troia.libraryproject.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.troia.libraryproject.view.Views;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Author extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -38646184279778811L;

    @JsonView({Views.AuthorListing.class, Views.BookListing.class})
    private String name;
    @JsonView({Views.AuthorListing.class, Views.BookListing.class})
    private String surname;
    @JsonView({Views.AuthorListing.class, Views.BookListing.class})
    @Column(length = 1028)
    private String description;

    @JsonView({Views.AuthorListing.class})
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "author")
    private List<Book> bookList;
}