package com.troia.libraryproject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.troia.libraryproject.view.Views;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
public class Book extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3787981092766931662L;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    private String name;

    @JsonView({Views.BookListing.class})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    private String isbn;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    @Column(length = 1200)
    private String description;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Instant publishDate;

    @JsonView({Views.BookListing.class, Views.AuthorListing.class})
    private int copyCount;

}
