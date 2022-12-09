package com.bootcamp.bank.service.generic;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.model.generic.Movements;
import com.bootcamp.bank.repository.account.active.BusinessAccountRepository;
import com.bootcamp.bank.repository.generic.MovementsRepository;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MovementsService {

    @Autowired
    MovementsRepository movementsRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(Movements.class);


    public Flux<Movements> getMovements(String idCustomer){
        return movementsRepository.getMovementsByCustomerAndStatusAndType(idCustomer,1, Constants.MOV_ACCOUNT);
    }
}
