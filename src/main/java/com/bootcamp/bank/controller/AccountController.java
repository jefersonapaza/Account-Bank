package com.bootcamp.bank.controller;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.model.Account;
import com.bootcamp.bank.model.Customer;
import com.bootcamp.bank.model.account.IAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.model.account.pasive.SavingAccount;
import com.bootcamp.bank.model.customer.Personal;
import com.bootcamp.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class AccountController {


    @Autowired
    private AccountService accountService;




    @PostMapping("/accountPersonal/saveSavingAccount")
    public Mono<SavingAccount> saveSavingAccount(@RequestBody  AccountDto accountDto){
        return accountService.savePersonalSavingAccount(accountDto);
    }

    @PostMapping("/accountPersonal/saveFixedTermAccount")
    public Mono<FixedTermAccount> saveFixedTermAccount(@RequestBody  AccountDto accountDto){
        return accountService.saveFixedTermAccount(accountDto);
    }

    @PostMapping("/accountPersonal/saveCheckingAccount")
    public Mono<CheckingAccount> saveCheckingAccount(@RequestBody  AccountDto accountDto){
        return accountService.savePersonalCheckingAccount(accountDto);
    }

    @PostMapping("/accountBusiness/saveCheckingAccount")
    public Mono<CheckingAccount> saveCheckingAccountBusiness(@RequestBody  AccountDto accountDto){
        return accountService.saveCheckingAccountBusiness(accountDto);
    }


    @GetMapping(value = "/getPersonaById/{id}")
    @ResponseBody
    public Mono<Customer> getPersonaById(@PathVariable String id) {
        return accountService.getPersonaById(id);
    }
}
