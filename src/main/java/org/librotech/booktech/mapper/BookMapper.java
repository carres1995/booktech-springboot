package org.librotech.booktech.mapper;

import org.librotech.booktech.dto.req.BookDTOReqCreate;
import org.librotech.booktech.dto.req.BookDTOReqUpdate;
import org.librotech.booktech.dto.res.BookDTORes;
import org.librotech.booktech.models.Book;
import org.librotech.booktech.models.Category;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
) // Indica a Spring que inyecte este mapper como un @Component
public interface BookMapper {
    // === DE ENTIDAD A RESPONSE DTO ===
    @Mapping(source = "editorial.name", target = "nameEditorial")
    @Mapping(source = "categories", target = "categories", qualifiedByName = "mapGenerosToNombres")
    BookDTORes toResponseDTO(Book book);

    // Método auxiliar para extraer solo los nombres de los géneros
    @Named("mapGenerosToNombres")
    default Set<String> mapGenerosToNombres(Set<Category> categories) {
        if (categories == null) return null;
        return categories.stream().map(Category::getName).collect(Collectors.toSet());
    }

    // === DE CREATE DTO A ENTIDAD ===
    // Ignoramos el ID, disponibilidad y relaciones complejas (las manejaremos en el Service)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "available", constant = "true")
    @Mapping(target = "editorial", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Book toEntity(BookDTOReqCreate dto);

    void updateEntityFromDTO(BookDTOReqUpdate dto, @MappingTarget Book book);//@MappingTarget que actualice el objeto recibido.
}
