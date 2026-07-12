package com.petprojects.mymoneyproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends GenericDTO{

    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String login;
    private String password;
    private String number;
    private RoleDTO role;
}
