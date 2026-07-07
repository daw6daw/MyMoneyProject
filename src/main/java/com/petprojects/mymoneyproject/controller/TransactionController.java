package com.petprojects.mymoneyproject.controller;

import com.petprojects.mymoneyproject.DTO.TransactionDTO;
import com.petprojects.mymoneyproject.model.Transaction;
import com.petprojects.mymoneyproject.repository.TransactionRepository;
import com.petprojects.mymoneyproject.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Транзакции", description = "Контроллер для работы с транзакциями пользователей")
public class TransactionController extends GenericController<Transaction, TransactionDTO> {

    public TransactionController (TransactionService transactionService) {
        super(transactionService);
    }
}
