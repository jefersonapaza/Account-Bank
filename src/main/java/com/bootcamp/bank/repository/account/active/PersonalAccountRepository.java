package com.bootcamp.bank.repository.account.active;

import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.model.account.active.PersonalAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PersonalAccountRepository extends ReactiveMongoRepository<PersonalAccount,String> {

    Flux<PersonalAccount> getByIdCustomer(String IdCustomer);


}
