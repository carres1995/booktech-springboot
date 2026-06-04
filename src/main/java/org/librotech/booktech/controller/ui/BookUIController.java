package org.librotech.booktech.controller.ui;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.librotech.booktech.dto.BookSummaryDTO;
import org.librotech.booktech.dto.req.BookDTOReqCreate;
import org.librotech.booktech.models.Category;
import org.librotech.booktech.services.BookService;
import org.librotech.booktech.services.CategoryService;
import org.librotech.booktech.services.EditorialService;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/libros")
public class BookUIController {
    private final BookService bookService;
    private final EditorialService editorialService;

    private final CategoryService categoryService;

    @GetMapping
    public String listarLibros(
            @RequestParam(required = false) String pais,
            @RequestParam(required = false) Long generoId,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // Obtener libros filtrados y paginados
        final Slice<BookSummaryDTO> slice = bookService.searchLibros(pais, generoId, page);

        // Cargar géneros para el dropdown de filtro
        final List<Category> categories = categoryService.getAllCategories();

        // Pasar datos al modelo
        model.addAttribute("libros", slice.getContent());
        model.addAttribute("currentPage", slice.getNumber());
        model.addAttribute("hasNext", slice.hasNext());
        model.addAttribute("hasPrevious", slice.hasPrevious());
        model.addAttribute("categories", categories);

        // Mantener los filtros activos en el modelo
        model.addAttribute("filterPais", pais != null ? pais : "");
        model.addAttribute("filterGeneroId", generoId);

        return "books/catalog";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        // Debemos inyectar un objeto vacío en el modelo para que el formulario se enlace
        model.addAttribute("libroCreateDTO", new BookDTOReqCreate(null, null, null, null, null, null, null));
        model.addAttribute("editoriales", editorialService.getAllCategories()); // Lista de objetos Editorial
        model.addAttribute("todasCategorias", categoryService.getAllCategories());
        return "books/form";
    }

    @PostMapping("/guardar")
    public String guardarLibro(
            @Valid @ModelAttribute("libroCreateDTO") BookDTOReqCreate dto,
            BindingResult bindingResult, // DEBE IR JUSTO DESPUÉS DEL OBJETO VALIDADO
            Model model) {

        // Si la validación falla, recargamos la MISMA VISTA
        if (bindingResult.hasErrors()) {
            // Thymeleaf usará el BindingResult automáticamente
            return "books/form";
        }

        bookService.AddBook(dto);
        return "redirect:/admin/libros"; // Patrón PRG (Post-Redirect-Get) si tiene éxito
    }
}
