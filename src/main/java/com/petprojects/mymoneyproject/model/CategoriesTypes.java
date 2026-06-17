package com.petprojects.mymoneyproject.model;

public enum CategoriesTypes {
    INCOME("Доходы"),
    EXPENSE("Расходы");

    private final String categoryTypeTextDisplay;

    CategoriesTypes(String typeName) {
        this.categoryTypeTextDisplay = typeName;
    }

    public String getCategoryTypeTextDisplay() {
        return this.categoryTypeTextDisplay;
    }
}
