package com.clothshop.catalogservice.service;

import com.clothshop.catalogservice.data.entity.Category;
import com.clothshop.catalogservice.data.repository.CategoryRepository;
import com.clothshop.catalogservice.exception.CategoryNotFoundException;
import com.clothshop.catalogservice.exception.EntityConstraintViolationException;
import com.clothshop.catalogservice.util.UUIDGenerator;
import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    private MessageSource messageSource;

    @Autowired
    public CategoryService(CategoryRepository categoryRepo, MessageSource messageSource) {
        this.categoryRepo = categoryRepo;
        this.messageSource = messageSource;
    }

    public Page<Category> findAll(@Nullable Specification<Category> specification, @Nullable Pageable pageable) {
        return categoryRepo.findAll(specification, pageable);
    }

    public Category findById(@NotNull String categoryId) {
        return categoryRepo.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

    public Page<Category> findByParentIsNull(@Nullable Pageable pageable) {
        return categoryRepo.findByParentIsNull(pageable);
    }

    public Page<Category> findByParentId(@NotNull String categoryId, @Nullable Pageable pageable) {
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
            throw new EntityConstraintViolationException(
                    messageSource.getMessage("error_message.name.required", null, null));
        }
        if (StringUtils.isEmpty(category.getId())) {
            category.setId(UUIDGenerator.randomUUID());
        }
        categoryRepo.save(category);
    }

    public void delete(@NotNull Category category) {
        categoryRepo.delete(category);
    }
}
