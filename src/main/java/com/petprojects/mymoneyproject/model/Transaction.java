package com.petprojects.mymoneyproject.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
@SequenceGenerator(name = "default_generator", sequenceName = "transaction_seq", allocationSize = 1)
public class Transaction extends GenericModel{

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "type", nullable = false)
    @Enumerated
    private TransactionsType type;

    @Column(name = "amount", nullable = false)
    private long amount;

    @Column(name = "desctiption")
    private String description;

    @Column(name = "category_id", nullable = false)
    private long categoryId;

    @Column(name = "from_wallet_id", nullable = false)
    private long fromWalletId;

    @Column(name = "to_wallet_id")
    private long toWalletId;
}
