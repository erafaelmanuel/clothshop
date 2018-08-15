package com.clothshop.accountservice.service;

import com.clothshop.accountservice.data.entity.Role;
import com.clothshop.accountservice.data.repository.RoleRepository;
import com.clothshop.accountservice.exception.EntityConstraintViolationException;
import com.clothshop.accountservice.util.UUIDGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RoleService {

    private RoleRepository roleRepo;

    public RoleService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Page<Role> findAll(Specification<Role> specification, Pageable pageable) {
        return roleRepo.findAll(specification, pageable);
    }

    public Role findById(String roleId) {
        return roleRepo.findById(roleId).orElseThrow(() -> new EntityConstraintViolationException("No role found"));
    }

    public Role findByName(String name) {
        return roleRepo.findByName(name).orElseThrow(() -> new EntityConstraintViolationException("No role found"));
    }

    public void save(Role role) {
        if (StringUtils.isEmpty(role.getName())) {
            throw new EntityConstraintViolationException("Property name is required!");
        }
        if (StringUtils.isEmpty(role.getId())) {
            role.setId(UUIDGenerator.randomUUID());
        }
        roleRepo.save(role);
    }

    public void delete(Role role) {
        roleRepo.delete(role);
    }
}
