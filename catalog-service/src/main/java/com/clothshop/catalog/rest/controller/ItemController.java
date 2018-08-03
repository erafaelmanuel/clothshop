package com.clothshop.catalog.rest.controller;

import com.clothshop.catalog.data.domain.EntitySpecificationBuilder;
import com.clothshop.catalog.data.entity.Item;
import com.clothshop.catalog.exception.EntityException;
import com.clothshop.catalog.rest.dto.ItemDto;
import com.clothshop.catalog.service.ItemService;
import com.rem.mappyfy.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/items")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findAll(@RequestParam(name = "search", required = false) String search,
                                     @RequestParam(name = "page", required = false) Integer page,
                                     @RequestParam(name = "size", required = false) Integer size,
                                     @RequestParam(name = "sort", required = false) String sort) {
        final EntitySpecificationBuilder<Item> builder = new EntitySpecificationBuilder<>();
        final List<ItemDto> items = new ArrayList<>();
        final Mapper mapper = new Mapper();

        final int tempPage = (page != null && page > 0 ? page - 1 : 0);
        final int tempSize = (size != null ? size : 20);
        final String tempSort = !StringUtils.isEmpty(sort) ? sort : "name";

        final Pageable pageable = PageRequest.of(tempPage, tempSize, Sort.by(tempSort));

        final PagedResources<ItemDto> resources;
        final Page<Item> pageItem;

        if (!StringUtils.isEmpty(search)) {
            final Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            final Matcher matcher = pattern.matcher(search + ",");

            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            if (builder.getParamSize() == 0) {
                builder.with("name", ":", search);
            }
        }
        pageItem = itemService.findAll(builder.build(), pageable);
        pageItem.forEach(item -> {
            final ItemDto dto = mapper.from(item).toInstanceOf(ItemDto.class);

            dto.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            items.add(dto);
        });
        resources = new PagedResources<>(items, new PagedResources.PageMetadata(tempSize, (tempPage + 1), pageItem
                .getTotalElements(), pageItem.getTotalPages()));

        resources.add(linkTo(methodOn(getClass()).findAll(search, page, size, sort)).withSelfRel());
        if (pageItem.getTotalPages() > 1) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, 1, size, sort)).withRel("first"));
            resources.add(linkTo(methodOn(getClass()).findAll(search, pageItem.getTotalPages(), size, sort))
                    .withRel("last"));
        }
        if ((tempPage + 1) > 1 && (tempPage + 1) <= pageItem.getTotalPages() && !pageItem.isFirst()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, (tempPage + 1) - 1, size, sort))
                    .withRel("prev"));
        }
        if (!pageItem.isLast()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, (tempPage + 1) + 1, size, sort))
                    .withRel("next"));
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = {"/{itemId}"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findById(@PathVariable("itemId") String itemId) {
        final Mapper mapper = new Mapper();
        final Item item = itemService.findById(itemId);
        try {
            final ItemDto dto = mapper.from(item).toInstanceOf(ItemDto.class);
            final Resource<ItemDto> resource = new Resource<>(dto);

            resource.add(linkTo(methodOn(getClass()).findById(itemId)).withSelfRel());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (EntityException e) {
            return ResponseEntity.status(404).body(e);
        }
    }
}
