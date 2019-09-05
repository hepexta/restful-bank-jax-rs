package com.hepexta.jaxrs.bank.repository.dao.mapper;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountLockMapper implements ResultSetMapper<Account>{
    @Override
    public Account map(ResultSet resultSet) throws SQLException {
        return Account.builder()
                .id(resultSet.getString("ACCOUNTID"))
                .number(resultSet.getString("NUMBER"))
                .client(Client.builder()
                        .id(resultSet.getString("CLIENTID"))
                        .build())
                .balance(resultSet.getBigDecimal("BALANCE"))
                .build();
    }
}
