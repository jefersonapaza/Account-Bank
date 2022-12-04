package com.bootcamp.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessAccountDTO {

    private String code;
    private String idCustomer;
    private String type;
    private Float amount;
    private List<String> holder;
    private List<String> signatureAuthorized;
}
