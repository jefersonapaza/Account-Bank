package com.bootcamp.bank.controller;


import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.service.FixedTermAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/FixedTermAccount")
public class FixedTermAccountController {

    @Autowired
    FixedTermAccountService fixedTermAccountService;

    @GetMapping("/list")
    public Flux<FixedTermAccount> list(){
        return fixedTermAccountService.list();
    }

    @GetMapping("/findByCustomer/{id}")
    public Mono<FixedTermAccount> findByCustomer(@PathVariable String id){
        return fixedTermAccountService.findByCustomer(id);
    }

    @PostMapping("/savePersonalCustomer")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<FixedTermAccount>> savePersonalCustomer(@RequestBody AccountDto accountDto){
        return fixedTermAccountService.saveFixedTermAccount(accountDto)
                .map(fixedTermAccount -> new ResponseEntity<>(fixedTermAccount,HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return fixedTermAccountService.delete(id)
                .filter(deleteSavingAccount -> deleteSavingAccount)
                .map(deleteCustomer -> new ResponseEntity<>("FixedTerm-Account Deleted", HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }



}
