package com.hepexta.jaxrs.bank.repository.db;

public interface LockRepository<T> {
    T findByIdAndLock(String id);
    void unlock(String id);
}
