CREATE TABLE editorials (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            address VARCHAR(500) NOT NULL,
                            country VARCHAR(100) NOT NULL,
                            founded_in INT
);

CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL UNIQUE,
                            description VARCHAR(500)
);

CREATE TABLE books (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(150) NOT NULL,
                       author VARCHAR(200) NOT NULL,
                       isbn VARCHAR(20) UNIQUE,
                       fecha_publicacion  DATE NOT NULL,
                       price DOUBLE,
                       available BOOLEAN NOT NULL DEFAULT TRUE,
                       editorial_id BIGINT NOT NULL,
                       CONSTRAINT fk_libro_editorial FOREIGN KEY (editorial_id) REFERENCES editorials(id)
);

