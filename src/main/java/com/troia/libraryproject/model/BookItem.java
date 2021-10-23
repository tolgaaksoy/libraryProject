package com.troia.libraryproject.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookItem extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2532493753104776259L;

    private String barcode;

    private Boolean isReferenceOnly;

    private Instant borrowed;

    private Instant dueDate;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private BookFormat bookFormat;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private BookStatus bookStatus;

    private Instant dateOfPurchase;

    private Instant publicationDate;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false, insertable = false, updatable = false)
    private Library library;
}
