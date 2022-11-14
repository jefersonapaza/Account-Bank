package com.bootcamp.bank.repository.account.active;

import com.bootcamp.bank.model.Account;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessAccountRepository extends ReactiveMongoRepository<BusinessAccount,String> {

}
