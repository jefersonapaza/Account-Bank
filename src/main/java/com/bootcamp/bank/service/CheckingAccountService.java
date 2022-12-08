package com.bootcamp.bank.service;


import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.active.BusinessAccount;
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
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
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
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Checking Account"));
                });
    }





}
