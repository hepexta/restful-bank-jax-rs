package com.hepexta.jaxrs.bank.repository.db;

import com.hepexta.jaxrs.bank.model.TransferStatus;

import java.util.List;

public interface TransRepository<T> {
    List<T> findByAccountId(String accountId);
    String insert(T model);
    void updateStatus(String id, TransferStatus status, String... params);
}
