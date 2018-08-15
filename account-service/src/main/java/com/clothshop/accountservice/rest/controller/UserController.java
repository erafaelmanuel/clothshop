package com.clothshop.accountservice.rest.controller;

import com.clothshop.accountservice.data.entity.User;
import com.clothshop.accountservice.domain.Message;
import com.clothshop.accountservice.exception.EntityConstraintViolationException;
import com.clothshop.accountservice.rest.dto.UserDto;
import com.clothshop.accountservice.service.UserService;
import com.clothshop.accountservice.util.SearchingAndPagingUtil;
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
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private Mapper mapper;

    @Autowired
    public UserController(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping(value = {"/all"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findAll(@RequestParam(value = "search", required = false) String search,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "size", required = false) Integer size,
                                     @RequestParam(value = "sort", required = false) String sort) {
        final SearchingAndPagingUtil<User> util = new SearchingAndPagingUtil<>(search, page, size, sort);
        final Page<User> pageUser = userService.findAll(util.buildSpecification(), util.buildPageable());
        final List<UserDto> users = new ArrayList<>();
        final PagedResources<UserDto> resources;

        pageUser.forEach(user -> {
            final UserDto dto = mapper.from(user).toInstanceOf(UserDto.class);

            dto.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            users.add(dto);
        });

        resources = new PagedResources<>(users, new PagedResources.PageMetadata(util.getSize(), util.getNumber(),
                pageUser.getTotalElements(), pageUser.getTotalPages()));

        resources.add(linkTo(methodOn(getClass()).findAll(search, page, size, sort)).withSelfRel());
        if (pageUser.getTotalPages() > 1) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, 1, size, sort)).withRel("first"));
            resources.add(linkTo(methodOn(getClass()).findAll(search, pageUser.getTotalPages(), size, sort))
                    .withRel("last"));
        }
        if (util.getNumber() > 1 && util.getNumber() <= pageUser.getTotalPages() && !pageUser.isFirst()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, util.getNumber() - 1, size, sort))
                    .withRel("prev"));
        }
        if (!pageUser.isLast()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, util.getNumber() + 1, size, sort))
                    .withRel("next"));
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = {"/{userId}"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findById(@PathVariable("userId") String userId) {
        final User user = userService.findById(userId);
        final UserDto dto = mapper.from(user).toInstanceOf(UserDto.class);
        final Resource<UserDto> resource = new Resource<>(dto);

        resource.add(linkTo(methodOn(getClass()).findById(userId)).withSelfRel());
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping(value = {"/search/findByEmail"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findByEmail(@RequestParam("email") String email) {
        final User user = userService.findByEmail(email);
        final UserDto dto = mapper.from(user).toInstanceOf(UserDto.class);
        final Resource<UserDto> resource = new Resource<>(dto);

        resource.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping(value = {"/add"}, consumes = {"application/json", "application/hal+json"})
    public ResponseEntity<?> add(@RequestBody UserDto dto) {
        userService.save(mapper.from(dto).toInstanceOf(User.class));
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = {"/{userId}"}, consumes = {"application/json", "application/hal+json"})
    public ResponseEntity<?> updateById(@PathVariable("userId") String userId, @RequestBody UserDto dto) {
        final User user = userService.findById(userId);

        mapper.from(dto).ignore("uid").to(user);
        userService.save(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteById(@PathVariable("userId") String userId) {
        final User user = userService.findById(userId);

        userService.delete(user);
        return ResponseEntity.noContent().build();
    }
}
