package org.librotech.booktech.repository;

import org.librotech.booktech.dto.BookSummaryDTO;
import org.librotech.booktech.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
    Slice<BookSummaryDTO> summaryBook(Pageable pageable);

    @EntityGraph(attributePaths = {"editorial", "categories"})
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"categories", "editorial"})
    List<Book> findAll();

    @EntityGraph(attributePaths = {"editorial"})
    @Query("""
                SELECT new org.librotech.booktech.dto.BookSummaryDTO(
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

}

