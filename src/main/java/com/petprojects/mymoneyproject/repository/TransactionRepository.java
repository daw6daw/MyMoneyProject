package com.petprojects.mymoneyproject.repository;

import com.petprojects.mymoneyproject.model.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends GenericRepository<Transaction> {
}
