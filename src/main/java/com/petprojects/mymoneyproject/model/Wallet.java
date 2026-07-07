package com.petprojects.mymoneyproject.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallets")
@SequenceGenerator(name = "default_generator", sequenceName = "wallet_seq", allocationSize = 1)
public class Wallet extends GenericModel{

    @ManyToOne(fetch = FetchType.LAZY) //AI говорит, что лучше использовать ленивый тип загрузки
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_WALLETS_USER"))
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "balance", nullable = false)
    private Long balance;
}
