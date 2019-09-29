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

    private static AccountRepositoryCache instance;

    public static AccountRepositoryCache getInstance(){
        if (instance ==null){
            instance = new AccountRepositoryCache();
        }
        return instance;
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
    public boolean modify(Account...accountsToModify) {
        boolean result = true;
        for (Account client : accountsToModify) {
            result &= accounts.put(client.getId(), client) != null;
        }
        return result;
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
