package org.librotech.booktech.services;

import lombok.RequiredArgsConstructor;
import org.librotech.booktech.dto.BookDetailDTO;
import org.librotech.booktech.dto.BookDto;
import org.librotech.booktech.dto.BookSummaryDTO;
import org.librotech.booktech.models.Book;
import org.librotech.booktech.models.Category;
import org.librotech.booktech.models.Editorial;
import org.librotech.booktech.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public List<Book> getAllBooks() {
        return this.repository.findAll();
    }

    /**
     * Obtiene un "slice" (fragmento) del catálogo de libros.
     * No ejecuta COUNT → más rápido que Page para catálogos masivos.
     *
     * @param page número de página (0-indexed)
     * @return Slice con los DTOs de resumen y metadatos de navegación
     */
    public Slice<BookSummaryDTO> getCatalogo(int page) {
        final Pageable pageable = PageRequest.of(page, BookService.DEFAULT_PAGE_SIZE);
        return this.repository.summaryBook(pageable);
    }

    /**
     * Obtiene un libro con TODAS sus relaciones cargadas (para edición/detalle).
     */
    public Page<BookSummaryDTO> getLibros() {
        final Pageable pageable = PageRequest.ofSize(5);
        return repository.findAllWithRelations(pageable);

    }

    public List<BookDetailDTO> allBookDetail() {
        final List<Book> books = this.repository.findAll();

        return books
                .stream()
                .map(b -> {
                    Editorial editorial = b.getEditorial();
                    final Set<Category> categories = b.getCategories();
                    final List<String> categoriesName = categories
                            .stream()
                            .map(Category::getName)
                            .toList();

                    final Long id = b.getId();
                    final String title = b.getTitle();
                    final LocalDate date = b.getFechaPublicacion();
                    final String nameEditorial = editorial.getName();
                    final String country = editorial.getCountry();
                    final Double price = b.getPrice();
                    return new BookDetailDTO(
                            id,
                            title,
                            date,
                            price,
                            nameEditorial,
                            country,
                            categoriesName

                    );
                }).toList();
    }

    public BookDetailDTO getDetailBook(Long id) {
        return repository.findById(id)
                .map(book -> {
                    var categoriesNames = book.getCategories()
                            .stream()
                            .map(c -> c.getName())
                            .toList();
                    final Editorial editorial = book.getEditorial();
                    return new BookDetailDTO(
                            book.getId(), book.getTitle(), book.getFechaPublicacion(), book.getPrice(), editorial != null ? editorial.getName() : null, editorial != null ? editorial.getCountry() : null, categoriesNames);
                })
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
    }

    public Book AddBook(BookDto bookDto) {
        if (bookDto.author().isEmpty() || bookDto.isbn().isEmpty() || bookDto.title().isEmpty()) {
            throw new RuntimeException("Input all fields");
        }
        Book book = new Book();
        book.setTitle(bookDto.title());
        book.setAuthor(bookDto.author());
        book.setIsbn(bookDto.isbn());
        book.setFechaPublicacion(bookDto.fechaPublicacion());
        return repository.save(book);
    }

    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    public void discountBook(Long id) {
        final Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
        book.softDelete(id);
        repository.save(book);
    }

    private static final int SIZE_PAGE = 50;

    public Page<Book> getWithRelations(int page, int size) {
        final int pageSize = Math.min(size, SIZE_PAGE);
        final Pageable pageable = PageRequest.of(page, pageSize);
        return this.repository.findAllWithRelationsJPQL(pageable);
    }

    public int getSIZE_PAGE() {
        return SIZE_PAGE;
    }
}
