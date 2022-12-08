package com.bootcamp.bank.repository.account.pasive;

import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.model.account.pasive.SavingAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SavingAccountRepository extends ReactiveMongoRepository<SavingAccount,String> {

    Mono<SavingAccount> getByIdCustomer(String idCustomer);
    Mono<SavingAccount> getSavingAccountByCode(String code);

    Mono<SavingAccount> getSavingAccountById(Long id);

}
