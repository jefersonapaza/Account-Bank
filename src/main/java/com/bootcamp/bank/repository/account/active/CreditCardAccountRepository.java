package com.bootcamp.bank.repository.account.active;

import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardAccountRepository extends ReactiveMongoRepository<CreditCardAccount,String> {

}
