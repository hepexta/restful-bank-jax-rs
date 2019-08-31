package com.hepexta.jaxrs.bank.model;

import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.repository.AccountFactory;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.bank.repository.cache.AccountRepositoryCache;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Account {

    private Repository<Account> accountRepository = AccountFactory.getAccountRepository();

    private String number;
    private Client client;
    private BigDecimal balance;

    public Account(Client client, BigDecimal balance) {
        this.client = client;
        this.balance = balance;
    }

    public Account(String number, Client client, BigDecimal balance) {
        this.number = number;
        this.client = client;
        this.balance = balance;
    }

    public synchronized void withdraw(BigDecimal bal) {
        if (balance.compareTo(bal)>=0) {
            mockDataBase();
            balance = balance.subtract(bal);
        } else {
            throw new TransferException(String.format(TransferException.NOT_ENOUGH_MONEY_ERROR_WITHDRAWAL, client.getName()));
        }
    }

    public synchronized void deposit(BigDecimal bal) {
        if (bal.compareTo(BigDecimal.ZERO)>0) {
            mockDataBase();
            balance = balance.add(bal);
        } else {
            throw new TransferException(String.format(TransferException.NOT_ENOUGH_MONEY_ERROR_DEPOSIT, client.getName()));
        }
    }

    public BigDecimal getBalance() {
        return balance;
    }

    private void mockDataBase() {
        accountRepository.modify(this);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return this.number;
    }
}
