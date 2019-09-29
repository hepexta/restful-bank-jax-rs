package com.hepexta.jaxrs.bank.repository.db;

public interface LockRepository<T> {
    void lock(String...id);
    void unlock(String...id);
}
