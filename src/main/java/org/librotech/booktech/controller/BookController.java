package org.librotech.booktech.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.librotech.booktech.dto.BookDetailDTO;
import org.librotech.booktech.dto.BookDto;
import org.librotech.booktech.dto.BookSummaryDTO;
import org.librotech.booktech.models.Book;
import org.librotech.booktech.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;

    @GetMapping("/graph")
    public ResponseEntity<Map<String, Object>> getBooks(Pageable pageable) {
        final Page<BookSummaryDTO> list = bookService.getLibros();
        final Map<String, Object> response = new HashMap<>();

        response.put("books", list.getContent());
        response.put("page", list.getNumber());
        response.put("size", list.getSize());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/libros?page=0
     * Retorna un fragmento (Slice) del catálogo con metadatos de navegación.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCatalogo(@RequestParam(defaultValue = "0") int page) {
        final Slice<BookSummaryDTO> slice = bookService.getCatalogo(page);

        final Map<String, Object> response = new HashMap<>();
        response.put("books", slice.getContent());
        response.put("currentPage", slice.getNumber());
        response.put("pageSize", slice.getSize());
        response.put("hasPrevious", slice.hasPrevious());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public List<BookDetailDTO> allBooks() {
        return this.bookService.allBookDetail();
    }

    @PostMapping
    public Book addBook(@RequestBody @Valid BookDto bookDto) {
        return this.bookService.AddBook(bookDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDetailDTO> getBookDetail(@PathVariable Long id) {
        final BookDetailDTO dto = bookService.getDetailBook(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/jpql")
    public ResponseEntity<Map<String, Object>> getWithRelations(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size) {
        final Page<Book> list = this.bookService.getWithRelations(page, size);
        final Map<String, Object> response = new HashMap<>();
        response.put("Books", list.getContent());
        response.put("pageTotal", list.getTotalPages());
        response.put("TotalElements", list.getTotalElements());
        response.put("currentPage", list.getNumber());
        return ResponseEntity.ok(response);
    }

    @PutMapping("({id}")
    public ResponseEntity<Map<String, String>> softDelete(Long id) {
        this.bookService.discountBook(id);
        final Map<String, String> response = Map.of("Messages", "Book disable succesfull");
        return ResponseEntity.ok(response);
    }


}
