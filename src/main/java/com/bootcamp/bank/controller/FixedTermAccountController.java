package com.bootcamp.bank.controller;


import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.service.pasive.FixedTermAccountService;
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

    @PostMapping("/depositMoney")
    public Mono<ResponseEntity<String>> depositMoney(@RequestBody DepositMoneyDTO depositMoneyDTO){
        return depositMoneyDTO.validator()
                .switchIfEmpty(
                        fixedTermAccountService.depositMoneyFixedTermAccount(depositMoneyDTO)
                                .map(depositMoney -> new ResponseEntity<>(depositMoney,HttpStatus.CREATED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );
    }

    @PostMapping("/withdrawMoney")
    public Mono<ResponseEntity<String>> withdrawMoney(@RequestBody WithDrawMoneyDTO withDrawMoneyDTO){
        return withDrawMoneyDTO.validator()
                .switchIfEmpty(
                        fixedTermAccountService.withdrawMoneyFixedTermAccount(withDrawMoneyDTO)
                                .map(withDrawMoney -> new ResponseEntity<>(withDrawMoney,HttpStatus.CREATED))
                                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                );
    }

    @GetMapping("/getMoneyAvailable/{code_account}")
    public Mono<ResponseEntity<String>> getMoneyAvailable(@PathVariable String code_account){
        return fixedTermAccountService.getMoneyAvailable(code_account)
                .map(moneyAvailable -> new ResponseEntity<>(moneyAvailable , HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
