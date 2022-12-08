package com.bootcamp.bank.controller;


import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.model.account.active.PersonalAccount;
import com.bootcamp.bank.service.CreditCardAccountService;
import com.bootcamp.bank.service.PersonalAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/PersonalAccount")
public class PersonalAccountController {

    @Autowired
    PersonalAccountService personalAccountService;

    @GetMapping("/list")
    public Flux<PersonalAccount> list(){
        return personalAccountService.list();
    }

    @GetMapping("/findByCustomer/{id}")
    public Flux<PersonalAccount> findByCustomer(@PathVariable String id){
        return personalAccountService.findByCustomer(id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<PersonalAccount>> save(@RequestBody AccountDto accountDto){
        return personalAccountService.save(accountDto)
                .map(personalAccount -> new ResponseEntity<>(personalAccount,HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return personalAccountService.delete(id)
                .filter(deleteSavingAccount -> deleteSavingAccount)
                .map(deleteCustomer -> new ResponseEntity<>("Personal-Account Deleted", HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/depositMoney")
    public Mono<ResponseEntity<String>> depositMoney(@RequestBody DepositMoneyDTO depositMoneyDTO){
        return depositMoneyDTO.validator()
                .switchIfEmpty(
                        personalAccountService.depositMoneyPersonalAccount(depositMoneyDTO)
                                .map(depositMoney -> new ResponseEntity<>(depositMoney,HttpStatus.CREATED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );
    }

    @PostMapping("/withdrawMoney")
    public Mono<ResponseEntity<String>> withdrawMoney(@RequestBody WithDrawMoneyDTO withDrawMoneyDTO){
        return withDrawMoneyDTO.validator()
                .switchIfEmpty(
                        personalAccountService.withdrawMoneyPersonalAccount(withDrawMoneyDTO)
                                .map(withDrawMoney -> new ResponseEntity<>(withDrawMoney,HttpStatus.CREATED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );
    }



}
