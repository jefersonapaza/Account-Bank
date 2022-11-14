package com.bootcamp.bank.repository.account.pasive;

import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedTermAccountRepository extends ReactiveMongoRepository<FixedTermAccount,String> {

}
