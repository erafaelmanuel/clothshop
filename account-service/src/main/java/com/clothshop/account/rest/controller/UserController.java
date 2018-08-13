package com.clothshop.account.rest.controller;

import com.clothshop.account.data.entity.User;
import com.clothshop.account.domain.EntitySpecificationBuilder;
import com.clothshop.account.exception.EntityException;
import com.clothshop.account.rest.dto.UserDto;
import com.clothshop.account.service.UserService;
import com.rem.mappyfy.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
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
        final EntitySpecificationBuilder<User> builder = new EntitySpecificationBuilder<>();
        final List<UserDto> users = new ArrayList<>();
        final Mapper mapper = new Mapper();
        final int tempPage = (page != null && page > 0 ? page - 1 : 0);
        final int tempSize = (size != null ? size : 20);
        final String tempSort = !StringUtils.isEmpty(sort) ? sort : "name";
        final Pageable pageable = PageRequest.of(tempPage, tempSize, Sort.by(tempSort));
        final PagedResources<UserDto> resources;
        final Page<User> pageUser;

        if (!StringUtils.isEmpty(search)) {
            final Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            final Matcher matcher = pattern.matcher(search.concat(","));

            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            if (builder.getParamSize() == 0) {
                builder.with("name", ":", search);
            }
        }
        pageUser = userService.findAll(builder.build(), pageable);
        pageUser.forEach(user -> {
            final UserDto dto = mapper.from(user).toInstanceOf(UserDto.class);

            dto.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            users.add(dto);
        });

        resources = new PagedResources<>(users, new PagedResources.PageMetadata(tempSize, (tempPage + 1), pageUser
                .getTotalElements(), pageUser.getTotalPages()));

        resources.add(linkTo(methodOn(getClass()).findAll(search, page, size, sort)).withSelfRel());
        if (pageUser.getTotalPages() > 1) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, 1, size, sort)).withRel("first"));
            resources.add(linkTo(methodOn(getClass()).findAll(search, pageUser.getTotalPages(), size, sort))
                    .withRel("last"));
        }
        if ((tempPage + 1) > 1 && (tempPage + 1) <= pageUser.getTotalPages() && !pageUser.isFirst()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, (tempPage + 1) - 1, size, sort))
                    .withRel("prev"));
        }
        if (!pageUser.isLast()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, (tempPage + 1) + 1, size, sort))
                    .withRel("next"));
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = {"/{userId}"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findById(@PathVariable("userId") String userId) {
        final Mapper mapper = new Mapper();
        final User user = userService.findById(userId);

        try {
            final UserDto dto = mapper.from(user).toInstanceOf(UserDto.class);
            final Resource<UserDto> resource = new Resource<>(dto);

            resource.add(linkTo(methodOn(getClass()).findById(userId)).withSelfRel());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (EntityException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = {"/search/findByEmail"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findByEmail(@RequestParam("email") String email) {
        final Mapper mapper = new Mapper();
        final User user = userService.findByEmail(email);

        try {
            final UserDto dto = mapper.from(user).toInstanceOf(UserDto.class);
            final Resource<UserDto> resource = new Resource<>(dto);

            resource.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (EntityException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
