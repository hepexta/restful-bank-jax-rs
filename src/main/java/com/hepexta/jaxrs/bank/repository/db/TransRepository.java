package com.hepexta.jaxrs.bank.repository.db;

import java.util.List;

public interface TransRepository<T> {
    List<T> findByAccountId(String id);
    String insert(T model);
}
