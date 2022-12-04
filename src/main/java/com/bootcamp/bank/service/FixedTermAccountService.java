package com.bootcamp.bank.service;


import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.repository.account.pasive.FixedTermAccountRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FixedTermAccountService {

    @Autowired
    private FixedTermAccountRepository fixedTermAccountRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(AccountService.class);


    public Flux<FixedTermAccount> list(){
        return fixedTermAccountRepository.findAll();
    }

    public Mono<FixedTermAccount> findByCustomer(String id){
        return fixedTermAccountRepository.getByIdCustomer(id);
    }

    public Mono<FixedTermAccount> saveFixedTermAccount(AccountDto accountDto){
        logger.info("SAVE : saveFixedTermAccount()");
        FixedTermAccount fixedAccount = new FixedTermAccount();
        fixedAccount.setCode(accountDto.getCode());
        fixedAccount.setAmount(accountDto.getAmount());
        fixedAccount.setTypeCustomer(Constants.PERSONAL);
        return fixedTermAccountRepository.save(fixedAccount);

    }

    public Mono<Boolean> delete(String id){
        return fixedTermAccountRepository.findById(id)
                .flatMap(ca -> fixedTermAccountRepository.delete(ca)
                        .then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }

}
