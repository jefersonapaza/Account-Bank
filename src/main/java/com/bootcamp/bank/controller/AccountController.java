package com.bootcamp.bank.controller;

import com.bootcamp.bank.dto.AccountListDTO;
import com.bootcamp.bank.model.generic.Movements;
import com.bootcamp.bank.service.active.BusinessAccountService;
import com.bootcamp.bank.service.generic.GenericAccountService;
import com.bootcamp.bank.service.generic.MovementsService;
import com.bootcamp.bank.service.pasive.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    MovementsService movementsService;

    @Autowired
    GenericAccountService genericAccountService;



    @GetMapping("movements/getByAccountAndCustomer/{id_customer}")
    public Flux<ResponseEntity<Movements>> getMovements(@PathVariable String id_customer){
        return movementsService.getMovements(id_customer).map(mov -> new  ResponseEntity<>(mov , HttpStatus.ACCEPTED));
    }

    @GetMapping("/getAllProductsByCustomer/{id_customer}")
    public Mono<ResponseEntity<AccountListDTO>> getAllProductsByCustomer(@PathVariable String id_customer){
        return genericAccountService.getAllProductsByCustomer(id_customer).map(aldto -> new  ResponseEntity<>(aldto , HttpStatus.ACCEPTED));
    }


}
