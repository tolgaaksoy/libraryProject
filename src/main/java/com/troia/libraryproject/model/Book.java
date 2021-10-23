package com.troia.libraryproject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.troia.libraryproject.view.Views;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3787981092766931662L;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    private String name;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    private String subject;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    private String publisher;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Language language;

    @JsonView({Views.BookListing.class})
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    private String isbn;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    @Column(length = 1200)
    private String description;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    private Instant publishDate;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    private int numberOfPages;

}
