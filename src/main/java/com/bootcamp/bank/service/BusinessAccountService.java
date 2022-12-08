package com.bootcamp.bank.service;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.BusinessAccountDTO;
import com.bootcamp.bank.dto.DepositMoneyDTO;
import com.bootcamp.bank.dto.WithDrawMoneyDTO;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.repository.DatabaseSequenceRepository;
import com.bootcamp.bank.repository.account.active.BusinessAccountRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BusinessAccountService {

    @Autowired
    BusinessAccountRepository businessAccountRepository;

    @Autowired
    DatabaseSequenceService databaseSequenceService;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(BusinessAccountService.class);

    public Flux<BusinessAccount> list(){
        return businessAccountRepository.findAll();
    }

    public Flux<BusinessAccount> findByCustomer(String id){
        return businessAccountRepository.getBusinessAccountByIdCustomer(id);
    }

    public Mono<BusinessAccount> saveBusinessAccountforPersonal(BusinessAccountDTO businessAccountDTO){
        logger.info("SAVE : saveBusinessAccountforPersonal()");
        return databaseSequenceService.generateSequence(Constants.ACCOUNT_SEQUENCE).flatMap(sequence -> {
            BusinessAccount businessAccount = new BusinessAccount();
            businessAccount.setId(sequence);
            businessAccount.setCode(businessAccountDTO.getCode());
            businessAccount.setAmount(businessAccountDTO.getAmount());
            businessAccount.setTypeCustomer(Constants.PERSONAL);
            businessAccount.setHolder(businessAccountDTO.getHolder());
            businessAccount.setSignatureAuthorized(businessAccount.getSignatureAuthorized());
            return businessAccountRepository.save(businessAccount);
        });
    }

    public Mono<BusinessAccount> saveBusinessAccountforBusiness(BusinessAccountDTO businessAccountDTO){
        logger.info("SAVE : saveBusinessAccountforBusiness()");
        return databaseSequenceService.generateSequence(Constants.ACCOUNT_SEQUENCE).flatMap(sequence -> {
            BusinessAccount businessAccount = new BusinessAccount();
            businessAccount.setId(sequence);
            businessAccount.setCode(businessAccountDTO.getCode());
            businessAccount.setAmount(businessAccountDTO.getAmount());
            businessAccount.setTypeCustomer(Constants.BUSINESS);
            businessAccount.setHolder(businessAccountDTO.getHolder());
            businessAccount.setSignatureAuthorized(businessAccount.getSignatureAuthorized());
            return businessAccountRepository.save(businessAccount);

        });


    }

    public Mono<BusinessAccount> update(BusinessAccount businessAccount){
        return businessAccountRepository.getBusinessAccountById(businessAccount.getId()).flatMap( ba -> {
            return businessAccountRepository.save(businessAccount);
        });
    }

    public Mono<Boolean> delete(Long id){
        return businessAccountRepository.getBusinessAccountById(id)
                .flatMap(ca -> businessAccountRepository.delete(ca)
                        .then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }



    public Mono<String> depositMoneyBusinessAccount(DepositMoneyDTO depositMoneyDTO){
        return businessAccountRepository.getBusinessAccountByCode(depositMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.BUSINESS_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        ba.setAmount(depositMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Business Account"));
                });
    }


    public Mono<String> withdrawMoneyBusinessAccount(WithDrawMoneyDTO withDrawMoneyDTO){
        return businessAccountRepository.getBusinessAccountByCode(withDrawMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.BUSINESS_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        Float newAmount = currentAmount - withDrawMoneyDTO.getAmount();
                        if(newAmount < 0F){
                            return Mono.error(new IllegalArgumentException("There is not enough money in the account !"));
                        }
                        ba.setAmount(withDrawMoneyDTO.getAmount() + currentAmount);
                        return this.update(ba).flatMap(lo -> Mono.just("Money Update  ! " ));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Business Account"));
                });
    }




}
