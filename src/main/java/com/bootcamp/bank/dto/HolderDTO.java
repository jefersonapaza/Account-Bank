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
public class HolderDTO {

    private String codeAccount;

    private List<String> holders;

    public Mono<ResponseEntity<String>> validator(){
        if(this.codeAccount == null || this.codeAccount.trim().equals("")){
            return Mono.error(new IllegalArgumentException("Code Account cannot be empty !"));
        }
        if(this.holders == null || this.holders.isEmpty() ){
            return Mono.error(new IllegalArgumentException("Holders List cannot be empty !"));
        }
        return Mono.empty();
    }
}
