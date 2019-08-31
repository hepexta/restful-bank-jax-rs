package com.hepexta.jaxrs.bank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.repository.AccountFactory;
import com.hepexta.jaxrs.bank.repository.Repository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonFormat
public class Account {

    private static Repository<Account> accountRepository = AccountFactory.getAccountRepository();

    @JsonProperty
    private String id;
    @JsonProperty
    private String number;
    @JsonProperty(required = true)
    private Client client;
    @JsonProperty(required = true)
    private BigDecimal balance;

    public Account(Client client, BigDecimal balance) {
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

    private void mockDataBase() {
        accountRepository.modify(this);
    }

}
