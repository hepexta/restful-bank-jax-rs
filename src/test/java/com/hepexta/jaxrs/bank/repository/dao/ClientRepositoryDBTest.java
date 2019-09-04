package com.hepexta.jaxrs.bank.repository.dao;

import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.bank.repository.db.ClientRepositoryDBImpl;
import org.h2.tools.RunScript;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static com.hepexta.jaxrs.util.DBUtils.getConnection;

public class ClientRepositoryDBTest {

    private Repository<Client> clientRepository = ClientRepositoryDBImpl.getINSTANCE();

    @BeforeClass
    public static void before(){
        try (Connection conn = getConnection()) {
            RunScript.execute(conn, new InputStreamReader(ClientRepositoryDBTest.class.getResourceAsStream("/database.sql")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreate() {
        String clientId = clientRepository.insert(
                Client.builder()
                        .name("Peter Pan")
                        .build());
        Assert.assertEquals("5", clientId);
        Assert.assertEquals(5, clientRepository.getList().size());
    }

    @Test
    public void testFindById() {
        Client client = clientRepository.findById("2");
        Assert.assertEquals("2", client.getId());
    }

    @Test
    public void testDelete() {
        clientRepository.delete("1");
        Assert.assertEquals(4, clientRepository.getList().size());
    }

    @Test
    public void testModify() {
        String newName = "Tony Stark";
        Client client = Client.builder()
                .id("2")
                .name(newName)
                .build();
        clientRepository.modify(client);
        Assert.assertEquals(client.getName(), clientRepository.findById(client.getId()).getName());
    }

}
