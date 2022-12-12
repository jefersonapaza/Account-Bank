package com.bootcamp.bank.service.generic;


import com.bootcamp.bank.dto.AccountListDTO;
import com.bootcamp.bank.model.account.Account;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.model.account.active.PersonalAccount;
import com.bootcamp.bank.service.active.BusinessAccountService;
import com.bootcamp.bank.service.active.CreditCardAccountService;
import com.bootcamp.bank.service.active.PersonalAccountService;
import com.bootcamp.bank.service.pasive.CheckingAccountService;
import com.bootcamp.bank.service.pasive.FixedTermAccountService;
import com.bootcamp.bank.service.pasive.SavingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GenericAccountService {


    @Autowired
    BusinessAccountService businessAccountService;

    @Autowired
    CreditCardAccountService creditCardAccountService;

    @Autowired
    PersonalAccountService personalAccountService;


    @Autowired
    CheckingAccountService checkingAccountService;

    @Autowired
    FixedTermAccountService fixedTermAccountService;

    @Autowired
    SavingAccountService savingAccountService;


    public Mono<AccountListDTO> getAllProductsByCustomer(String id){

        Mono<AccountListDTO> accountDTOMono = Mono.just(new AccountListDTO());
        Flux<BusinessAccount> baccount = businessAccountService.findByCustomer(id);
        Flux<CreditCardAccount> caccount = creditCardAccountService.findByCustomer(id);
        Flux<PersonalAccount> paccount = personalAccountService.findByCustomer(id);
        return accountDTOMono.flatMap(adm1 -> baccount.collectList().map(ba -> {
            adm1.setBusinessAccountList(ba);
            return adm1;
        }).flatMap(adm2 -> caccount.collectList().map(ca -> {
            adm2.setCreditCardAccountList(ca);
            return adm2;
        })).flatMap(adm3 -> paccount.collectList().map(pa -> {
            adm3.setPersonalAccountList(pa);
            return adm3;
        })));

    }

}
