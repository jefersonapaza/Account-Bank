package com.bootcamp.bank.dto;

import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import lombok.*;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

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

    public Mono<CheckingAccount> validator(){
        if(this.code == null || this.code.trim().equals("")){
            return Mono.error(new IllegalArgumentException("Code Account cannot be empty"));
        }
        if(this.idCustomer == null || this.idCustomer.trim().equals("")){
            return Mono.error(new IllegalArgumentException("Customer cannot be empty  "));
        }
        if(this.amount == null || this.amount <= 0F){
            return Mono.error(new IllegalArgumentException("Amount cannot be empty or zero "));
        }


        return Mono.empty();
    }
}
