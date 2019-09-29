package com.hepexta.jaxrs.bank.repository.cache;

import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientRepositoryCache implements Repository<Client> {

    private AtomicInteger clientCounter = new AtomicInteger();

    private Map<String, Client> clients = new ConcurrentHashMap<>();

    private ClientRepositoryCache() {
    }

    private static ClientRepositoryCache instance;

    public static ClientRepositoryCache getInstance(){
        if (instance == null){
            instance = new ClientRepositoryCache();
        }
        return instance;
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
    public boolean modify(Client...clientsToModify) {
        boolean result = true;
        for (Client client : clientsToModify) {
            result &= clients.put(client.getId(), client) != null;
        }
        return result;
    }

    @Override
    public boolean delete(String id) {
        return clients.remove(id) != null;
    }

    public void clearCache(){
        clients.clear();
        clientCounter = new AtomicInteger();
    }
}
