package com.hepexta.jaxrs.bank.repository.dao;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.repository.db.AccountLockRepositoryDBImpl;
import com.hepexta.jaxrs.bank.repository.db.LockRepository;
import org.h2.tools.RunScript;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import static com.hepexta.jaxrs.util.DBUtils.dataBaseInit;
import static com.hepexta.jaxrs.util.DBUtils.dataBasePopulate;
import static com.hepexta.jaxrs.util.DBUtils.getConnection;

public class LockRepositoryDBTest {

    private LockRepository<Account> lockRepository = AccountLockRepositoryDBImpl.getINSTANCE();

    @BeforeClass
    public static void before(){
        dataBaseInit();
        dataBasePopulate();
    }

    @Test
    public void testFindById() {
        Account account = lockRepository.findByIdAndLock("2");
        Assert.assertEquals("2", account.getId());
    }

}
