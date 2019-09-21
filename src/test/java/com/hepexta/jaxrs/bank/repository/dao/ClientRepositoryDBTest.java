package com.hepexta.jaxrs.bank.repository.dao;

import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.bank.repository.db.ClientRepositoryDBImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.hepexta.jaxrs.util.DBUtils.dataBaseInit;
import static com.hepexta.jaxrs.util.DBUtils.dataBasePopulate;

public class ClientRepositoryDBTest {

    private Repository<Client> clientRepository = ClientRepositoryDBImpl.getInstance();

    @BeforeClass
    public static void before(){
        dataBaseInit();
        dataBasePopulate();
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
