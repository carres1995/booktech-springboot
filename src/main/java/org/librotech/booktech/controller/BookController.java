package org.librotech.booktech.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.librotech.booktech.dto.BookDetailDTO;
import org.librotech.booktech.dto.BookSummaryDTO;
import org.librotech.booktech.dto.req.BookDTOReqCreate;
import org.librotech.booktech.dto.req.BookDTOReqUpdate;
import org.librotech.booktech.dto.res.BookDTORes;
import org.librotech.booktech.models.Book;
import org.librotech.booktech.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Libros", description = "Gestión del catálogo de libros de LibroTech")
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;

    @Operation(
            summary = "Buscar libros con filtros opcionales",
            description = """
                    Retorna un Slice paginado de libros en formato DTO liviano (LibroResumenDTO).
                    Los filtros son opcionales y combinables. Los resultados están ordenados por
                    fecha de publicación descendente. Los registros con borrado lógico (disponible=false)
                    son excluidos automáticamente por @SQLRestriction y NUNCA aparecen en los resultados.
                    """
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> searchBooks(
            @Parameter(description = "Filtro parcial por país de editorial (insensible a mayúsculas). Ej: 'esp' encuentra 'España'")
            @RequestParam(required = false) String country,

            @Parameter(description = "ID de género para filtrar. Ej: 1=Ficción, 2=No Ficción")
            @RequestParam(required = false) Long categoryId,

            @Parameter(description = "Número de página (0-indexed)")
            @RequestParam(defaultValue = "0") int page) {

        Slice<BookSummaryDTO> slice = bookService.searchLibros(country, categoryId, page);

        Map<String, Object> response = new HashMap<>();
        response.put("libros", slice.getContent());
        response.put("currentPage", slice.getNumber());
        response.put("hasNext", slice.hasNext());
        response.put("hasPrevious", slice.hasPrevious());
        response.put("filters", Map.of(
                "pais", country != null ? country : "",
                "generoId", categoryId != null ? categoryId : ""
        ));
        return ResponseEntity.ok(response);
    }

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
    @GetMapping("/catalog")
    public ResponseEntity<Map<String, Object>> getCatalogo(@RequestParam(defaultValue = "0") int page) {
        final Slice<BookDTORes> slice = bookService.getCatalogo(page);

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
    public ResponseEntity<BookDTORes> addBook(@RequestBody @Valid BookDTOReqCreate bookDto) {
        BookDTORes newBook = bookService.AddBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTORes> getBookDetail(@PathVariable Long id) {
        final BookDTORes dto = bookService.findById(id);
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

    @Operation(
            summary = "Descatalogar libro (borrado lógico)",
            description = """
                    Realiza un soft delete: marca el libro como disponible=false.
                    El registro permanece en la base de datos para trazabilidad,
                    pero desaparece de todas las consultas del catálogo.
                    """
    )
    @DeleteMapping("({id}")
    public ResponseEntity<Map<String, String>> softDelete(@PathVariable Long id) {
        this.bookService.discountBook(id);
        final Map<String, String> response = Map.of("Messages", "Book disable succesfull");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BookDTORes> update(@PathVariable Long id, @Valid @RequestBody BookDTOReqUpdate book) {

        return ResponseEntity.ok(bookService.updateBook(id, book));
    }
}
