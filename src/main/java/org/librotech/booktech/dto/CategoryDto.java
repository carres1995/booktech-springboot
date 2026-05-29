package org.librotech.booktech.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public record CategoryDto(
        @NotBlank(message = "Category name doesn't be empty")
        @Size(max = 150, message = "Category name can't superate 150 characters")
        String name,
        String description
) {
}
