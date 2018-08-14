package com.clothshop.catalog.rest.controller;

import com.clothshop.catalog.data.entity.Category;
import com.clothshop.catalog.data.entity.Item;
import com.clothshop.catalog.domain.Message;
import com.clothshop.catalog.exception.EntityException;
import com.clothshop.catalog.rest.dto.CategoryDto;
import com.clothshop.catalog.rest.dto.ItemDto;
import com.clothshop.catalog.service.ItemService;
import com.clothshop.catalog.util.SearchingAndPagingUtil;
import com.rem.mappyfy.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/items")
public class ItemController {

    private ItemService itemService;
    private Mapper mapper;

    @Autowired
    public ItemController(ItemService itemService, Mapper mapper) {
        this.itemService = itemService;
        this.mapper = mapper;
    }

    @GetMapping(value = {"/all"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findAll(@RequestParam(name = "search", required = false) String search,
                                     @RequestParam(name = "page", required = false) Integer page,
                                     @RequestParam(name = "size", required = false) Integer size,
                                     @RequestParam(name = "sort", required = false) String sort) {
        final SearchingAndPagingUtil<Item> util = new SearchingAndPagingUtil<>(search, page, size, sort);
        final List<ItemDto> items = new ArrayList<>();
        final PagedResources<ItemDto> resources;
        final Page<Item> pageItem;

        pageItem = itemService.findAll(util.buildSpecification(), util.buildPageable());
        pageItem.forEach(item -> {
            final ItemDto dto = mapper.from(item).toInstanceOf(ItemDto.class);

            dto.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            dto.add(linkTo(methodOn(getClass()).findCategoriesById(dto.getUid(), null, null, null))
                    .withRel("categories"));
            items.add(dto);
        });
        resources = new PagedResources<>(items, new PagedResources.PageMetadata(util.getSize(), util.getNumber(),
                pageItem.getTotalElements(), pageItem.getTotalPages()));

        resources.add(linkTo(methodOn(getClass()).findAll(search, page, size, sort)).withSelfRel());
        if (pageItem.getTotalPages() > 1) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, 1, size, sort)).withRel("first"));
            resources.add(linkTo(methodOn(getClass()).findAll(search, pageItem.getTotalPages(), size, sort))
                    .withRel("last"));
        }
        if (util.getNumber() > 1 && util.getNumber() <= pageItem.getTotalPages() && !pageItem.isFirst()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, util.getNumber() - 1, size, sort))
                    .withRel("prev"));
        }
        if (!pageItem.isLast()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, util.getNumber() + 1, size, sort))
                    .withRel("next"));
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = {"/{itemId}"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findById(@PathVariable("itemId") String itemId) {
        try {
            final Item item = itemService.findById(itemId);
            final ItemDto dto = mapper.from(item).toInstanceOf(ItemDto.class);
            final Resource<ItemDto> resource = new Resource<>(dto);

            resource.add(linkTo(methodOn(getClass()).findById(itemId)).withSelfRel());
            resource.add(linkTo(methodOn(getClass()).findCategoriesById(dto.getUid(), null, null, null))
                    .withRel("categories"));
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (EntityException e) {
            return ResponseEntity.status(404).body(new Message(404, e.getMessage()));
        }
    }

    @GetMapping(value = {"/search/findByCategoryId"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findByCategoryId(@RequestParam(name = "categoryId") List<String> categoryIds,
                                              @RequestParam(name = "page", required = false) Integer page,
                                              @RequestParam(name = "size", required = false) Integer size,
                                              @RequestParam(name = "sort", required = false) String sort) {
        final SearchingAndPagingUtil util = new SearchingAndPagingUtil(null, page, size, sort);
        final List<ItemDto> items = new ArrayList<>();
        final Page<Item> pageItem = itemService.findByCategoryId(categoryIds, util.buildPageable());
        final PagedResources<ItemDto> resources;

        pageItem.forEach(item -> {
            final ItemDto dto = mapper.from(item).toInstanceOf(ItemDto.class);

            dto.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            items.add(dto);
        });
        resources = new PagedResources<>(items, new PagedResources.PageMetadata(util.getSize(), util.getNumber(),
                pageItem.getTotalElements(), pageItem.getTotalPages()));

        resources.add(linkTo(methodOn(getClass()).findByCategoryId(categoryIds, page, size, sort)).withSelfRel());
        if (pageItem.getTotalPages() > 1) {
            resources.add(linkTo(methodOn(getClass()).findByCategoryId(categoryIds, 1, size, sort))
                    .withRel("first"));
            resources.add(linkTo(methodOn(getClass()).findByCategoryId(categoryIds, pageItem
                    .getTotalPages(), size, sort)).withRel("last"));
        }
        if (util.getNumber() > 1 && util.getNumber() <= pageItem.getTotalPages() && !pageItem.isFirst()) {
            resources.add(linkTo(methodOn(getClass()).findByCategoryId(categoryIds, util.getNumber() - 1, size,
                    sort)).withRel("prev"));
        }
        if (!pageItem.isLast()) {
            resources.add(linkTo(methodOn(getClass()).findByCategoryId(categoryIds, util.getNumber() + 1, size,
                    sort)).withRel("next"));
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = {"/{itemId}/categories"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findCategoriesById(@PathVariable("itemId") String itemId,
                                                @RequestParam(name = "page", required = false) Integer page,
                                                @RequestParam(name = "size", required = false) Integer size,
                                                @RequestParam(name = "sort", required = false) String sort) {
        final SearchingAndPagingUtil util = new SearchingAndPagingUtil(null, page, size, sort);
        final List<CategoryDto> categories = new ArrayList<>();
        final Page<Category> pageCategory = itemService.findCategoriesById(itemId, util.buildPageable());
        final PagedResources<CategoryDto> resources;

        pageCategory.forEach(category -> {
            final CategoryDto dto = mapper.from(category).toInstanceOf(CategoryDto.class);

            dto.add(linkTo(methodOn(CategoryController.class).findById(dto.getUid())).withSelfRel());
            categories.add(dto);
        });
        resources = new PagedResources<>(categories, new PagedResources.PageMetadata(util.getSize(), util.getNumber(),
                pageCategory.getTotalElements(), pageCategory.getTotalPages()));

        resources.add(linkTo(methodOn(getClass()).findCategoriesById(itemId, page, size, sort)).withSelfRel());
        if (pageCategory.getTotalPages() > 1) {
            resources.add(linkTo(methodOn(getClass()).findCategoriesById(itemId, 1, size, sort))
                    .withRel("first"));
            resources.add(linkTo(methodOn(getClass()).findCategoriesById(itemId, pageCategory
                    .getTotalPages(), size, sort)).withRel("last"));
        }
        if (util.getNumber() > 1 && util.getNumber() <= pageCategory.getTotalPages() && !pageCategory.isFirst()) {
            resources.add(linkTo(methodOn(getClass()).findCategoriesById(itemId, util.getNumber() - 1, size, sort))
                    .withRel("prev"));
        }
        if (!pageCategory.isLast()) {
            resources.add(linkTo(methodOn(getClass()).findCategoriesById(itemId, util.getNumber() + 1, size, sort))
                    .withRel("next"));
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @PostMapping(value = {"/add"}, consumes = {"application/json", "application/hal+json"})
    public ResponseEntity<?> add(@RequestBody ItemDto dto) {
        try {
            itemService.save(mapper.from(dto).toInstanceOf(Item.class));
            return ResponseEntity.noContent().build();
        } catch (EntityException e) {
            return ResponseEntity.badRequest().body(new Message(400, e.getMessage()));
        }
    }

    @PutMapping(value = {"/{itemId}"}, consumes = {"application/json", "application/hal+json"})
    public ResponseEntity<?> updateById(@PathVariable("itemId") String itemId, @RequestBody ItemDto dto) {
        try {
            final Item item = itemService.findById(itemId);

            mapper.from(dto).ignore("uid").to(item);
            itemService.save(item);
            return ResponseEntity.noContent().build();
        } catch (EntityException e) {
            return ResponseEntity.badRequest().body(new Message(400, e.getMessage()));
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteById(@PathVariable("itemId") String itemId) {
        try {
            final Item item = itemService.findById(itemId);

            itemService.delete(item);
            return ResponseEntity.noContent().build();
        } catch (EntityException e) {
            return ResponseEntity.badRequest().body(new Message(400, e.getMessage()));
        }
    }
}
