package com.hepexta.jaxrs.bank.repository;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.repository.cache.AccountRepositoryCache;
import com.hepexta.jaxrs.bank.repository.db.AccountRepositoryDBImpl;
import com.hepexta.jaxrs.util.AppConstants;

public class AccountFactory {

    public static Repository<Account> getAccountRepository() {
        String env = System.getProperty("ENV");
        if(env.equals(AppConstants.DB)) {
            return AccountRepositoryDBImpl.getInstance();
        }
        return AccountRepositoryCache.getInstance();
    }
}
