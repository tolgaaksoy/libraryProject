package com.troia.libraryproject.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookItem extends Book implements Serializable {

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

//    @ManyToOne
//    private Library library;
}
