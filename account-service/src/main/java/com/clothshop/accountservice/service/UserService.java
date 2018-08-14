package com.clothshop.accountservice.service;

import com.clothshop.accountservice.data.entity.User;
import com.clothshop.accountservice.data.repository.UserRepository;
import com.clothshop.accountservice.exception.EntityException;
import com.clothshop.accountservice.util.UUIDGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    private UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return userRepo.findAll(specification, pageable);
    }

    public User findById(String userId) {
        return userRepo.findById(userId).orElseThrow(() -> new EntityException("No user found"));
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new EntityException("No user found"));
    }

    public void save(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            throw new EntityException("Property name is required!");
        }
        if (StringUtils.isEmpty(user.getEmail())) {
            throw new EntityException("Property email is required!");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new EntityException("Property password is required!");
        }
        if (StringUtils.isEmpty(user.getId())) {
            user.setId(UUIDGenerator.randomUUID());
        }
        userRepo.save(user);
    }

    public void delete(User user) {
        userRepo.delete(user);
    }
}
