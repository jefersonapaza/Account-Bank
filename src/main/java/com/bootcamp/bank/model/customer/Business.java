package com.bootcamp.bank.model.customer;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;

import java.util.List;

public class Business implements ICustomer{

    private String ruc;
    private String nameBusiness;
    private List<CheckingAccount> checkingAccountList;
    private List<String> holderList; /*Titulares*/
    private List<String> authorizedList; /*Firmantes Autorizados*/
    private List<String> historyTransactionList;

    @Override
    public void validateCustomer(String tipo) {
        if(!tipo.equals("EMPRESARIAL")){

        }
    }
}
