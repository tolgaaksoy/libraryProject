package com.troia.libraryproject.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Author extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -38646184279778811L;

    private String name;
    private String surname;
    @Column(length = 512)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Book> bookList;
}