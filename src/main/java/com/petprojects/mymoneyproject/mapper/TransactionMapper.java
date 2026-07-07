package com.petprojects.mymoneyproject.mapper;

import com.petprojects.mymoneyproject.DTO.TransactionDTO;
import com.petprojects.mymoneyproject.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper extends GenericMapper<Transaction, TransactionDTO> {

    private final ModelMapper modelMapper;

    protected TransactionMapper (ModelMapper modelMapper) {
        super(modelMapper, Transaction.class, TransactionDTO.class);
        this.modelMapper = modelMapper;
    }

}
