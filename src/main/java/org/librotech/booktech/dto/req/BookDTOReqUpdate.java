package org.librotech.booktech.dto.req;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.librotech.booktech.validation.ValidISBN;

import java.time.LocalDate;
import java.util.Set;

public record BookDTOReqUpdate(
        @Size(min = 2, max = 255, message = "El título debe tener entre 2 y 255 caracteres")
        String title,

        @Size(min = 2, max = 255, message = "El autor debe tener entre 2 y 255 caracteres")
        String author,

        @ValidISBN(message = "debe iniciar con ISBN-")
        String isbn,

        @PastOrPresent(message = "La fecha de publicación no puede estar en el futuro")
        LocalDate fechaPublicacion,

        @Positive(message = "El precio no puede ser negativo")
        Double price,

        @Positive(message = "ID de editorial inválido")
        Long editorialId,

        Set<@Positive(message = "No pueden existir ids negativos.") Long> categoriesIds) {
}
