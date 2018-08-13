package com.clothshop.catalog.service;

import com.clothshop.catalog.data.entity.Category;
import com.clothshop.catalog.data.repository.CategoryRepository;
import com.clothshop.catalog.exception.EntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public Page<Category> findAll(Specification<Category> specification, Pageable pageable) {
        return categoryRepo.findAll(specification, pageable);
    }

    public Category findById(@NotNull String categoryId) {
        return categoryRepo.findById(categoryId).orElseThrow(() -> new EntityException("No category found"));
    }

    public Page<Category> findByParentIsNull(Pageable pageable) {
        return categoryRepo.findByParentIsNull(pageable);
    }

    public Page<Category> findByParentId(@NotNull String categoryId, Pageable pageable) {
        return categoryRepo.findByParentId(categoryId, pageable);
    }

    public List<Category> findByAncestor(@NotNull String id) {
        final List<Category> categories = new ArrayList<>();
        categoryRepo.findByParentId(id, null).forEach(category -> {
            categories.add(category);
            categories.addAll(findByAncestor(category.getId()));
        });
        return categories;
    }
}
