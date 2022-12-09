package com.bootcamp.bank.controller;

import com.bootcamp.bank.model.generic.Movements;
import com.bootcamp.bank.service.generic.MovementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/movements")
public class AccountController {

    @Autowired
    MovementsService movementsService;


    @GetMapping("/getByAccountAndCustomer/{id_customer}")
    public Flux<ResponseEntity<Movements>> getMovements(@PathVariable String id_customer){
        return movementsService.getMovements(id_customer).map(mov -> new  ResponseEntity<>(mov , HttpStatus.ACCEPTED));
    }


}
