CREATE TABLE libros_generos (
                                libro_id BIGINT NOT NULL,
                                genero_id BIGINT NOT NULL,
                                PRIMARY KEY (libro_id, genero_id),
                                CONSTRAINT fk_lg_book FOREIGN KEY (libro_id) REFERENCES books(id),
                                CONSTRAINT fk_lg_category FOREIGN KEY (genero_id) REFERENCES categories(id)
);

CREATE INDEX idx_books_date ON books(fecha_publicacion);
CREATE INDEX idx_books_available ON books(available);
CREATE INDEX idx_book_editorial ON books(editorial_id);
CREATE INDEX idx_editorials_pais ON editorials(country);