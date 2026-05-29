package org.librotech.booktech.dto;


import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record BookDto(
        @NotBlank(message = "title doesn't be empty")
        @Size(max = 150, message = "title can't superate 60 characters")
        String title,

        @NotBlank(message = "Author doesn't be empty")
        @Size(max = 60, message = "title can't superate 60 characters")
        String author,

        @NotBlank(message = "isbn doesn't be empty")
        @Pattern(regexp = "^[0-9]+$", message = "isbn can't superate 60 characters")
        String isbn,
        @NotNull(message = "Publication date is required")
        LocalDate fechaPublicacion
) {
}
