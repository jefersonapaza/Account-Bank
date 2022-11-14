package com.bootcamp.bank.repository.account.pasive;

import com.bootcamp.bank.model.account.active.PersonalAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CheckingAccountRepository extends ReactiveMongoRepository<CheckingAccount,String> {

    Mono<CheckingAccount> getByIdCustomer(String IdCustomer);

}
