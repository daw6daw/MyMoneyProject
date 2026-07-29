package com.petprojects.mymoneyproject.repository;

import com.petprojects.mymoneyproject.model.GenericModel;
import com.petprojects.mymoneyproject.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface GenericRepository <T extends GenericModel>
        extends JpaRepository <T, Long> {

}
