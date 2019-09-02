package com.hepexta.jaxrs.bank.repository.dao.mapper;

import com.hepexta.jaxrs.bank.model.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientMapper implements ResultSetMapper<Client> {

    @Override
    public Client map(ResultSet resultSet) throws SQLException {
        return Client.builder()
                .id(resultSet.getString("CLIENTID"))
                .name(resultSet.getString("NAME"))
                .build();

    }
}
