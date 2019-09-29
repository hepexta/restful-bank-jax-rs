package com.hepexta.jaxrs.bank.repository;

import com.hepexta.jaxrs.bank.model.Transfer;
import com.hepexta.jaxrs.bank.repository.db.AccountLockRepositoryDBImpl;
import com.hepexta.jaxrs.bank.repository.db.LockRepository;
import com.hepexta.jaxrs.bank.repository.db.TransRepository;
import com.hepexta.jaxrs.bank.repository.db.TransferRepositoryDBImpl;

public class TransferFactory {

    public static LockRepository getLockRepository() {
        return AccountLockRepositoryDBImpl.getInstance();
    }

    public static TransRepository<Transfer> getTransRepository() {
        return TransferRepositoryDBImpl.getInstance();
    }
}
