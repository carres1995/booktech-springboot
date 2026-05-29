package org.librotech.booktech.repository;

import org.librotech.booktech.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
