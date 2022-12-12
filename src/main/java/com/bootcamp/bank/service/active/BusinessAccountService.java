package com.bootcamp.bank.service.active;

import ch.qos.logback.classic.Logger;
import com.bootcamp.bank.dto.*;
import com.bootcamp.bank.model.account.active.BusinessAccount;

import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.model.generic.Movements;

import com.bootcamp.bank.repository.account.active.BusinessAccountRepository;
import com.bootcamp.bank.repository.account.active.CreditCardAccountRepository;
import com.bootcamp.bank.repository.generic.MovementsRepository;
import com.bootcamp.bank.service.DatabaseSequenceService;
import com.bootcamp.bank.utils.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class BusinessAccountService {

    @Autowired
    BusinessAccountRepository businessAccountRepository;

    @Autowired
    DatabaseSequenceService databaseSequenceService;


    @Autowired
    MovementsRepository movementsRepository;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(BusinessAccountService.class);

    public Flux<BusinessAccount> list(){
        return businessAccountRepository.findAll();
    }

    public Flux<BusinessAccount> findByCustomer(String id){
        return businessAccountRepository.getBusinessAccountByIdCustomer(id);
    }

    public Mono<BusinessAccount> saveBusinessAccountforPersonal(BusinessAccountDTO businessAccountDTO){

        logger.info("SAVE : saveBusinessAccountforPersonal()");
        return databaseSequenceService.generateSequence(Constants.ACCOUNT_SEQUENCE).flatMap(sequence -> {
            BusinessAccount businessAccount = new BusinessAccount();
            businessAccount.setId(sequence);
            businessAccount.setIdCustomer(businessAccountDTO.getIdCustomer());
            businessAccount.setCode(businessAccountDTO.getCode());
            businessAccount.setAmount(businessAccountDTO.getAmount());
            businessAccount.setTypeCustomer(Constants.PERSONAL);
            businessAccount.setHolder(businessAccountDTO.getHolder());
            businessAccount.setTransaction(0);
            businessAccount.setSignatureAuthorized(businessAccount.getSignatureAuthorized());
            return businessAccountRepository.save(businessAccount).flatMap( businessAccount1 -> {
                Movements movement = new Movements();
                movement.setType(Constants.MOV_ACCOUNT);
                movement.setCreation(new Date());
                movement.setCustomer(businessAccountDTO.getIdCustomer());
                movement.setTable(businessAccount1.getId());
                movement.setStatus(1);
                return movementsRepository.save(movement).flatMap( movement1 -> Mono.just(businessAccount1));
            });
        });
    }

    public Mono<BusinessAccount> saveBusinessAccountforBusiness(BusinessAccountDTO businessAccountDTO){
        logger.info("SAVE : saveBusinessAccountforBusiness()");
        return databaseSequenceService.generateSequence(Constants.ACCOUNT_SEQUENCE).flatMap(sequence -> {
            BusinessAccount businessAccount = new BusinessAccount();
            businessAccount.setId(sequence);
            businessAccount.setCode(businessAccountDTO.getCode());
            businessAccount.setIdCustomer(businessAccountDTO.getIdCustomer());
            businessAccount.setAmount(businessAccountDTO.getAmount());
            businessAccount.setTypeCustomer(Constants.BUSINESS);
            businessAccount.setHolder(businessAccountDTO.getHolder());
            businessAccount.setTransaction(0);
            businessAccount.setSignatureAuthorized(businessAccount.getSignatureAuthorized());
            return businessAccountRepository.save(businessAccount).flatMap( businessAccount1 -> {
                Movements movement = new Movements();
                movement.setType(Constants.MOV_ACCOUNT);
                movement.setCreation(new Date());
                movement.setCustomer(businessAccountDTO.getIdCustomer());
                movement.setTable(businessAccount1.getId());
                movement.setStatus(1);
                return movementsRepository.save(movement).flatMap( movement1 -> Mono.just(businessAccount1));
            });

        });


    }

    public Mono<BusinessAccount> update(BusinessAccount businessAccount){
        return businessAccountRepository.getBusinessAccountById(businessAccount.getId()).flatMap( ba -> businessAccountRepository.save(businessAccount));
    }

    public Mono<Boolean> delete(Long id){
        return businessAccountRepository.getBusinessAccountById(id)
                .flatMap(ca -> businessAccountRepository.delete(ca)
                        .then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }



    public Mono<String> depositMoneyBusinessAccount(DepositMoneyDTO depositMoneyDTO){
        return businessAccountRepository.getBusinessAccountByCode(depositMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.BUSINESS_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        Integer transaction = ba.getTransaction();
                        Float newAmount = 0F;
                        if(transaction <= Constants.BUSINESS_MAX_TRANSACTION){
                            newAmount = depositMoneyDTO.getAmount() + currentAmount;
                            ba.setAmount(newAmount);
                            ba.setTransaction(transaction+1);
                        }else{
                            newAmount = depositMoneyDTO.getAmount() + currentAmount - Constants.COMMISION_TRANSACTION ;
                            ba.setAmount(newAmount);
                            ba.setTransaction(transaction+1);
                        }
                        if(newAmount < 0F){
                            return Mono.error(new IllegalArgumentException("There is not enough money in the account !"));
                        }
                        return this.update(ba).flatMap( businessAccount1  -> {
                                Movements movement = new Movements();
                                movement.setType(Constants.MOV_DEPOSIT_MONEY);
                                movement.setCreation(new Date());
                               // movement.setCustomer(depositMoneyDTO.getIdCustomer());
                                movement.setTable(businessAccount1.getId());
                                movement.setStatus(1);
                                movement.setDescription(depositMoneyDTO.getCodeAccount());
                                return movementsRepository.save(movement).flatMap( movement1 -> Mono.just("Money Update ..!! "));

                        });
                    }
                    return Mono.error(new IllegalArgumentException("It is not Business Account"));
                });
    }


    public Mono<String> withdrawMoneyBusinessAccount(WithDrawMoneyDTO withDrawMoneyDTO){
        return businessAccountRepository.getBusinessAccountByCode(withDrawMoneyDTO.getCodeAccount())
                .flatMap(ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.BUSINESS_ACCOUNT)){
                        Float currentAmount = ba.getAmount();
                        Integer transaction = ba.getTransaction();
                        Float newAmount = 0F;
                        if(transaction <= Constants.BUSINESS_MAX_TRANSACTION){
                            newAmount = currentAmount - withDrawMoneyDTO.getAmount() ;
                            ba.setAmount(newAmount);
                            ba.setTransaction(transaction+1);
                        }else{
                            newAmount = currentAmount - withDrawMoneyDTO.getAmount() - Constants.COMMISION_TRANSACTION;
                            ba.setAmount(newAmount);
                            ba.setTransaction(transaction+1);
                        }
                        if(newAmount < 0F){
                            return Mono.error(new IllegalArgumentException("There is not enough money in the account !"));
                        }
                        return this.update(ba).flatMap( businessAccount1  -> {
                            Movements movement = new Movements();
                            movement.setType(Constants.MOV_WITHDRAW_MONEY);
                            movement.setCreation(new Date());
                            // movement.setCustomer(depositMoneyDTO.getIdCustomer());
                            movement.setTable(businessAccount1.getId());
                            movement.setStatus(1);
                            movement.setDescription(withDrawMoneyDTO.getCodeAccount());
                            return movementsRepository.save(movement).flatMap( movement1 -> Mono.just("Money Update ..!! "));
                        });
                    }
                    return Mono.error(new IllegalArgumentException("It is not Business Account"));
                });
    }


    public Mono<String> setHolders(HolderDTO holderDTO){
        return businessAccountRepository.getBusinessAccountByCode(holderDTO.getCodeAccount())
                .flatMap( ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.BUSINESS_ACCOUNT)){
                        ba.setHolder(holderDTO.getHolders());
                        return this.update(ba).flatMap(ba1 -> Mono.just("Holders Update ..!! "));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Business Account"));
                });
    }

    public Mono<String> setsignatureAuthorized(SignatureAuthorizedDTO signatureAuthorizedDTO){
        return businessAccountRepository.getBusinessAccountByCode(signatureAuthorizedDTO.getCodeAccount())
                .flatMap( ba -> {
                    if(ba.getType().equalsIgnoreCase(Constants.BUSINESS_ACCOUNT)){
                        ba.setSignatureAuthorized(signatureAuthorizedDTO.getSignatures());
                        return this.update(ba).flatMap(ba1 -> Mono.just("Signature Authorized Update ..!! "));
                    }
                    return Mono.error(new IllegalArgumentException("It is not Business Account"));

                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found !! ")));
    }

    public Mono<String> getMoneyAvailable(String code_account){
        return businessAccountRepository.getBusinessAccountByCode(code_account)
                .flatMap( ba -> {
                    return Mono.just(ba.getAmount().toString());
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found !! ")));
    }



}
