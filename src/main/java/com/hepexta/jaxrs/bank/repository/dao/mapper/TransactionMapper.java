package com.hepexta.jaxrs.bank.repository.dao.mapper;

import com.hepexta.jaxrs.bank.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionMapper implements ResultSetMapper<Transaction>{
    @Override
    public Transaction map(ResultSet resultSet) throws SQLException {
        return Transaction.builder()
                .id(resultSet.getString("TRANSACTIONID"))
                .sourceAccountId(resultSet.getString("SRCACCOUNTID"))
                .destAccountId(resultSet.getString("DSTACCOUNTID"))
                .operDate(resultSet.getDate("OPERDATE").toLocalDate())
                .amount(resultSet.getBigDecimal("AMOUNT"))
                .comment(resultSet.getString("COMMENT"))
                .build();
    }

}
