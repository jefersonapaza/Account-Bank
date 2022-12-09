package com.bootcamp.bank.repository.generic;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.generic.Movements;
import com.bootcamp.bank.service.BusinessAccountService;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementsRepository extends ReactiveMongoRepository<Movements,String> {




}