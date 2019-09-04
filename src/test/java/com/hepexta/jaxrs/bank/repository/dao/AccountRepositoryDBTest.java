package com.hepexta.jaxrs.bank.repository.dao;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.bank.repository.db.AccountRepositoryDBImpl;
import org.h2.tools.RunScript;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static com.hepexta.jaxrs.util.DBUtils.getConnection;

public class AccountRepositoryDBTest {

    private Repository<Account> accountRepository = AccountRepositoryDBImpl.getINSTANCE();
    private static final Client CLIENT = Client.builder().name("John Smith").id("1").build();

    @BeforeClass
    public static void before(){
        try (Connection conn = getConnection()) {
            RunScript.execute(conn, new InputStreamReader(AccountRepositoryDBTest.class.getResourceAsStream("/database.sql")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreate() {
        String accountId = accountRepository.insert(
                Account.builder()
                        .number("ACC01")
                        .client(CLIENT)
                        .balance(new BigDecimal(1000))
                        .build());
        Assert.assertEquals("6", accountId);
        Assert.assertEquals(6, accountRepository.getList().size());
    }

    @Test
    public void testFindById() {
        Account account = accountRepository.findById("2");
        Assert.assertEquals("2", account.getId());
    }

    @Test
    public void testDelete() {
        accountRepository.delete("1");
        Assert.assertEquals(5, accountRepository.getList().size());
    }

    @Test
    public void testModify() {
        BigDecimal newBalance = new BigDecimal(1000.0000);
        Account account = Account.builder()
                .id("2")
                .number("2")
                .client(CLIENT)
                .balance(newBalance)
                .build();
        accountRepository.modify(account);
        Assert.assertEquals(0, account.getBalance().compareTo(accountRepository.findById(account.getId()).getBalance()));
    }

}
