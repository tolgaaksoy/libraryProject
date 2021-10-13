package com.troia.libraryproject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
public class Book extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3787981092766931662L;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    private String isbn;

    @Column(length = 1200)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Timestamp publishDate;

    private int copyCount;

}
