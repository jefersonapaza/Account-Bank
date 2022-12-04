package com.bootcamp.bank.dto;

import lombok.*;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private String code;
    private String idCustomer;
    private String type;
    private Float amount;


}
