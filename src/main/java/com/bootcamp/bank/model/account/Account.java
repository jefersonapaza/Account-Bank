package com.bootcamp.bank.model.account;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @NotEmpty
    private Long id;

    private String code;
    private String idCustomer;
    private String typeCustomer;
    private String type;
    private Float amount;

}
