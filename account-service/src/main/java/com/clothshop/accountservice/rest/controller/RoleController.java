package com.clothshop.accountservice.rest.controller;

import com.clothshop.accountservice.data.entity.Role;
import com.clothshop.accountservice.domain.Message;
import com.clothshop.accountservice.exception.EntityConstraintViolationException;
import com.clothshop.accountservice.rest.dto.RoleDto;
import com.clothshop.accountservice.service.RoleService;
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
@RequestMapping("/role")
public class RoleController {

    private RoleService roleService;
    private Mapper mapper;

    @Autowired
    public RoleController(RoleService roleService, Mapper mapper) {
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @GetMapping(value = {"/all"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findAll(@RequestParam(value = "search", required = false) String search,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "size", required = false) Integer size,
                                     @RequestParam(value = "sort", required = false) String sort) {
        final SearchingAndPagingUtil<Role> util = new SearchingAndPagingUtil<>(search, page, size, sort);
        final Page<Role> pageRole = roleService.findAll(util.buildSpecification(), util.buildPageable());
        final List<RoleDto> roles = new ArrayList<>();
        final PagedResources<RoleDto> resources;

        pageRole.forEach(role -> {
            final RoleDto dto = mapper.from(role).toInstanceOf(RoleDto.class);

            dto.add(linkTo(methodOn(getClass()).findById(dto.getUid())).withSelfRel());
            roles.add(dto);
        });

        resources = new PagedResources<>(roles, new PagedResources.PageMetadata(util.getSize(), util.getNumber(),
                pageRole.getTotalElements(), pageRole.getTotalPages()));

        resources.add(linkTo(methodOn(getClass()).findAll(search, page, size, sort)).withSelfRel());
        if (pageRole.getTotalPages() > 1) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, 1, size, sort)).withRel("first"));
            resources.add(linkTo(methodOn(getClass()).findAll(search, pageRole.getTotalPages(), size, sort))
                    .withRel("last"));
        }
        if (util.getNumber() > 1 && util.getNumber() <= pageRole.getTotalPages() && !pageRole.isFirst()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, util.getNumber() - 1, size, sort))
                    .withRel("prev"));
        }
        if (!pageRole.isLast()) {
            resources.add(linkTo(methodOn(getClass()).findAll(search, util.getNumber() + 1, size, sort))
                    .withRel("next"));
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = {"/{roleId}"}, produces = {"application/json", "application/hal+json"})
    public ResponseEntity<?> findById(@PathVariable("roleId") String roleId) {
        try {
            final Role role = roleService.findById(roleId);
            final RoleDto dto = mapper.from(role).toInstanceOf(RoleDto.class);
            final Resource<RoleDto> resource = new Resource<>(dto);

            resource.add(linkTo(methodOn(getClass()).findById(roleId)).withSelfRel());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (EntityConstraintViolationException e) {
            return ResponseEntity.status(404).body(new Message(404, e.getMessage()));
        }
    }

    @PostMapping(value = {"/add"}, consumes = {"application/json", "application/hal+json"})
    public ResponseEntity<?> add(@RequestBody RoleDto dto) {
        try {
            roleService.save(mapper.from(dto).toInstanceOf(Role.class));
            return ResponseEntity.noContent().build();
        } catch (EntityConstraintViolationException e) {
            return ResponseEntity.badRequest().body(new Message(400, e.getMessage()));
        }
    }

    @PutMapping(value = {"/{roleId}"}, consumes = {"application/json", "application/hal+json"})
    public ResponseEntity<?> updateById(@PathVariable("roleId") String roleId, @RequestBody RoleDto dto) {
        try {
            final Role role = roleService.findById(roleId);

            mapper.from(dto).ignore("uid").to(role);
            roleService.save(role);
            return ResponseEntity.noContent().build();
        } catch (EntityConstraintViolationException e) {
            return ResponseEntity.badRequest().body(new Message(400, e.getMessage()));
        }
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteById(@PathVariable("roleId") String roleId) {
        try {
            final Role role = roleService.findById(roleId);

            roleService.delete(role);
            return ResponseEntity.noContent().build();
        } catch (EntityConstraintViolationException e) {
            return ResponseEntity.badRequest().body(new Message(400, e.getMessage()));
        }
    }
}
