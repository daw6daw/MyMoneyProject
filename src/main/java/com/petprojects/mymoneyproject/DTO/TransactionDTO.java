package com.petprojects.mymoneyproject.DTO;

import com.petprojects.mymoneyproject.model.TransactionsType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO extends GenericDTO{
    private UserDTO user;
    private TransactionsType type;
    private Long amount;
    private String description;
    private CategoryDTO category;
    private WalletDTO fromWallet;
    private WalletDTO toWallet;
}
