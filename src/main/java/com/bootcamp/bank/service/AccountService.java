package com.bootcamp.bank.service;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.controller.AccountController;
import com.bootcamp.bank.dto.AccountDto;
import com.bootcamp.bank.model.Account;
import com.bootcamp.bank.model.Customer;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.model.account.pasive.SavingAccount;
import com.bootcamp.bank.model.customer.Personal;
import com.bootcamp.bank.repository.CustomerRepository;
import com.bootcamp.bank.repository.account.pasive.CheckingAccountRepository;
import com.bootcamp.bank.repository.account.pasive.FixedTermAccountRepository;
import com.bootcamp.bank.repository.account.pasive.SavingAccountRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountService {

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private SavingAccountRepository savingAccountRepository;


    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private FixedTermAccountRepository fixedTermAccountRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(AccountService.class);

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

    public Mono<SavingAccount> getSavingAccountByCustomer(String idCustomer){
        logger.info("GET : getSavingAccountByCustomer()");
        Mono<SavingAccount> savingAccountFind = savingAccountRepository.getByIdCustomer(idCustomer);
        return savingAccountFind;
    }


    public Mono<FixedTermAccount> saveFixedTermAccount(AccountDto accountDto){
        logger.info("SAVE : saveFixedTermAccount()");
        FixedTermAccount fixedAccount = new FixedTermAccount();
        fixedAccount.setCode(accountDto.getCode());
        fixedAccount.setAmount(accountDto.getAmount());
        fixedAccount.setTypeCustomer(Constants.PERSONAL);
        return fixedTermAccountRepository.save(fixedAccount);

    }

    public Mono<FixedTermAccount> getFixedTermAccountByCustomer(String idCustomer){
        logger.info("GET : getFixedTermAccountByCustomer()");
        Mono<FixedTermAccount> fixedAccountFind = fixedTermAccountRepository.getByIdCustomer(idCustomer);
        return fixedAccountFind;
    }


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

    public Mono<CheckingAccount> getCheckingAccountByCustomer(String idCustomer){
        logger.info("GET : getCheckingAccountByCustomer()");
        Mono<CheckingAccount> checkingAccountFind = checkingAccountRepository.getByIdCustomer(idCustomer);
        return checkingAccountFind;
    }


    public Mono<CheckingAccount> saveCheckingAccountBusiness(AccountDto accountDto){
        logger.info("SAVE : saveCheckingAccountBusiness()");
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setCode(accountDto.getCode());
        checkingAccount.setAmount(accountDto.getAmount());
        checkingAccount.setTypeCustomer(Constants.BUSINESS_ACCOUNT);
        return checkingAccountRepository.save(checkingAccount);
    }



    public Mono<Customer> getPersonaById(String idCustomer){
        logger.info("GET : getPersonaById()");
        return customerRepository.findCustomerByCodigo(idCustomer);
    }


}