package com.bootcamp.bank.controller;


import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.service.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/CheckingAccount")
public class CheckingAccountController {

    @Autowired
    private CheckingAccountService checkingAccountService;


    @GetMapping("/list")
    public Flux<CheckingAccount> list(){
        return checkingAccountService.list();
    }

    @GetMapping("/findByCustomer/{id}")
    public Mono<CheckingAccount> findByCustomer(@PathVariable String id){
        return checkingAccountService.findByCustomer(id);
    }

    @PostMapping("/savePersonal")
    public Mono<ResponseEntity<CheckingAccount>> savePersonal(@RequestBody AccountDto accountDto){
        return checkingAccountService.savePersonalCheckingAccount(accountDto)
                .map(checkingAccount -> new ResponseEntity<>(checkingAccount,HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/saveBusiness")
    public Mono<ResponseEntity<CheckingAccount>> saveBusiness(@RequestBody  AccountDto accountDto){

        return accountDto.validator()
                .map(myerror -> new ResponseEntity<>(myerror , HttpStatus.CREATED) )
                .switchIfEmpty(
                        checkingAccountService.saveCheckingAccountBusiness(accountDto)
                                .map(checkingAccount -> new ResponseEntity<>(checkingAccount,HttpStatus.CREATED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );

    }


    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return checkingAccountService.delete(id)
                .filter(deleteSavingAccount -> deleteSavingAccount)
                .map(deleteCustomer -> new ResponseEntity<>("Checking-Account Deleted", HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/depositMoney")
    public Mono<ResponseEntity<String>> depositMoney(@RequestBody DepositMoneyDTO depositMoneyDTO){
        return depositMoneyDTO.validator()
                .switchIfEmpty(
                        checkingAccountService.depositMoneyCheckingAccount(depositMoneyDTO)
                                .map(businessAccount -> new ResponseEntity<>(businessAccount , HttpStatus.ACCEPTED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );
    }

    @PostMapping("/withdrawMoney")
    public Mono<ResponseEntity<String>> withdrawMoney(@RequestBody WithDrawMoneyDTO withDrawMoneyDTO){
        return withDrawMoneyDTO.validator()
                .switchIfEmpty(
                        checkingAccountService.withdrawMoneyCheckingAccount(withDrawMoneyDTO)
                                .map(businessAccount -> new ResponseEntity<>(businessAccount , HttpStatus.ACCEPTED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );
    }

    @GetMapping("/getMoneyAvailable/{code_account}")
    public Mono<ResponseEntity<String>> getMoneyAvailable(@PathVariable String code_account){
        return checkingAccountService.getMoneyAvailable(code_account)
                .map(moneyAvailable -> new ResponseEntity<>(moneyAvailable , HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
