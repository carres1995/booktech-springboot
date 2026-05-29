package org.librotech.booktech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record BookDetailDTO(Long id,
                            @NotBlank(message = "title don´t can empty")
                            String title,
                            @NotNull(message = "Date don´t can null")
                            LocalDate fechaPublicacion,
                            @NotNull(message = "price don´t can null")
                            Double price,
                            @NotBlank(message = "Editorial don´t can empty")
                            String nameEditorial,
                            @NotBlank(message = "country don´t can empty")
                            String countryEditorial,
                            @NotNull(message = "Not should empty")
                            List<String> categoriesNames){}
