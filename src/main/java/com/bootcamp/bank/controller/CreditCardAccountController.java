package com.bootcamp.bank.controller;


import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.service.active.BusinessAccountService;
import com.bootcamp.bank.service.active.CreditCardAccountService;
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

    @PostMapping("/depositMoney")
    public Mono<ResponseEntity<String>> depositMoney(@RequestBody DepositMoneyDTO depositMoneyDTO){
        return depositMoneyDTO.validator()
                .switchIfEmpty(
                        creditCardAccountService.depositMoneyCreditCardAccount(depositMoneyDTO)
                                .map(savingAccount -> new ResponseEntity<>(savingAccount,HttpStatus.CREATED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );
    }

    @PostMapping("/withdrawMoney")
    public Mono<ResponseEntity<String>> withdrawMoney(@RequestBody WithDrawMoneyDTO withDrawMoneyDTO){
        return withDrawMoneyDTO.validator()
                .switchIfEmpty(
                        creditCardAccountService.withdrawMoneyCreditCardAccount(withDrawMoneyDTO)
                                .map(savingAccount -> new ResponseEntity<>(savingAccount,HttpStatus.CREATED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );
    }

    @GetMapping("/getMoneyAvailable/{code_account}")
    public Mono<ResponseEntity<String>> getMoneyAvailable(@PathVariable String code_account){
        return creditCardAccountService.getMoneyAvailable(code_account)
                .map(moneyAvailable -> new ResponseEntity<>(moneyAvailable , HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
