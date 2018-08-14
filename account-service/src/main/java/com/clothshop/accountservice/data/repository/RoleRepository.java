package com.clothshop.accountservice.data.repository;

import com.clothshop.accountservice.data.entity.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends PagingAndSortingRepository<Role, String>, JpaSpecificationExecutor<Role> {

    @Query("select r from Role as r where r.name = :name")
    Optional<Role> findByName(@Param("name") String name);
}
