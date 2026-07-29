package com.petprojects.mymoneyproject.repository;

import com.petprojects.mymoneyproject.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends GenericRepository<Category> {
    Page<Category> findAllByUserId(Long userId, Pageable pageable);
    List<Category> findAllByUserId(Long userId);
}
