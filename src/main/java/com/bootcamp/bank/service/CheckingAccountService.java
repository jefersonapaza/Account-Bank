package com.bootcamp.bank.service;


import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.repository.account.pasive.CheckingAccountRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CheckingAccountService {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(AccountService.class);


    public Mono<CheckingAccount> savePersonalCheckingAccount(AccountDto accountDto){

        logger.info("SAVE : savePersonalCheckingAccount()");

        CheckingAccount checkingAccount = new CheckingAccount();
        if(accountDto.getType().equalsIgnoreCase(Constants.PERSONAL)){
            Mono<CheckingAccount> checkingAccountFind = checkingAccountRepository.getByIdCustomer(accountDto.getIdCustomer());

            return checkingAccountFind.flux().count().flatMap(l -> (l < 1)
                            ? Mono.just(checkingAccount).flatMap( s -> {
                        s.setCode(accountDto.getCode());
                        s.setAmount(accountDto.getAmount());
                        s.setTypeCustomer(Constants.PERSONAL);
                        s.setIdCustomer(accountDto.getIdCustomer());
                        return checkingAccountRepository.save(s);
                    })
                            : Mono.error(new IllegalArgumentException("Personal clients can have only one current account"))
            );
        }
        return Mono.error(new IllegalArgumentException("Only Personal Customer can have an checking account !!"));
    }


    public Mono<CheckingAccount> saveCheckingAccountBusiness(AccountDto accountDto){
        logger.info("SAVE : saveCheckingAccountBusiness()");
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setCode(accountDto.getCode());
        checkingAccount.setAmount(accountDto.getAmount());
        checkingAccount.setTypeCustomer(Constants.BUSINESS_ACCOUNT);
        return checkingAccountRepository.save(checkingAccount);
    }


    public Flux<CheckingAccount> list(){
        return checkingAccountRepository.findAll();
    }

    public Mono<CheckingAccount> findByCustomer(String id){
        return checkingAccountRepository.getByIdCustomer(id);
    }

    public Mono<Boolean> delete(String id){
        return checkingAccountRepository.findById(id)
                .flatMap(ca -> checkingAccountRepository.delete(ca)
                        .then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }





}
