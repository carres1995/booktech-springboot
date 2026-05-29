INSERT INTO editorials (name, address, country, founded_in) VALUES
                                                                ('Alfaguara', 'Calle Gran V�a 32', 'Espa�a', 1964),
                                                                ('Planeta', 'Av. Diagonal 662-664', 'Espa�a', 1949),
                                                                ('Penguin Random House', '1745 Broadway', 'Estados Unidos', 2013),
                                                                ('Fondo de Cultura Econ�mica', 'Carretera Picacho-Ajusco 227', 'M�xico', 1934),
                                                                ('Norma Editorial', 'Calle 10 #24-60', 'Colombia', 1960),
                                                                ('Anagrama', 'Pedr� de la Creu 58', 'Espa�a', 1969),
                                                                ('Siglo XXI Editores', 'Cerro del Agua 248', 'M�xico', 1965),
                                                                ('Editorial Oveja Negra', 'Cra 14 #79-17', 'Colombia', 1968),
                                                                ('Tusquets Editores', 'Av. Diagonal 662-664', 'Espa�a', 1969),
                                                                ('Seix Barral', 'Av. Diagonal 662-664', 'Argentina', 1911);

INSERT INTO categories (name, description) VALUES
                                               ('Ficci�n', 'Narrativa de invenci�n, novelas y cuentos imaginativos'),
                                               ('No Ficci�n', 'Textos basados en hechos reales, ensayos y cr�nicas'),
                                               ('Ciencia Ficci�n', 'Narrativa especulativa con elementos cient�ficos y tecnol�gicos'),
                                               ('Fantas�a', 'Historias en mundos imaginarios con elementos m�gicos o sobrenaturales'),
                                               ('Terror', 'Obras dise�adas para provocar miedo y tensi�n en el lector'),
                                               ('Historia', 'Textos sobre eventos y personajes hist�ricos documentados'),
                                               ('Desarrollo Personal', 'Gu�as de crecimiento personal, productividad y bienestar');

INSERT INTO books (title, author, isbn, fecha_publicacion, price, available, editorial_id) VALUES
                                                                                              ('Cien a�os de soledad', 'Gabriel Garc�a M�rquez', 'ISBN-0001', '1967-06-05', 45000.0, true, 1),
                                                                                              ('El amor en los tiempos del c�lera', 'Gabriel Garc�a M�rquez', 'ISBN-0002', '1985-09-05', 42000.0, true, 5),
                                                                                              ('1984', 'George Orwell', 'ISBN-0003', '1949-06-08', 38000.0, true, 3),
                                                                                              ('Cr�nica de una muerte anunciada', 'Gabriel Garc�a M�rquez', 'ISBN-0004', '1981-04-01', 35000.0, true, 8),
                                                                                              ('Don Quijote de la Mancha', 'Miguel de Cervantes', 'ISBN-0005', '1605-01-16', 55000.0, true, 2),
                                                                                              ('El Principito', 'Antoine de Saint-Exup�ry', 'ISBN-0006', '1943-04-06', 28000.0, true, 3),
                                                                                              ('Rayuela', 'Julio Cort�zar', 'ISBN-0007', '1963-06-28', 40000.0, true, 1),
                                                                                              ('La sombra del viento', 'Carlos Ruiz Zaf�n', 'ISBN-0008', '2001-04-01', 48000.0, true, 2),
                                                                                              ('Fahrenheit 451', 'Ray Bradbury', 'ISBN-0009', '1953-10-19', 36000.0, true, 3),
                                                                                              ('Dune', 'Frank Herbert', 'ISBN-0010', '1965-08-01', 52000.0, true, 3),
                                                                                              ('El nombre de la rosa', 'Umberto Eco', 'ISBN-0011', '1980-01-01', 47000.0, true, 6),
                                                                                              ('Los pilares de la Tierra', 'Ken Follett', 'ISBN-0012', '1989-01-01', 58000.0, true, 2),
                                                                                              ('It', 'Stephen King', 'ISBN-0013', '1986-09-15', 50000.0, true, 3),
                                                                                              ('Sapiens', 'Yuval Noah Harari', 'ISBN-0014', '2011-01-01', 62000.0, true, 4),
                                                                                              ('H�bitos at�micos', 'James Clear', 'ISBN-0015', '2018-10-16', 55000.0, true, 3),
                                                                                              ('La casa de los esp�ritus', 'Isabel Allende', 'ISBN-0016', '1982-01-01', 41000.0, true, 9),
                                                                                              ('Pedro P�ramo', 'Juan Rulfo', 'ISBN-0017', '1955-03-01', 33000.0, true, 4),
                                                                                              ('El laberinto de la soledad', 'Octavio Paz', 'ISBN-0018', '1950-01-01', 37000.0, true, 7),
                                                                                              ('Frankenstein', 'Mary Shelley', 'ISBN-0019', '1818-01-01', 30000.0, true, 6),
                                                                                              ('El resplandor', 'Stephen King', 'ISBN-0020', '1977-01-28', 44000.0, true, 3);

INSERT INTO libros_generos (libro_id, genero_id) VALUES
                                                      (1, 1), (1, 4),       -- Cien a�os de soledad: Ficci�n + Fantas�a
                                                      (2, 1),               -- El amor en los tiempos del c�lera: Ficci�n
                                                      (3, 1), (3, 3),       -- 1984: Ficci�n + Ciencia Ficci�n
                                                      (4, 1),               -- Cr�nica de una muerte anunciada: Ficci�n
                                                      (5, 1),               -- Don Quijote: Ficci�n
                                                      (6, 1), (6, 4),       -- El Principito: Ficci�n + Fantas�a
                                                      (7, 1),               -- Rayuela: Ficci�n
                                                      (8, 1), (8, 5),       -- La sombra del viento: Ficci�n + Terror
                                                      (9, 1), (9, 3),       -- Fahrenheit 451: Ficci�n + Ciencia Ficci�n
                                                      (10, 3), (10, 4),     -- Dune: Ciencia Ficci�n + Fantas�a
                                                      (11, 1), (11, 6),     -- El nombre de la rosa: Ficci�n + Historia
                                                      (12, 1), (12, 6),     -- Los pilares de la Tierra: Ficci�n + Historia
                                                      (13, 5),              -- It: Terror
                                                      (14, 2), (14, 6),     -- Sapiens: No Ficci�n + Historia
                                                      (15, 2), (15, 7),     -- H�bitos at�micos: No Ficci�n + Desarrollo Personal
                                                      (16, 1), (16, 4),     -- La casa de los esp�ritus: Ficci�n + Fantas�a
                                                      (17, 1),              -- Pedro P�ramo: Ficci�n
                                                      (18, 2), (18, 6),     -- El laberinto de la soledad: No Ficci�n + Historia
                                                      (19, 1), (19, 5),     -- Frankenstein: Ficci�n + Terror
                                                      (20, 5);              -- El resplandor: Terror