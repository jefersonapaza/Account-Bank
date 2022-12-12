package com.bootcamp.bank.service;


import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.HolderDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.generic.Movements;
import com.bootcamp.bank.repository.account.pasive.CheckingAccountRepository;
import com.bootcamp.bank.repository.generic.MovementsRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class CheckingAccountService {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    MovementsRepository movementsRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(CheckingAccountService.class);


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
                        return checkingAccountRepository.save(s).flatMap( checkingAccount1 -> {
                            Movements movement = new Movements();
                            movement.setType(Constants.MOV_ACCOUNT);
                            movement.setCreation(new Date());
                            movement.setCustomer(accountDto.getIdCustomer());
                            movement.setTable(checkingAccount1.getId());
                            movement.setStatus(1);
                            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just(checkingAccount1));
                        });
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
        return checkingAccountRepository.save(checkingAccount).flatMap( checkingAccount1 -> {
            Movements movement = new Movements();
            movement.setType(Constants.MOV_ACCOUNT);
            movement.setCreation(new Date());
            movement.setCustomer(accountDto.getIdCustomer());
            movement.setTable(checkingAccount1.getId());
            movement.setStatus(1);
            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just(checkingAccount1));
        });
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

    public Mono<CheckingAccount> update(CheckingAccount checkingAccount){
        return checkingAccountRepository.getCheckingAccountById(checkingAccount.getId()).flatMap( ba -> {
            return checkingAccountRepository.save(checkingAccount);
        });
    }


    public Mono<String> depositMoneyCheckingAccount(DepositMoneyDTO depositMoneyDTO){
        return checkingAccountRepository.getCheckingAccountByCode(depositMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.CHECKING_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        ba.setAmount(depositMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap( businessAccount1  -> {
                            Movements movement = new Movements();
                            movement.setType(Constants.MOV_DEPOSIT_MONEY);
                            movement.setCreation(new Date());
                            // movement.setCustomer(depositMoneyDTO.getIdCustomer());
                            movement.setTable(businessAccount1.getId());
                            movement.setStatus(1);
                            movement.setDescription(" CODE : " + depositMoneyDTO.getCodeAccount() + " AMOUNT : " + currentAmount.toString());
                            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just("Money Update ..!! "));

                        });
                    }
                    return Mono.error(new IllegalArgumentException("It is not Checking Account"));
                });
    }


    public Mono<String> withdrawMoneyCheckingAccount(WithDrawMoneyDTO withDrawMoneyDTO){
        return checkingAccountRepository.getCheckingAccountByCode(withDrawMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.CHECKING_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        Float newAmount = currentAmount - withDrawMoneyDTO.getAmount();
                        if(newAmount < 0F){
                            return Mono.error(new IllegalArgumentException("There is not enough money in the account !"));
                        }
                        ba.setAmount(withDrawMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap( businessAccount1  -> {
                            Movements movement = new Movements();
                            movement.setType(Constants.MOV_WITHDRAW_MONEY);
                            movement.setCreation(new Date());
                            // movement.setCustomer(depositMoneyDTO.getIdCustomer());
                            movement.setTable(businessAccount1.getId());
                            movement.setStatus(1);
                            movement.setDescription(" CODE : " + withDrawMoneyDTO.getCodeAccount() + " AMOUNT : " + currentAmount.toString());
                            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just("Money Update ..!! "));
                        });
                    }
                    return Mono.error(new IllegalArgumentException("It is not Checking Account"));
                });
    }

    public Mono<String> getMoneyAvailable(String code_account){
        return checkingAccountRepository.getCheckingAccountByCode(code_account)
                .flatMap( ba -> {
                    return Mono.just(ba.getAmount().toString());
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found !! ")));
    }



}
