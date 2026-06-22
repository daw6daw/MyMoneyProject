package com.petprojects.mymoneyproject.controller;

import com.petprojects.mymoneyproject.model.Transaction;
import com.petprojects.mymoneyproject.repository.TransactionRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Транзакции", description = "Контроллер для работы с транзакциями пользователей")
public class TransactionController extends GenericController<Transaction> {

    private final TransactionRepository transactionRepository;

    public TransactionController (TransactionRepository transactionRepository) {
        super(transactionRepository);
        this.transactionRepository = transactionRepository;
    }
}
