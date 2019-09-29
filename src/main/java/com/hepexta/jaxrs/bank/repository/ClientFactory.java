package com.hepexta.jaxrs.bank.repository;

import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.cache.ClientRepositoryCache;
import com.hepexta.jaxrs.bank.repository.db.ClientRepositoryDBImpl;
import com.hepexta.jaxrs.util.AppConstants;

public class ClientFactory {

    public static Repository<Client> getClientRepository() {
        String env = System.getProperty("ENV");
        if(env.equals(AppConstants.DB)) {
            return ClientRepositoryDBImpl.getInstance();
        }
        return ClientRepositoryCache.getInstance();
    }
}
