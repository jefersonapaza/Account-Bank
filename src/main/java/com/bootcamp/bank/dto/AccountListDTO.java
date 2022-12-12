package com.bootcamp.bank.dto;

import com.bootcamp.bank.model.account.active.BusinessAccount;
import com.bootcamp.bank.model.account.active.CreditCardAccount;
import com.bootcamp.bank.model.account.active.PersonalAccount;
import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.model.account.pasive.SavingAccount;
import lombok.Data;
import java.util.List;

@Data
public class AccountListDTO {

    private List<BusinessAccount> businessAccountList;
    private List<CreditCardAccount> creditCardAccountList;
    private List<PersonalAccount> personalAccountList;

    private List<CheckingAccount> checkingAccountList;
    private List<FixedTermAccount> fixedTermAccountList;
    private List<SavingAccount> savingAccountList;

}
