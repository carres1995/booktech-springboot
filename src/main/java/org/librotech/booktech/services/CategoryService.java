package org.librotech.booktech.services;

import lombok.RequiredArgsConstructor;
import org.librotech.booktech.dto.CategoryDto;
import org.librotech.booktech.models.Category;
import org.librotech.booktech.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    public Category addCategory(CategoryDto categoryDto) {
        if (categoryDto.name().isEmpty()) {
            throw new RuntimeException("The field need to be completed");
        }

        Category category = new Category();
        category.setName(categoryDto.name());
        return repository.save(category);
    }
}
