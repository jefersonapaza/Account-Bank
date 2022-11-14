package com.bootcamp.bank.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountService {

    /*
    @Autowired
    private AccountRepository accountRepository;
    */

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private SavingAccountRepository savingAccountRepository;


    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private FixedTermAccountRepository fixedTermAccountRepository;



    /*
    public Flux<Account> getCustomers(){

        return accountRepository.findAll();
    }
    */

    public Mono<SavingAccount> saveSavingAccount(AccountDto accountDto){

        SavingAccount savingAccount = new SavingAccount();
       return  savingAccountRepository.getByIdCustomer(accountDto.getIdCustomer()).switchIfEmpty(Mono.just(savingAccount).flatMap( s -> {
                s.setCode(accountDto.getCode());
                s.setAmount(accountDto.getAmount());
                s.setTypeCustomer("PERSONAL");
                s.setIdCustomer(accountDto.getIdCustomer());
                return savingAccountRepository.save(s);
            }).switchIfEmpty(  Mono.error(new IllegalArgumentException("Personal Client already has a Saving Account !!"))));
    }

    public Mono<FixedTermAccount> saveFixedTermAccount(AccountDto accountDto){
        FixedTermAccount fixedAccount = new FixedTermAccount();
        fixedAccount.setCode(accountDto.getCode());
        fixedAccount.setAmount(accountDto.getAmount());
        fixedAccount.setTypeCustomer("PERSONAL");
        return fixedTermAccountRepository.save(fixedAccount);

    }

    public Mono<CheckingAccount> saveCheckingAccount(AccountDto accountDto){
        CheckingAccount checkingAccount = new CheckingAccount();
        return  checkingAccountRepository.getByIdCustomer(accountDto.getIdCustomer()).switchIfEmpty(Mono.just(checkingAccount).flatMap( s -> {
            s.setCode(accountDto.getCode());
            s.setAmount(accountDto.getAmount());
            s.setTypeCustomer("PERSONAL");
            s.setIdCustomer(accountDto.getIdCustomer());
            return checkingAccountRepository.save(s);
        }).switchIfEmpty(  Mono.error(new IllegalArgumentException("Personal Client already has a Checking Account !!"))));
    }

    public Mono<CheckingAccount> saveCheckingAccountBusiness(AccountDto accountDto){
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setCode(accountDto.getCode());
        checkingAccount.setAmount(accountDto.getAmount());
        checkingAccount.setTypeCustomer("BUSINESS");
        return checkingAccountRepository.save(checkingAccount);
    }



    public Flux<SavingAccount> getSavingAccount(){
        return savingAccountRepository.findAll();
    }

    public Mono<Customer> getPersonaById(String idCustomer){
        return customerRepository.findCustomerByCodigo(idCustomer);
    }

    /*
    private Flux<IAccount> getAllAccountsPersonal(String id){
        return accountRepository.getAllAccountsPersonal(id);
    }
    */

}