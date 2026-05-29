package org.librotech.booktech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150, unique = true)
    private String title;

    @Column(nullable = false, length = 60)
    private String author;

    @Column(nullable = false, length = 60, unique = true)
    private String isbn;

    @Column(nullable = false)
    private LocalDate fechaPublicacion;

    private Double price;

    // === SOFT DELETE ===
    @Column(nullable = false)
    private Boolean available = true;

    // === RELATION MANY-TO-ONE: CADA LIBRO PERTENECE A UNA EDITORIAL ===
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editorial_id", nullable = false)
    private Editorial editorial;

    // === RELATION MANY-TO-MANY: UN LIBRO TIENE MUCHOS GENEROS
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "libros_generos",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private Set<Category> categories = new HashSet<>();

    public void softDelete(Long id) {
        this.available = false;
    }

}
