package com.clothshop.account.service;

import com.clothshop.account.data.entity.User;
import com.clothshop.account.data.repository.UserRepository;
import com.clothshop.account.exception.EntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
}
