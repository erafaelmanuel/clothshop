package com.clothshop.catalogservice.service;

import com.clothshop.catalogservice.data.entity.Category;
import com.clothshop.catalogservice.data.repository.CategoryRepository;
import com.clothshop.catalogservice.exception.EntityException;
import com.clothshop.catalogservice.util.UUIDGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public void save(@NotNull Category category) {
        if (StringUtils.isEmpty(category.getName())) {
            throw new EntityException("Property name is required!");
        }
        if (StringUtils.isEmpty(category.getId())) {
            category.setId(UUIDGenerator.randomUUID());
        }
        categoryRepo.save(category);
    }

    public void delete(Category category) {
        categoryRepo.delete(category);
    }
}
