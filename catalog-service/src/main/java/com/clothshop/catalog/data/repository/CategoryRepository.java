package com.clothshop.catalog.data.repository;

import com.clothshop.catalog.data.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository  extends JpaRepository<Category, String>, JpaSpecificationExecutor<Category> {

    @Query("select c from Category as c join c.parent as p where p.id = :parentId")
    Page<Category> findByParentId(@Param("parentId") String parentId, Pageable pageable);

    @Query("select c from Category as c where c.parent is null")
    Page<Category> findByParentIsNull(Pageable pageable);
}
