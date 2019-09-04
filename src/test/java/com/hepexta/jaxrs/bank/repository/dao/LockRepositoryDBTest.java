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

import static com.hepexta.jaxrs.util.DBUtils.getConnection;

public class LockRepositoryDBTest {

    private LockRepository<Account> lockRepository = AccountLockRepositoryDBImpl.getINSTANCE();

    @BeforeClass
    public static void before(){
        try (Connection conn = getConnection()) {
            RunScript.execute(conn, new InputStreamReader(LockRepositoryDBTest.class.getResourceAsStream("/database.sql")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindById() {
        Account account = lockRepository.findByIdAndLock("2");
        Assert.assertEquals("2", account.getId());
    }

}
