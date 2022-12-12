package com.bootcamp.bank.service.active;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
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
public  class CreditCardAccountService {

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
        creditCardAccount.setTransaction(0);
        return creditCardAccountRepository.save(creditCardAccount).flatMap( creditCardAccount1 -> {
            Movements movement = new Movements();
            movement.setType(Constants.MOV_ACCOUNT);
            movement.setCreation(new Date());
            movement.setCustomer(accountDto.getIdCustomer());
            movement.setTable(creditCardAccount1.getId());
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
                        Integer transaction = ba.getTransaction();
                        Float newAmount = 0F;
                        if(transaction <= Constants.CREDIT_CARD_MAX_TRANSACTION){
                            newAmount = depositMoneyDTO.getAmount() + currentAmount;
                            ba.setAmount(newAmount);
                            ba.setTransaction(transaction+1);
                        }else{
                            newAmount = depositMoneyDTO.getAmount() + currentAmount - Constants.COMMISION_TRANSACTION ;
                            ba.setAmount(newAmount);
                            ba.setTransaction(transaction+1);
                        }
                        if(newAmount < 0F){
                            return Mono.error(new IllegalArgumentException("There is not enough money in the account !"));
                        }
                        return this.update(ba).flatMap( creditCardAccount1  -> {
                            Movements movement = new Movements();
                            movement.setType(Constants.MOV_DEPOSIT_MONEY);
                            movement.setCreation(new Date());
                            // movement.setCustomer(depositMoneyDTO.getIdCustomer());
                            movement.setTable(creditCardAccount1.getId());
                            movement.setStatus(1);
                            movement.setDescription(" CODE : " + depositMoneyDTO.getCodeAccount() + " AMOUNT : " + currentAmount.toString());
                            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just("Money Update ..!! "));

                        });
                    }
                    return Mono.error(new IllegalArgumentException("It is not Credit card Account"));
                });
    }

    public Mono<String> withdrawMoneyCreditCardAccount(WithDrawMoneyDTO withDrawMoneyDTO){
        return creditCardAccountRepository.getCreditCardAccountByCode(withDrawMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.CREDIT_CARD)){
                        Float currentAmount = ba.getAmount();
                        Integer transaction = ba.getTransaction();
                        Float newAmount = 0F;
                        if(transaction <= Constants.CREDIT_CARD_MAX_TRANSACTION){
                            newAmount = currentAmount - withDrawMoneyDTO.getAmount() ;
                            ba.setAmount(newAmount);
                            ba.setTransaction(transaction+1);
                        }else{
                            newAmount = currentAmount + withDrawMoneyDTO.getAmount() - Constants.COMMISION_TRANSACTION;
                            ba.setAmount(newAmount);
                            ba.setTransaction(transaction+1);
                        }
                        if(newAmount < 0F){
                            return Mono.error(new IllegalArgumentException("There is not enough money in the account !"));
                        }
                        return this.update(ba).flatMap( creditCardAccount1  -> {
                            Movements movement = new Movements();
                            movement.setType(Constants.MOV_WITHDRAW_MONEY);
                            movement.setCreation(new Date());
                            // movement.setCustomer(depositMoneyDTO.getIdCustomer());
                            movement.setTable(creditCardAccount1.getId());
                            movement.setStatus(1);
                            movement.setDescription(withDrawMoneyDTO.getCodeAccount());
                            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just("Money Update ..!! "));
                        });
                    }
                    return Mono.error(new IllegalArgumentException("It is not Credit card Account"));
                });
    }


    public Mono<String> getMoneyAvailable(String code_account){
        return creditCardAccountRepository.getCreditCardAccountByCode(code_account)
                .flatMap( ba -> {
                    return Mono.just(ba.getAmount().toString());
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found !! ")));
    }



}