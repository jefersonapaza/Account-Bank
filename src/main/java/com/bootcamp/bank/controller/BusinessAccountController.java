package com.bootcamp.bank.controller;


import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.BusinessAccountDTO;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.service.BusinessAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/BusinessAccount")
public class BusinessAccountController {

    @Autowired
    BusinessAccountService  businessAccountService;


    @GetMapping("/list")
    public Flux<BusinessAccount> list(){
        return businessAccountService.list();
    }

    @GetMapping("/findByCustomer/{id}")
    public Flux<BusinessAccount> findByCustomer(@PathVariable String id){
        return businessAccountService.findByCustomer(id);
    }

    @PostMapping("/savePersonal")
    public Mono<ResponseEntity<BusinessAccount>> savePersonal(@RequestBody BusinessAccountDTO businessAccountDTO){
        return businessAccountService.saveBusinessAccountforPersonal(businessAccountDTO)
                .map(businessAccount -> new ResponseEntity<>(businessAccount , HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/saveBusiness")
    public Mono<ResponseEntity<BusinessAccount>> saveBusiness(@RequestBody BusinessAccountDTO businessAccountDTO){
        return businessAccountService.saveBusinessAccountforBusiness(businessAccountDTO)
                .map(businessAccount -> new ResponseEntity<>(businessAccount , HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return businessAccountService.delete(id)
                .filter(deleteSavingAccount -> deleteSavingAccount)
                .map(deleteCustomer -> new ResponseEntity<>("Business-Account Deleted", HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }


}
