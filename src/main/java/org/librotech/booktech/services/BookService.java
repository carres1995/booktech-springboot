package org.librotech.booktech.services;

import lombok.RequiredArgsConstructor;
import org.librotech.booktech.dto.BookDetailDTO;
import org.librotech.booktech.dto.BookSummaryDTO;
import org.librotech.booktech.dto.req.BookDTOReqCreate;
import org.librotech.booktech.dto.req.BookDTOReqUpdate;
import org.librotech.booktech.dto.res.BookDTORes;
import org.librotech.booktech.mapper.BookMapper;
import org.librotech.booktech.models.Book;
import org.librotech.booktech.models.Category;
import org.librotech.booktech.models.Editorial;
import org.librotech.booktech.repository.BookRepository;
import org.librotech.booktech.repository.CategoryRepository;
import org.librotech.booktech.repository.EditorialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;
    private final EditorialRepository editorialRepository;
    private final CategoryRepository categoryRepository;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int SIZE_PAGE = 50;

    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Slice<BookDTORes> getCatalogo(int page) {
        final Pageable pageable = PageRequest.of(page, BookService.DEFAULT_PAGE_SIZE);
        return this.repository.findAll(pageable).map(mapper::toResponseDTO);
    }

    /**
     * Obtiene un libro con TODAS sus relaciones cargadas (para edición/detalle).
     */
    @Transactional(readOnly = true)
    public Page<BookSummaryDTO> getLibros() {
        final Pageable pageable = PageRequest.ofSize(5);
        return repository.findAllWithRelations(pageable);

    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    public BookDTORes AddBook(BookDTOReqCreate bookDto) {
        Book book = mapper.toEntity(bookDto);

        Editorial editorial = editorialRepository.findById(bookDto.editorialId()).orElseThrow(() -> new RuntimeException("Editorial no existe"));
        book.setEditorial(editorial);

        List<Category> categories = categoryRepository.findAllById(bookDto.categoriesIds().stream().collect(Collectors.toSet()));
        Set<Category> categoriesSet = new HashSet<>(categories);
        book.setCategories(categoriesSet);
        if (categories.size() != bookDto.categoriesIds().size()) {
            throw new RuntimeException("Una o mas categorias no existen");
        }
        Book bookSaved = repository.save(book);
        return mapper.toResponseDTO(book);
    }

    @Transactional(readOnly = true)
    public BookDTORes findById(Long id) {
        Book book = repository.findById(id).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        return mapper.toResponseDTO(book);
    }

    public void discountBook(Long id) {
        final Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
        book.softDelete(id);
    }

    @Transactional(readOnly = true)
    public Page<Book> getWithRelations(int page, int size) {
        final int pageSize = Math.min(size, SIZE_PAGE);
        final Pageable pageable = PageRequest.of(page, pageSize);
        return this.repository.findAllWithRelationsJPQL(pageable);
    }


    public Slice<BookSummaryDTO> searchLibros(String country, Long categoryId, int page) {
        final PageRequest pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        // Si ambos filtros están presentes, búsqueda combinada
        if (country != null && !country.isBlank() && categoryId != null) {
            return repository.searchBooks(country, categoryId, pageable);
        }
        // Solo país
        if (country != null && !country.isBlank()) {
            return repository.findByCountry(country, pageable);
        }
        // Solo género
        if (categoryId != null) {
            return repository.findByCategoryId(categoryId, pageable);
        }
        // Sin filtros: catálogo completo
        return repository.findAllWithRelations(pageable);
    }

    public BookDTORes updateBook(Long id, BookDTOReqUpdate dto) {
        Book book = repository.findById(id).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        mapper.updateEntityFromDTO(dto, book);

        Editorial editorial = editorialRepository.findById(dto.editorialId()).orElseThrow(() -> new RuntimeException("Id de editorial no existente"));
        book.setEditorial(editorial);
        List<Category> category = categoryRepository.findAllById(dto.categoriesIds().stream().collect(Collectors.toSet()));
        if (dto.categoriesIds().size() != category.size()) {
            throw new RuntimeException("Uno o mas datos de categories no se pasaron.");
        }
        Set<Category> categoriesSet = new HashSet<>(category);
        book.setCategories(categoriesSet);
        return mapper.toResponseDTO(book);
    }
}
