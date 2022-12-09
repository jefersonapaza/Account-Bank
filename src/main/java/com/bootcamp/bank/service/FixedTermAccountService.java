package com.bootcamp.bank.service;


import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.model.generic.Movements;
import com.bootcamp.bank.repository.account.pasive.FixedTermAccountRepository;
import com.bootcamp.bank.repository.generic.MovementsRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class FixedTermAccountService {

    @Autowired
    private FixedTermAccountRepository fixedTermAccountRepository;

    @Autowired
    MovementsRepository movementsRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(FixedTermAccountService.class);


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
        return fixedTermAccountRepository.save(fixedAccount).flatMap( fixedAccount1 -> {
            Movements movement = new Movements();
            movement.setType(Constants.MOV_ACCOUNT);
            movement.setCreation(new Date());
            movement.setCustomer(accountDto.getIdCustomer());
            movement.setTable(fixedAccount1.getId());
            movement.setStatus(1);
            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just(fixedAccount1));
        });

    }

    public Mono<Boolean> delete(String id){
        return fixedTermAccountRepository.findById(id)
                .flatMap(ca -> fixedTermAccountRepository.delete(ca)
                        .then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }

    public Mono<FixedTermAccount> update(FixedTermAccount fixedTermAccount){
        return fixedTermAccountRepository.getFixedTermAccountById(fixedTermAccount.getId()).flatMap( ba -> {
            return fixedTermAccountRepository.save(fixedTermAccount);
        });
    }

    public Mono<String> depositMoneyFixedTermAccount(DepositMoneyDTO depositMoneyDTO){
        return fixedTermAccountRepository.getFixedTermAccountByCode(depositMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.FIXEDTERM_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        ba.setAmount(depositMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Fixed-Term Account"));
                });
    }

    public Mono<String> withdrawMoneyFixedTermAccount(WithDrawMoneyDTO withDrawMoneyDTO){
        return fixedTermAccountRepository.getFixedTermAccountByCode(withDrawMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.FIXEDTERM_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        Float newAmount = currentAmount - withDrawMoneyDTO.getAmount();
                        if(newAmount < 0F){
                            return Mono.error(new IllegalArgumentException("There is not enough money in the account !"));
                        }
                        ba.setAmount(withDrawMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Fixed-Term Account"));
                });
    }

}
