package com.bootcamp.bank.service;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.model.account.active.PersonalAccount;
import com.bootcamp.bank.model.account.pasive.SavingAccount;
import com.bootcamp.bank.repository.account.pasive.SavingAccountRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SavingAccountService {

    @Autowired
    SavingAccountRepository savingAccountRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(AccountService.class);

    public Flux<SavingAccount> list(){
        return savingAccountRepository.findAll();
    }

    public Mono<SavingAccount> findByCustomer(String id){
        return savingAccountRepository.getByIdCustomer(id);
    }

    public Mono<SavingAccount> savePersonalSavingAccount(AccountDto accountDto){

        SavingAccount savingAccount = new SavingAccount();
        logger.info("SAVE : savePersonalSavingAccount()");
        if(accountDto.getType().equalsIgnoreCase(Constants.PERSONAL)){
            Mono<SavingAccount> savingAccountFind = savingAccountRepository.getByIdCustomer(accountDto.getIdCustomer());

            return savingAccountFind.flux().count().flatMap(l -> (l < 1)
                            ? Mono.just(savingAccount).flatMap( s -> {
                        s.setCode(accountDto.getCode());
                        s.setAmount(accountDto.getAmount());
                        s.setTypeCustomer(Constants.BUSINESS);
                        s.setIdCustomer(accountDto.getIdCustomer());
                        return savingAccountRepository.save(s);
                    })
                            : Mono.error(new IllegalArgumentException("Personal clients can have only one current account"))
            );
        }
        return Mono.error(new IllegalArgumentException("Only Personal Customer can have an saving account !!"));
    }

    public Mono<Boolean> delete(String id){
        return savingAccountRepository.findById(id)
                .flatMap(ca -> savingAccountRepository.delete(ca)
                        .then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }


}
