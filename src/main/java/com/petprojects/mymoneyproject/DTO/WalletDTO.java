package com.petprojects.mymoneyproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO extends GenericDTO{

    private UserDTO user;
    private String name;
    private Long balance;
}
