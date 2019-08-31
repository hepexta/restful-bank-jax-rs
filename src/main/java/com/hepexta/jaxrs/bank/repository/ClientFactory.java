package com.hepexta.jaxrs.bank.repository;

import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.cache.ClientRepositoryCache;

public class ClientFactory {

    public static Repository<Client> getClientRepository() {
        String env = System.getenv("ENV");
        if(env != null) {
           // return new ClientRepositoryDB();
        }
        return ClientRepositoryCache.getINSTANCE();
    }
}
