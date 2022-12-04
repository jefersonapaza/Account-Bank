package com.bootcamp.bank.controller;


import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.model.account.active.PersonalAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.model.account.pasive.SavingAccount;
import com.bootcamp.bank.service.FixedTermAccountService;
import com.bootcamp.bank.service.SavingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/SavingAccount")
public class SavingAccountController {

    @Autowired
    SavingAccountService savingAccountService;

    @GetMapping("/list")
    public Flux<SavingAccount> list(){
        return savingAccountService.list();
    }


    @GetMapping("/findByCustomer/{id}")
    public Mono<SavingAccount> findByCustomer(@PathVariable String id){
        return savingAccountService.findByCustomer(id);
    }


    @PostMapping("/savePersonalCustomer")
    public Mono<ResponseEntity<SavingAccount>> savePersonalCustomer(@RequestBody AccountDto accountDto){
        return savingAccountService.savePersonalSavingAccount(accountDto)
                .map(savingAccount -> new ResponseEntity<>(savingAccount,HttpStatus.CREATED));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return savingAccountService.delete(id)
                .filter(deleteSavingAccount -> deleteSavingAccount)
                .map(deleteCustomer -> new ResponseEntity<>("Saving-Account Deleted", HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }


}
