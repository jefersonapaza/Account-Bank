package com.bootcamp.bank.service;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.generic.Movements;
import com.bootcamp.bank.repository.account.active.CreditCardAccountRepository;
import com.bootcamp.bank.repository.generic.MovementsRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class CreditCardAccountService {

    @Autowired
    CreditCardAccountRepository creditCardAccountRepository;

    @Autowired
    MovementsRepository movementsRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(CreditCardAccountService.class);


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
        return creditCardAccountRepository.save(creditCardAccount).flatMap( creditCardAccount1 -> {
            Movements movement = new Movements();
            movement.setType(Constants.MOV_ACCOUNT);
            movement.setCreation(new Date());
            movement.setCode_customer(accountDto.getIdCustomer());
            movement.setId_table(creditCardAccount1.getId());
            movement.setStatus(1);
            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just(creditCardAccount1));
        });
    }

    public Mono<Boolean> delete(String id){
        return creditCardAccountRepository.findById(id)
                .flatMap(ca -> creditCardAccountRepository.delete(ca)
                        .then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }

    public Mono<CreditCardAccount> update(CreditCardAccount creditCardAccount){
        return creditCardAccountRepository.getCreditCardAccountById(creditCardAccount.getId()).flatMap( ba -> {
            return creditCardAccountRepository.save(creditCardAccount);
        });
    }

    public Mono<String> depositMoneyCreditCardAccount(DepositMoneyDTO depositMoneyDTO){
        return creditCardAccountRepository.getCreditCardAccountByCode(depositMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.CREDIT_CARD)){
                        Float currentAmount = ba.getAmount();
                        ba.setAmount(depositMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Credit card Account"));
                });
    }

    public Mono<String> withdrawMoneyCreditCardAccount(WithDrawMoneyDTO withDrawMoneyDTO){
        return creditCardAccountRepository.getCreditCardAccountByCode(withDrawMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.CREDIT_CARD)){
                        Float currentAmount = ba.getAmount();
                        Float newAmount = currentAmount - withDrawMoneyDTO.getAmount();
                        if(newAmount < 0F){
                            return Mono.error(new IllegalArgumentException("There is not enough money in the account !"));
                        }
                        ba.setAmount(withDrawMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Credit card Account"));
                });
    }



}
