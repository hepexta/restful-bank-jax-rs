package com.hepexta.jaxrs.bank.repository.db;

import com.hepexta.jaxrs.bank.ex.ErrorMessage;
import com.hepexta.jaxrs.bank.ex.TransferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class AccountLockRepositoryDBImpl implements LockRepository {

    private static final Logger LOG = LoggerFactory.getLogger(AccountLockRepositoryDBImpl.class);
    private static AccountLockRepositoryDBImpl instance;
    private final Set<String> localCache = new CopyOnWriteArraySet<>();

    private AccountLockRepositoryDBImpl() {
    }

    public static AccountLockRepositoryDBImpl getInstance(){
        if (instance ==null){
            instance = new AccountLockRepositoryDBImpl();
        }
        return instance;
    }

    @Override
    public synchronized void lock(String...ids) {
        long timestamp = new Date().getTime();
        LOG.info("Account Lock started:{} {}", ids, timestamp);
        for (String id : ids) {
            TransferException.throwIf(putIfNotPresent(id), ErrorMessage.ERROR_533, id);
        }
        LOG.info("Account Lock finished:{} {}", ids, timestamp);
    }

    @Override
    public synchronized void unlock(String...ids) {
        LOG.info("LocalCache unlock {}", ids);
        for (String id : ids) {
            localCache.remove(id);
        }
        LOG.info("LocalCache {}", localCache);
    }

    private boolean putIfNotPresent(String id) {
        LOG.info("LocalCache {}", localCache);
        boolean isContains = localCache.contains(id);
        if (!isContains){
            localCache.add(id);
        }
        return isContains;
    }

}
