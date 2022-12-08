package com.bootcamp.bank.repository.account.pasive;

import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.model.account.pasive.SavingAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FixedTermAccountRepository extends ReactiveMongoRepository<FixedTermAccount,String> {

    Mono<FixedTermAccount> getByIdCustomer(String idCustomer);

    Mono<FixedTermAccount> getFixedTermAccountByCode(String code);


    Mono<FixedTermAccount> getFixedTermAccountById(Long id);
}
