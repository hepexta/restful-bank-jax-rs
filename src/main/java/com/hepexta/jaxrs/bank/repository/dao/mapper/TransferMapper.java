package com.hepexta.jaxrs.bank.repository.dao.mapper;

import com.hepexta.jaxrs.bank.model.Transfer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferMapper implements ResultSetMapper<Transfer>{
    @Override
    public Transfer map(ResultSet resultSet) throws SQLException {
        return Transfer.builder()
                .id(resultSet.getString("TRANSACTIONID"))
                .sourceAccountId(resultSet.getString("SRCACCOUNTID"))
                .destAccountId(resultSet.getString("DSTACCOUNTID"))
                .operDate(resultSet.getDate("OPERDATE").toLocalDate())
                .amount(resultSet.getBigDecimal("AMOUNT"))
                .comment(resultSet.getString("COMMENT"))
                .build();
    }

}
