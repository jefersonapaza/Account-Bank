package com.bootcamp.bank.service.active;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.active.PersonalAccount;
import com.bootcamp.bank.model.generic.Movements;
import com.bootcamp.bank.repository.account.active.PersonalAccountRepository;
import com.bootcamp.bank.repository.generic.MovementsRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class PersonalAccountService {

    @Autowired
    PersonalAccountRepository personalAccountRepository;

    @Autowired
    MovementsRepository movementsRepository;

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
        personalAccount.setTransaction(0);
        return personalAccountRepository.save(personalAccount).flatMap( personalAccount1 -> {
            Movements movement = new Movements();
            movement.setType(Constants.MOV_ACCOUNT);
            movement.setCreation(new Date());
            movement.setCustomer(accountDto.getIdCustomer());
            movement.setTable(personalAccount1.getId());
            movement.setStatus(1);
            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just(personalAccount1));
        });
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
                        Integer transaction = ba.getTransaction();
                        Float newAmount = 0F;
                        if(transaction <= Constants.PERSONAL_MAX_TRANSACTION){
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
                        return this.update(ba).flatMap( businessAccount1  -> {
                            Movements movement = new Movements();
                            movement.setType(Constants.MOV_DEPOSIT_MONEY);
                            movement.setCreation(new Date());
                            // movement.setCustomer(depositMoneyDTO.getIdCustomer());
                            movement.setTable(businessAccount1.getId());
                            movement.setStatus(1);
                            movement.setDescription(depositMoneyDTO.getCodeAccount());
                            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just("Money Update ..!! "));
                        });
                    }
                    return Mono.error(new IllegalArgumentException("It is not Personal-Account "));
                });
    }

    public Mono<String> withdrawMoneyPersonalAccount(WithDrawMoneyDTO withDrawMoneyDTO){
        return personalAccountRepository.getPersonalAccountByCode(withDrawMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.PERSONAL_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        Integer transaction = ba.getTransaction();
                        Float newAmount = 0F;
                        if(transaction <= Constants.PERSONAL_MAX_TRANSACTION){
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
                        return this.update(ba).flatMap( businessAccount1  -> {
                            Movements movement = new Movements();
                            movement.setType(Constants.MOV_WITHDRAW_MONEY);
                            movement.setCreation(new Date());
                            // movement.setCustomer(depositMoneyDTO.getIdCustomer());
                            movement.setTable(businessAccount1.getId());
                            movement.setStatus(1);
                            movement.setDescription(withDrawMoneyDTO.getCodeAccount());
                            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just("Money Update ..!! "));
                        });
                    }
                    return Mono.error(new IllegalArgumentException("It is not Personal-Account"));
                });
    }

    public Mono<String> getMoneyAvailable(String code_account){
        return personalAccountRepository.getPersonalAccountByCode(code_account)
                .flatMap( ba -> {
                    return Mono.just(ba.getAmount().toString());
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found !! ")));
    }

}
