package com.hepexta.jaxrs.bank.repository;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.repository.cache.AccountRepositoryCache;

public class AccountFactory {

    public static Repository<Account> getAccountRepository() {
        String env = System.getenv("ENV");
        if(env != null) {
            // return new AccountRepositoryDB();
        }
        return AccountRepositoryCache.getINSTANCE();
    }
}
