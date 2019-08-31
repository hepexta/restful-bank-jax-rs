package com.hepexta.jaxrs.bank.repository.cache;

import com.hepexta.jaxrs.bank.model.Client;
import org.junit.Assert;
import org.junit.Test;

public class ClientRepositoryCacheTest {

    private ClientRepositoryCache clientRepository = ClientRepositoryCache.getINSTANCE();

    @Test
    public void testCreate() {
        clientRepository.insert(Client.builder().name("John Smith 1").build());
        clientRepository.insert(Client.builder().name("John Smith 2").build());
        clientRepository.insert(Client.builder().name("John Smith 3").build());

        Assert.assertEquals(3, clientRepository.getList().size());
    }

    @Test
    public void testDelete() {
        clientRepository.delete("1");
        Assert.assertEquals(2, clientRepository.getList().size());
    }

    @Test
    public void testModify() {
        Client client = new Client("2", "Peter Parker New");
        clientRepository.modify(client);
        Assert.assertEquals(client.getName(), clientRepository.findById(client.getId()).getName());
    }

}
