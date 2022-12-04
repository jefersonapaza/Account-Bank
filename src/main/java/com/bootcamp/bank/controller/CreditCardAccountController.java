package com.bootcamp.bank.controller;


import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.repository.account.active.CreditCardAccountRepository;
import com.bootcamp.bank.service.CreditCardAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/CreditCardAccount")
public class CreditCardAccountController {

    @Autowired
    CreditCardAccountService creditCardAccountService;


    @GetMapping("/list")
    public Flux<CreditCardAccount> list(){
        return creditCardAccountService.list();
    }

    @GetMapping("/findByCustomer/{id}")
    public Flux<CreditCardAccount> findByCustomer(@PathVariable String id){
        return creditCardAccountService.findByCustomer(id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<CreditCardAccount>> savePersonal(@RequestBody AccountDto accountDto){
        return creditCardAccountService.saveCreditCardAccount(accountDto)
                .map(creditCardAccount -> new ResponseEntity<>(creditCardAccount,HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }


    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return creditCardAccountService.delete(id)
                .filter(deleteSavingAccount -> deleteSavingAccount)
                .map(deleteCustomer -> new ResponseEntity<>("CreditCard-Account Deleted", HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
