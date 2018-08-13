package com.clothshop.account.data.repository;

import com.clothshop.account.data.entity.Role;
import com.clothshop.account.data.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User> {

    @Query("select u from User as u where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("select r from Role as r join r.users as u where u.id = :id")
    List<Role> findRolesById(@Param("id") String id);

    @Query("select count(u) from User as u where u.email = :email")
    long countByEmail(@Param("email") String email);
}
