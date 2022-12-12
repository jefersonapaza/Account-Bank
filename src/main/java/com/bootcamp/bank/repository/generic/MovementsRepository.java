package com.bootcamp.bank.repository.generic;

import com.bootcamp.bank.model.generic.Movements;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MovementsRepository extends ReactiveMongoRepository<Movements,String> {

    public Flux<Movements> getMovementsByCustomerAndStatusAndType(String customer , Integer status , String type);


}