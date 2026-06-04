package org.librotech.booktech.repository;

import org.librotech.booktech.dto.BookSummaryDTO;
import org.librotech.booktech.dto.res.BookDTORes;
import org.librotech.booktech.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
                SELECT new org.librotech.booktech.dto.BookSummaryDTO(
                b.id,b.title,b.fechaPublicacion,b.price,e.name,e.country
                )
                from Book b
                join b.editorial e
                ORDER BY e.name desc
            
            """)
    Slice<BookDTORes> summaryBook(Pageable pageable);

    @EntityGraph(attributePaths = {"editorial", "categories"})
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"categories", "editorial"})
    List<Book> findAll();

    @EntityGraph(attributePaths = {"editorial"})
    @Query("""
                SELECT DISTINCT new org.librotech.booktech.dto.BookSummaryDTO(
                    b.id,b.title,b.fechaPublicacion,b.price,e.name,e.country
                )
                from Book b
                join b.editorial e
                ORDER BY e.name desc
            
            """)
    Page<BookSummaryDTO> findAllWithRelations(Pageable pageable);

    @Query("""
            SELECT DISTINCT b
                        FROM Book b 
                                    JOIN FETCH b.editorial e
                                    JOIN FETCH b.categories c
                        ORDER BY b.fechaPublicacion DESC
            """)
    Page<Book> findAllWithRelationsJPQL(Pageable pageable);

    // ===== BÚSQUEDA POR PAÍS DE EDITORIAL (parcial, insensible a mayúsculas) =====
    @Query("""
            SELECT DISTINCT new org.librotech.booktech.dto.BookSummaryDTO(
                b.id,b.title,b.fechaPublicacion,b.price,e.name,e.country
            )
            FROM Book b JOIN b.editorial e
            WHERE LOWER(e.country) LIKE LOWER(CONCAT('%', :country, '%'))
            ORDER BY b.fechaPublicacion DESC
            """)
    Slice<BookSummaryDTO> findByCountry(@Param("country") String country, Pageable pageable);

    // ===== BÚSQUEDA POR GÉNERO LITERARIO =====
    @Query("""
            SELECT DISTINCT new org.librotech.booktech.dto.BookSummaryDTO(
                b.id,b.title,b.fechaPublicacion,b.price,e.name,e.country
            )
            FROM Book b JOIN b.editorial e JOIN b.categories c
            WHERE c.id = :genero_id
            ORDER BY b.fechaPublicacion DESC
            """)
    Slice<BookSummaryDTO> findByCategoryId(@Param("genero_id") Long categoryId, Pageable pageable);

    // ===== BÚSQUEDA POR RANGO DE FECHAS DE PUBLICACIÓN =====
    @Query("""
            SELECT DISTINCT new org.librotech.booktech.dto.BookSummaryDTO(
                b.id,b.title,b.fechaPublicacion,b.price,e.name,e.country
            )
            FROM Book b JOIN b.editorial e
            WHERE b.fechaPublicacion BETWEEN :startDate AND :endDate
            ORDER BY b.fechaPublicacion DESC
            """)
    Slice<BookSummaryDTO> findByPublicationDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    // ===== BÚSQUEDA COMBINADA (país + género) =====
    @Query("""
            SELECT DISTINCT new org.librotech.booktech.dto.BookSummaryDTO(
                b.id,b.title,b.fechaPublicacion,b.price,e.name,e.country
            )
            FROM Book b JOIN b.editorial e JOIN b.categories c
            WHERE (:country IS NULL OR LOWER(e.country) LIKE LOWER(CONCAT('%', :country, '%')))
            AND (:genero_id IS NULL OR c.id = :genero_id)
            ORDER BY b.fechaPublicacion DESC
            """)
    Slice<BookSummaryDTO> searchBooks(
            @Param("country") String country,
            @Param("genero_id") Long categoryId,
            Pageable pageable
    );
}

