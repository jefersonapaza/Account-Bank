package com.bootcamp.bank.service;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
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
            = (Logger) LoggerFactory.getLogger(SavingAccountService.class);

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

    public Mono<SavingAccount> update(SavingAccount savingAccount){
        return savingAccountRepository.getSavingAccountById(savingAccount.getId()).flatMap( ba -> {
            return savingAccountRepository.save(savingAccount);
        });
    }

    public Mono<String> depositMoneysavingAccount(DepositMoneyDTO depositMoneyDTO){
        return savingAccountRepository.getSavingAccountByCode(depositMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.SAVING_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        ba.setAmount(depositMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Saving Account "));
                });
    }


    public Mono<String> withdrawMoneysavingAccount(WithDrawMoneyDTO withDrawMoneyDTO){
        return savingAccountRepository.getSavingAccountByCode(withDrawMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.SAVING_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        Float newAmount = currentAmount - withDrawMoneyDTO.getAmount();
                        if(newAmount < 0F){
                            return Mono.error(new IllegalArgumentException("There is not enough money in the account !"));
                        }
                        ba.setAmount(withDrawMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Saving Account "));
                });
    }


}
