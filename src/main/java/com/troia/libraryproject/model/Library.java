package com.troia.libraryproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Library extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3988379135942511361L;

    private String name;

}
