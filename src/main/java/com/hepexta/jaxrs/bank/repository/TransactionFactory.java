package com.hepexta.jaxrs.bank.repository;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Transaction;
import com.hepexta.jaxrs.bank.repository.db.AccountLockRepositoryDBImpl;
import com.hepexta.jaxrs.bank.repository.db.LockRepository;
import com.hepexta.jaxrs.bank.repository.db.TransRepository;
import com.hepexta.jaxrs.bank.repository.db.TransactionRepositoryDBImpl;

public class TransactionFactory {

    public static LockRepository<Account> getLockRepository() {
        String env = System.getenv("ENV");
        if(env != null) {
           // return new ClientRepositoryDB();
        }
        return AccountLockRepositoryDBImpl.getINSTANCE();
    }

    public static TransRepository<Transaction> getTransRepository() {
        String env = System.getenv("ENV");
        if(env != null) {
            // return new ClientRepositoryDB();
        }
        return TransactionRepositoryDBImpl.getINSTANCE();
    }
}
