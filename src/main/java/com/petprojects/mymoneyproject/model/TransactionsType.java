package com.petprojects.mymoneyproject.model;

public enum TransactionsType {
    INCOME("Доход"),
    EXPENSE("Расход"),
    TRANSFER("Перевод");

    private final String transactionTypeTextDisplay;

    TransactionsType(String typeName) {
        this.transactionTypeTextDisplay = typeName;
    }

    public String getTransactionTypeTextDisplay() {
        return this.transactionTypeTextDisplay;
    }
}
