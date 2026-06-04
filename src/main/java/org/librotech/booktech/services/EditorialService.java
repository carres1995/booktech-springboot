package org.librotech.booktech.services;

import lombok.RequiredArgsConstructor;
import org.librotech.booktech.models.Editorial;
import org.librotech.booktech.repository.EditorialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EditorialService {
    private final EditorialRepository repository;

    @Transactional(readOnly = true)
    public List<Editorial> getAllCategories() {
        return repository.findAll();
    }

}
