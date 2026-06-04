package org.librotech.booktech.dto.req;

import jakarta.validation.constraints.*;
import org.librotech.booktech.validation.ValidISBN;

import java.time.LocalDate;
import java.util.Set;

public record BookDTOReqCreate(
        @NotBlank(message = "titulo del libro no puede estar vacio")
        @Size(min = 2, max = 255, message = "El título debe tener entre 2 y 255 caracteres")
        String title,

        @NotBlank(message = "autor del libro no puede estar vacio")
        @Size(min = 2, max = 255, message = "El autor debe tener entre 2 y 255 caracteres")
        String author,

        @NotBlank(message = "isbn no puede estar vacio")
        @ValidISBN(message = "debe iniciar con ISBN-")
        String isbn,

        @NotNull(message = "La fecha de publicación es obligatoria")
        @PastOrPresent(message = "La publicacion del libro no puede tener fecha futura.")
        LocalDate fechaPublicacion,

        @Positive(message = "El precio no puede ser negativo")
        @NotNull(message = "Precio no puede estar vacio")
        Double price,

        @NotNull(message = "Debe especificar el ID de la editorial")
        @Positive(message = "ID de editorial inválido")
        Long editorialId, // El cliente solo envía el ID de la editorial

        @NotEmpty(message = "El libro debe pertenecer al menos a un género")
        Set<@NotNull(message = "No pueden haber ids nulos") @Positive(message = "No pueden existir ids negativos.") Long> categoriesIds
        // El cliente envía los IDs de los géneros)
) {
}
