package com.bootcamp.bank.service;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.dto.BusinessAccountDTO;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
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

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(AccountService.class);

    public Flux<BusinessAccount> list(){
        return businessAccountRepository.findAll();
    }

    public Flux<BusinessAccount> findByCustomer(String id){
        return businessAccountRepository.getByIdCustomer(id);
    }

    public Mono<BusinessAccount> saveBusinessAccountforPersonal(BusinessAccountDTO businessAccountDTO){
        logger.info("SAVE : saveBusinessAccountforPersonal()");
        BusinessAccount businessAccount = new BusinessAccount();
        businessAccount.setCode(businessAccountDTO.getCode());
        businessAccount.setAmount(businessAccountDTO.getAmount());
        businessAccount.setTypeCustomer(Constants.PERSONAL);
        businessAccount.setHolder(businessAccountDTO.getHolder());
        businessAccount.setSignatureAuthorized(businessAccount.getSignatureAuthorized());
        return businessAccountRepository.save(businessAccount);
    }

    public Mono<BusinessAccount> saveBusinessAccountforBusiness(BusinessAccountDTO businessAccountDTO){
        logger.info("SAVE : saveBusinessAccountforBusiness()");
        BusinessAccount businessAccount = new BusinessAccount();
        businessAccount.setCode(businessAccountDTO.getCode());
        businessAccount.setAmount(businessAccountDTO.getAmount());
        businessAccount.setTypeCustomer(Constants.BUSINESS);
        businessAccount.setHolder(businessAccountDTO.getHolder());
        businessAccount.setSignatureAuthorized(businessAccount.getSignatureAuthorized());
        return businessAccountRepository.save(businessAccount);
    }

    public Mono<Boolean> delete(String id){
        return businessAccountRepository.findById(id)
                .flatMap(ca -> businessAccountRepository.delete(ca)
                        .then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }


}
