package com.bootcamp.bank.service;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.repository.account.active.CreditCardAccountRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditCardAccountService {

    @Autowired
    CreditCardAccountRepository creditCardAccountRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(AccountService.class);


    public Flux<CreditCardAccount> list(){
        return creditCardAccountRepository.findAll();
    }

    public Flux<CreditCardAccount> findByCustomer(String id){
        return creditCardAccountRepository.getByIdCustomer(id);
    }


    public Mono<CreditCardAccount> saveCreditCardAccount(AccountDto accountDto){
        logger.info("SAVE : saveCreditCardAccount()");
        CreditCardAccount creditCardAccount = new CreditCardAccount();
        creditCardAccount.setCode(accountDto.getCode());
        creditCardAccount.setAmount(accountDto.getAmount());
        creditCardAccount.setTypeCustomer(Constants.PERSONAL);
        return creditCardAccountRepository.save(creditCardAccount);
    }

    public Mono<Boolean> delete(String id){
        return creditCardAccountRepository.findById(id)
                .flatMap(ca -> creditCardAccountRepository.delete(ca)
                        .then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }

}
