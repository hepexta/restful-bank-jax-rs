package com.hepexta.jaxrs.bank.repository.db;

public interface LockRepository {
    void lock(String...id);
    void unlock(String...id);
}
