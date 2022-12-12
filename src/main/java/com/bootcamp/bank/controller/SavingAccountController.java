package com.bootcamp.bank.controller;


import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.pasive.SavingAccount;
import com.bootcamp.bank.service.pasive.SavingAccountService;
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

    @PostMapping("/depositMoney")
    public Mono<ResponseEntity<String>> depositMoney(@RequestBody DepositMoneyDTO depositMoneyDTO){
        return depositMoneyDTO.validator()
                .switchIfEmpty(
                        savingAccountService.depositMoneysavingAccount(depositMoneyDTO)
                                .map(depositMoney -> new ResponseEntity<>(depositMoney,HttpStatus.CREATED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );
    }

    @PostMapping("/withdrawMoney")
    public Mono<ResponseEntity<String>> withdrawMoney(@RequestBody WithDrawMoneyDTO withDrawMoneyDTO){
        return withDrawMoneyDTO.validator()
                .switchIfEmpty(
                        savingAccountService.withdrawMoneysavingAccount(withDrawMoneyDTO)
                                .map(withDrawMoney -> new ResponseEntity<>(withDrawMoney,HttpStatus.CREATED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );
    }

    @GetMapping("/getMoneyAvailable/{code_account}")
    public Mono<ResponseEntity<String>> getMoneyAvailable(@PathVariable String code_account){
        return savingAccountService.getMoneyAvailable(code_account)
                .map(moneyAvailable -> new ResponseEntity<>(moneyAvailable , HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
