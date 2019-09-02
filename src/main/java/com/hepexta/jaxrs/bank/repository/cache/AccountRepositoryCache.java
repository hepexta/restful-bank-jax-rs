package com.hepexta.jaxrs.bank.repository.cache;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountRepositoryCache implements Repository<Account> {

    private AtomicInteger accountCounter = new AtomicInteger();

    private Map<String, Account> accounts = new ConcurrentHashMap<>();

    private AccountRepositoryCache() {
    }

    private static AccountRepositoryCache INSTANCE;

    public static AccountRepositoryCache getINSTANCE(){
        if (INSTANCE==null){
            INSTANCE = new AccountRepositoryCache();
        }
        return INSTANCE;
    }

    @Override
    public List<Account> getList() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public Account findById(String id) {
        return accounts.get(id);
    }

    @Override
    public String insert(Account model) {
        String id = String.valueOf(accountCounter.incrementAndGet());
        model.setId(id);
        accounts.put(id, model);
        return id;
    }

    @Override
    public boolean modify(Account model) {
        return accounts.put(model.getId(), model) != null;
    }

    @Override
    public boolean delete(String id) {
        return accounts.remove(id) != null;
    }

    public void clear(){
        accounts = new ConcurrentHashMap<>();
        accountCounter = new AtomicInteger();
    }
}
