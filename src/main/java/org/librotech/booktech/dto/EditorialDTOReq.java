package org.librotech.booktech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditorialDTOReq(
        @NotBlank(message = "Nombre del editorial no puede estar vacio")
        @Size(min = 2, max = 255, message = "El nombre debe tener entre 2 y 255 caracteres")
        String name,

        @NotBlank(message = "direccion de la editorial no puede estar vacio")
        String address,

        @NotBlank(message = "Nombre del pais no puede estar vacio")
        @Size(min = 2, max = 255, message = "El nombre pais debe tener entre 2 y 255 caracteres")
        String country
) {

}
