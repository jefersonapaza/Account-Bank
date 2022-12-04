package com.bootcamp.bank.repository.account.active;

import com.bootcamp.bank.model.Account;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BusinessAccountRepository extends ReactiveMongoRepository<BusinessAccount,String> {

    Flux<BusinessAccount> getByIdCustomer(String IdCustomer);

}
