package org.librotech.booktech.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "editorials")
public class Editorial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    // Nuevo campo: país de la editorial
    @Column(nullable = false)
    private String country;

    private Integer foundedIn;

    // Lado inverso de la relación: una Editorial tiene muchos Libros
    @JsonIgnore
    @OneToMany(mappedBy = "editorial")
    private List<Book> Books = new ArrayList<>();
}
