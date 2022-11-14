package com.bootcamp.bank.model.customer;

import com.bootcamp.bank.model.account.pasive.CheckingAccount;
import com.bootcamp.bank.model.account.pasive.FixedTermAccount;
import com.bootcamp.bank.model.account.pasive.SavingAccount;

import java.util.List;

public class Personal {
    private String dni;
    private String name;
    private CheckingAccount checkingAccount;
    private SavingAccount savingAccount;
    private List<FixedTermAccount> fixedTermAccount;
    private List<String> historyTransactionList;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CheckingAccount getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(CheckingAccount checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    public SavingAccount getSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(SavingAccount savingAccount) {
        this.savingAccount = savingAccount;
    }

    public List<FixedTermAccount> getFixedTermAccount() {
        return fixedTermAccount;
    }

    public void setFixedTermAccount(List<FixedTermAccount> fixedTermAccount) {
        this.fixedTermAccount = fixedTermAccount;
    }

    public List<String> getHistoryTransactionList() {
        return historyTransactionList;
    }

    public void setHistoryTransactionList(List<String> historyTransactionList) {
        this.historyTransactionList = historyTransactionList;
    }
}
