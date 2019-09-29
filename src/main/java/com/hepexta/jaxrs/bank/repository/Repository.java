package com.hepexta.jaxrs.bank.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getList();
    T findById(String id);
    String insert(T model);
    boolean modify(T...model);
    boolean delete(String id);
}
