package com.clothshop.accountservice.service;

import com.clothshop.accountservice.data.entity.Role;
import com.clothshop.accountservice.data.repository.RoleRepository;
import com.clothshop.accountservice.exception.EntityConstraintViolationException;
import com.clothshop.accountservice.exception.RoleNotFoundException;
import com.clothshop.accountservice.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RoleService {

    private RoleRepository roleRepo;
    private MessageSource messageSource;

    @Autowired
    public RoleService(RoleRepository roleRepo, MessageSource messageSource) {
        this.roleRepo = roleRepo;
        this.messageSource = messageSource;
    }

    public Page<Role> findAll(Specification<Role> specification, Pageable pageable) {
        return roleRepo.findAll(specification, pageable);
    }

    public Role findById(String roleId) {
        return roleRepo.findById(roleId).orElseThrow(RoleNotFoundException::new);
    }

    public Role findByName(String name) {
        return roleRepo.findByName(name).orElseThrow(RoleNotFoundException::new);
    }

    public void save(Role role) {
        if (StringUtils.isEmpty(role.getName())) {
            throw new EntityConstraintViolationException(
                    messageSource.getMessage("error_message.name.required", null, null));
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
