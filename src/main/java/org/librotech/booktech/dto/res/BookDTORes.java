package org.librotech.booktech.dto.res;

import java.time.LocalDate;
import java.util.Set;

public record BookDTORes(String title,
                         String author,
                         String isbn,
                         LocalDate fechaPublicacion,
                         Double price,
                         String nameEditorial, // Aplanamos la relación: solo enviamos el nombre
                         Set<String> categories) {
}
