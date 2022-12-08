package com.bootcamp.bank.service;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.active.PersonalAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.repository.account.active.PersonalAccountRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonalAccountService {

    @Autowired
    PersonalAccountRepository personalAccountRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(PersonalAccountService.class);

    public Flux<PersonalAccount> list(){
        return personalAccountRepository.findAll();
    }

    public Flux<PersonalAccount> findByCustomer(String id){
        return personalAccountRepository.getByIdCustomer(id);
    }

    public Mono<PersonalAccount> save(AccountDto accountDto){
        logger.info("SAVE : saveFixedTermAccount()");
        PersonalAccount personalAccount = new PersonalAccount();
        personalAccount.setCode(accountDto.getCode());
        personalAccount.setAmount(accountDto.getAmount());
        personalAccount.setTypeCustomer(Constants.PERSONAL);
        return personalAccountRepository.save(personalAccount);
    }

    public Mono<Boolean> delete(String id){
        return personalAccountRepository.findById(id)
                .flatMap(ca -> personalAccountRepository.delete(ca)
                        .then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }


    public Mono<PersonalAccount> update(PersonalAccount personalAccount){
        return personalAccountRepository.getPersonalAccountById(personalAccount.getId()).flatMap( ba -> {
            return personalAccountRepository.save(personalAccount);
        });
    }

    public Mono<String> depositMoneyPersonalAccount(DepositMoneyDTO depositMoneyDTO){
        return personalAccountRepository.getPersonalAccountByCode(depositMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.PERSONAL_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        ba.setAmount(depositMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Personal-Account "));
                });
    }

    public Mono<String> withdrawMoneyPersonalAccount(WithDrawMoneyDTO withDrawMoneyDTO){
        return personalAccountRepository.getPersonalAccountByCode(withDrawMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.PERSONAL_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        Float newAmount = currentAmount - withDrawMoneyDTO.getAmount();
                        if(newAmount < 0F){
                            return Mono.error(new IllegalArgumentException("There is not enough money in the account !"));
                        }
                        ba.setAmount(withDrawMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Personal-Account"));
                });
    }



}
