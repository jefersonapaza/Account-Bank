package com.bootcamp.bank.dto;

import com.bootcamp.bank.model.account.active.BusinessAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessAccountDTO {

    private String code;
    private String idCustomer;

    private Float amount;
    private List<String> holder;
    private List<String> signatureAuthorized;


    public Mono<BusinessAccount> validator(){
        if(this.code == null || this.code.trim().equals("")){
            return Mono.error(new IllegalArgumentException("Code Account cannot be empty or zero "));
        }
        if(this.idCustomer == null || this.idCustomer.trim().equals("")){
            return Mono.error(new IllegalArgumentException("Identifier Customer cannot be empty or zero "));
        }
        if(this.amount == null || this.amount <= 0F ){
            return Mono.error(new IllegalArgumentException("Amount account cannot be empty or zero"));
        }
        return Mono.empty();


    }
    
    public Mono<ResponseEntity<BusinessAccount>> validator2(){
        Mono<ResponseEntity<BusinessAccount>> ga = null;
        return ga;
    }


}
