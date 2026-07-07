package com.petprojects.mymoneyproject.DTO;

import com.petprojects.mymoneyproject.model.CategoriesTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO extends GenericDTO{

    private UserDTO user;
    private String name;
    private CategoriesTypes type;
}
