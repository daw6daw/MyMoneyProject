package com.petprojects.mymoneyproject.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
@SequenceGenerator(name = "default_generator", sequenceName = "category_seq", allocationSize = 1)
public class Category extends GenericModel{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CATEGORIES_USER"))
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING) //AI опять сказал, что так будет лучше
    private CategoriesTypes type;
}
