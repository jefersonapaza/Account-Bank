package com.bootcamp.bank.repository.imp;

import com.bootcamp.bank.model.Customer;
import com.bootcamp.bank.repository.CustomerRepository;
import reactor.core.publisher.Mono;

public abstract class CustomerRepositoryImp implements CustomerRepository {


    /*
    @Override
    public Mono<Customer> isCustomerPersonal(String id) {
        Mono<Customer> myCustomer = this.findCustomerByCodigo(id);
        return myCustomer.flatMap(c -> {
            if(!c.getTipo().equals("PERSONAL")){
                return Mono.error(new IllegalArgumentException("Customer is not  Personal !!"));
            }
            else if(c == null){
                return Mono.error(new IllegalArgumentException("Customer doesn't exit !!"));
            }
            return myCustomer;
        });
    }
    */


}
