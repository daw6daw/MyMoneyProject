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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTIONS_USER"))
    private User user;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionsType type;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "description")
    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTIONS_CATEGORY"))
    private Category category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_wallet_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTIONS_FROM_WALLET"))
    private Wallet fromWallet;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_wallet_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTIONS_TO_WALLET"))
    private Wallet toWallet;
}
