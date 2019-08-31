package com.hepexta.jaxrs.bank.repository.cache;

import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientRepositoryCache implements Repository<Client> {

    private AtomicInteger clientCounter = new AtomicInteger();

    private Map<String, Client> clients = new HashMap<>();

    private ClientRepositoryCache() {
    }

    private static ClientRepositoryCache INSTANCE;

    public static ClientRepositoryCache getINSTANCE(){
        if (INSTANCE==null){
            INSTANCE = new ClientRepositoryCache();
        }
        return INSTANCE;
    }

    @Override
    public List<Client> getList() {
        return new ArrayList<>(clients.values());
    }

    @Override
    public Client findById(String id) {
        return clients.get(id);
    }

    @Override
    public String insert(Client client) {
        String id = String.valueOf(clientCounter.incrementAndGet());
        client.setId(id);
        clients.put(id, client);
        return id;
    }

    @Override
    public boolean modify(Client client) {
        return clients.put(client.getId(), client) != null;
    }

    @Override
    public boolean delete(String id) {
        return clients.remove(id) != null;
    }
}
