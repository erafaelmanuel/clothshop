package com.clothshop.catalogservice.rest.controller;

import com.clothshop.catalogservice.data.entity.Category;
import com.clothshop.catalogservice.domain.Message;
import com.clothshop.catalogservice.exception.EntityException;
import com.clothshop.catalogservice.rest.dto.CategoryDto;
import com.clothshop.catalogservice.service.CategoryService;
import com.clothshop.catalogservice.util.SearchingAndPagingUtil;
import com.rem.mappyfy.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;
    private Mapper mapper;

    @Autowired
    public CategoryController(CategoryService categoryService, Mapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @GetMapping(value = {"/all"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findAll(@RequestParam(name = "search", required = false) String search,
                                     @RequestParam(name = "page", required = false) Integer page,
                                     @RequestParam(name = "size", required = false) Integer size,
                                     @RequestParam(name = "sort", required = false) String sort) {
        final SearchingAndPagingUtil<Category> util = new SearchingAndPagingUtil<>(search, page, size, sort);
        final Page<Category> pageCategory = categoryService.findAll(util.buildSpecification(), util.buildPageable());
        final List<CategoryDto> categories = new ArrayList<>();
        final PagedResources<CategoryDto> resources;

        pageCategory.forEach(category -> {
            final CategoryDto dto = mapper.from(category).toInstanceOf(CategoryDto.class);

            dto.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            categories.add(dto);
        });
        resources = new PagedResources<>(categories, new PagedResources.PageMetadata(util.getSize(), util.getNumber(),
                pageCategory.getTotalElements(), pageCategory.getTotalPages()));

        resources.add(linkTo(methodOn(getClass()).findAll(search, page, size, sort)).withSelfRel());
        if (pageCategory.getTotalPages() > 1) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, 1, size, sort))
                    .withRel("first"));
            resources.add(linkTo(methodOn(getClass()).findAll(search, pageCategory.getTotalPages(),
                    size, sort)).withRel("last"));
        }
        if (util.getNumber() > 1 && util.getNumber() <= pageCategory.getTotalPages() && !pageCategory.isFirst()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, util.getNumber() - 1, size, sort))
                    .withRel("prev"));
        }
        if (!pageCategory.isLast()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, util.getNumber() + 1, size, sort))
                    .withRel("next"));
        }

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = "{categoryId}", produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findById(@PathVariable("categoryId") String categoryId) {
        try {
            final Category category = categoryService.findById(categoryId);
            final CategoryDto dto = mapper.from(category).toInstanceOf(CategoryDto.class);
            final Resource<CategoryDto> resource = new Resource<>(dto);

            resource.add(linkTo(methodOn(getClass()).findById(categoryId)).withSelfRel());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (EntityException e) {
            return ResponseEntity.status(404).body(new Message(404, e.getMessage()));
        }
    }

    @GetMapping(value = "/search/findByParentIsNull", produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findByParentIsNull(@RequestParam(name = "page", required = false) Integer page,
                                                @RequestParam(name = "size", required = false) Integer size,
                                                @RequestParam(name = "sort", required = false) String sort) {
        final SearchingAndPagingUtil util = new SearchingAndPagingUtil(page, size, sort);
        final Page<Category> pageCategory = categoryService.findByParentIsNull(util.buildPageable());
        final List<CategoryDto> categories = new ArrayList<>();
        final PagedResources<CategoryDto> resources;

        pageCategory.forEach(category -> {
            final CategoryDto dto = mapper.from(category).toInstanceOf(CategoryDto.class);

            dto.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            categories.add(dto);
        });
        resources = new PagedResources<>(categories, new PagedResources.PageMetadata(util.getSize(), util.getNumber(),
                pageCategory.getTotalElements(), pageCategory.getTotalPages()));

        resources.add(linkTo(methodOn(getClass()).findByParentIsNull(page, size, sort)).withSelfRel());
        if (pageCategory.getTotalPages() > 1) {
            resources.add(linkTo(methodOn(getClass()).findByParentIsNull(1, size, sort))
                    .withRel("first"));
            resources.add(linkTo(methodOn(getClass()).findByParentIsNull(pageCategory.getTotalPages(),
                    size, sort)).withRel("last"));
        }
        if (util.getNumber() > 1 && util.getNumber() <= pageCategory.getTotalPages() && !pageCategory.isFirst()) {
            resources.add(linkTo(methodOn(getClass()).findByParentIsNull(util.getNumber() - 1, size, sort))
                    .withRel("prev"));
        }
        if (!pageCategory.isLast()) {
            resources.add(linkTo(methodOn(getClass()).findByParentIsNull(util.getNumber() + 1, size, sort))
                    .withRel("next"));
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = "/search/findByParentId", produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findByParentId(@RequestParam("categoryId") String categoryId,
                                            @RequestParam(name = "page", required = false) Integer page,
                                            @RequestParam(name = "size", required = false) Integer size,
                                            @RequestParam(name = "sort", required = false) String sort) {
        final SearchingAndPagingUtil util = new SearchingAndPagingUtil(page, size, sort);
        final Page<Category> pageCategory = categoryService.findByParentId(categoryId, util.buildPageable());
        final List<CategoryDto> categories = new ArrayList<>();
        final PagedResources<CategoryDto> resources;

        pageCategory.forEach(category -> {
            final CategoryDto dto = mapper.from(category).toInstanceOf(CategoryDto.class);

            dto.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            categories.add(dto);
        });
        resources = new PagedResources<>(categories, new PagedResources.PageMetadata(util.getSize(), util.getNumber(),
                pageCategory.getTotalElements(), pageCategory.getTotalPages()));

        resources.add(linkTo(methodOn(getClass()).findByParentId(categoryId, page, size, sort)).withSelfRel());
        if (pageCategory.getTotalPages() > 1) {
            resources.add(linkTo(methodOn(getClass()).findByParentId(categoryId, 1, size, sort))
                    .withRel("first"));
            resources.add(linkTo(methodOn(getClass()).findByParentId(categoryId, pageCategory.getTotalPages(),
                    size, sort)).withRel("last"));
        }
        if (util.getNumber() > 1 && util.getNumber() <= pageCategory.getTotalPages() && !pageCategory.isFirst()) {
            resources.add(linkTo(methodOn(getClass()).findByParentId(categoryId, util.getNumber() - 1, size, sort))
                    .withRel("prev"));
        }
        if (!pageCategory.isLast()) {
            resources.add(linkTo(methodOn(getClass()).findByParentId(categoryId, util.getNumber() + 1, size, sort))
                    .withRel("next"));
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = "/search/findByAncestor", produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findByAncestor(@RequestParam("categoryId") String categoryId) {
        final List<CategoryDto> categories = new ArrayList<>();
        final Resources<CategoryDto> resources;

        categoryService.findByAncestor(categoryId).forEach(category -> {
            final CategoryDto dto = mapper.from(category).toInstanceOf(CategoryDto.class);

            dto.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            categories.add(dto);
        });
        resources = new Resources<>(categories);
        resources.add(linkTo(methodOn(getClass()).findByAncestor(categoryId)).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @PostMapping(value = {"/add"}, consumes = {"application/json", "application/hal+json"})
    public ResponseEntity<?> add(@RequestBody CategoryDto dto) {
        try {
            categoryService.save(mapper.from(dto).toInstanceOf(Category.class));
            return ResponseEntity.noContent().build();
        } catch (EntityException e) {
            return ResponseEntity.badRequest().body(new Message(400, e.getMessage()));
        }
    }

    @PutMapping(value = {"/{categoryId}"}, consumes = {"application/json", "application/hal+json"})
    public ResponseEntity<?> updateById(@PathVariable("categoryId") String categoryId, @RequestBody CategoryDto dto) {
        try {
            final Category category = categoryService.findById(categoryId);

            mapper.from(dto).ignore("uid").to(category);
            categoryService.save(category);
            return ResponseEntity.noContent().build();
        } catch (EntityException e) {
            return ResponseEntity.badRequest().body(new Message(400, e.getMessage()));
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteById(@PathVariable("categoryId") String categoryId) {
        try {
            final Category category = categoryService.findById(categoryId);

            categoryService.delete(category);
            return ResponseEntity.noContent().build();
        } catch (EntityException e) {
            return ResponseEntity.badRequest().body(new Message(400, e.getMessage()));
        }
    }
}
