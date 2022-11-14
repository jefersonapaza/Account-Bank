package com.bootcamp.bank.repository;

import com.bootcamp.bank.model.Account;
import com.bootcamp.bank.model.Customer;
import com.bootcamp.bank.model.customer.Personal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer,String> {

   // Mono<Customer> findById(String id);
//    Mono<Personal> getPersonaById(String id);

    /*
    Mono<Customer> getCustomerById(String Id);
    Mono<Customer> getCustomerByIdLike(String Id);
    Mono<Customer> getCustomerByTipo(String tipo);

     */
   // Flux<Customer>
  //  Mono<Customer> isCustomerPersonal(String id);
    Mono<Customer> findCustomerByTipo(String tipo);
    Mono<Customer> findCustomerByCodigo(String codigo);
  //  Mono<Customer> isCustomerPersonal(String id);
}
