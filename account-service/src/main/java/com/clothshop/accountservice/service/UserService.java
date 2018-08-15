package com.clothshop.accountservice.service;

import com.clothshop.accountservice.data.entity.User;
import com.clothshop.accountservice.data.repository.UserRepository;
import com.clothshop.accountservice.exception.EntityConstraintViolationException;
import com.clothshop.accountservice.exception.UserNotFoundException;
import com.clothshop.accountservice.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    private UserRepository userRepo;
    private MessageSource messageSource;

    @Autowired
    public UserService(UserRepository userRepo, MessageSource messageSource) {
        this.userRepo = userRepo;
        this.messageSource = messageSource;
    }

    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return userRepo.findAll(specification, pageable);
    }

    public User findById(String userId) {
        return userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public void save(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            throw new EntityConstraintViolationException(messageSource
                    .getMessage("error_message.name.required", null, null));
        }
        if (StringUtils.isEmpty(user.getEmail())) {
            throw new EntityConstraintViolationException(messageSource
                    .getMessage("error_message.email.required", null, null));
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new EntityConstraintViolationException(messageSource
                    .getMessage("error_message.password.required", null, null));
        }
        if (StringUtils.isEmpty(user.getId())) {
            user.setId(UUIDGenerator.randomUUID());
        }
        userRepo.save(user);
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    public long countByEmail(String email) {
        return userRepo.countByEmail(email);
    }
}
