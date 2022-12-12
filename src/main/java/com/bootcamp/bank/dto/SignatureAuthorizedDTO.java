package com.bootcamp.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignatureAuthorizedDTO {


    private String codeAccount;

    private List<String> signatures;

    public Mono<ResponseEntity<String>> validator(){
        if(this.codeAccount == null || this.codeAccount.trim().equals("")){
            return Mono.error(new IllegalArgumentException("Code Account cannot be empty !"));
        }
        if(this.signatures == null || this.signatures.isEmpty() ){
            return Mono.error(new IllegalArgumentException("Signature Authorized List cannot be empty !"));
        }
        return Mono.empty();
    }
}
