package com.bootcamp.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositMoneyDTO {

    private String codeAccount;
    private Float amount;

    private String typeAccount;

    public Mono<ResponseEntity<String>> validator(){
        if(this.codeAccount == null || this.codeAccount.trim().equals("")){
            return Mono.error(new IllegalArgumentException("Code Account cannot be empty"));
        }
        if(this.amount == null || this.amount == 0F){
            return Mono.error(new IllegalArgumentException("Amount cannot be empty or zero "));
        }
        if(this.typeAccount == null || this.typeAccount.trim().equals("")){
            return Mono.error(new IllegalArgumentException("Type Account cannot be empty"));
        }

        return Mono.empty();
    }
}
