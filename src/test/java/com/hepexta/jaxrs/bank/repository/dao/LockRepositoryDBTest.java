package com.hepexta.jaxrs.bank.repository.dao;

import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.repository.db.AccountLockRepositoryDBImpl;
import com.hepexta.jaxrs.bank.repository.db.LockRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.hepexta.jaxrs.util.DBUtils.dataBaseInit;
import static com.hepexta.jaxrs.util.DBUtils.dataBasePopulate;

public class LockRepositoryDBTest {

    private LockRepository lockRepository = AccountLockRepositoryDBImpl.getInstance();

    @BeforeClass
    public static void before(){
        dataBaseInit();
        dataBasePopulate();
    }

    @Test(expected = TransferException.class)
    public void testLock() {
        lockRepository.lock("2");
        lockRepository.lock("2");
    }

    @Test(expected = Test.None.class)
    public void testUnlock() {
        lockRepository.unlock("2");
    }

}
