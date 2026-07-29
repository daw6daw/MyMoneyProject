package com.petprojects.mymoneyproject.repository;

import com.petprojects.mymoneyproject.model.Transaction;
import com.petprojects.mymoneyproject.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends GenericRepository<Transaction> {
    Page<Transaction> findAllByUserId(Long userId, Pageable pageable);
    List<Transaction> findAllByUserId(Long userId);
}
