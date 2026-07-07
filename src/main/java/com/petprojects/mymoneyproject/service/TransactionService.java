package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.TransactionDTO;
import com.petprojects.mymoneyproject.mapper.TransactionMapper;
import com.petprojects.mymoneyproject.model.Transaction;
import com.petprojects.mymoneyproject.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService extends GenericService<Transaction, TransactionDTO> {

    protected TransactionRepository transactionRepository;

    public TransactionService (TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        super(transactionRepository, transactionMapper);
        this.transactionRepository = transactionRepository;
    }
}
