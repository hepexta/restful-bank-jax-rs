package com.hepexta.jaxrs.bank.repository.cache;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountRepositoryCacheTest {

    private AccountRepositoryCache accountRepository = AccountRepositoryCache.getInstance();
    private static final Client CLIENT = Client.builder().name("John Smith").build();

    @Before
    public void setUp() {
        accountRepository.clear();
        accountRepository.insert(new Account(CLIENT, new BigDecimal(1000)));
        accountRepository.insert(new Account(CLIENT, new BigDecimal(2000)));
        accountRepository.insert(new Account(CLIENT, new BigDecimal(3000)));
    }

    @Test
    public void testCreate() {
        accountRepository.insert(new Account(CLIENT, new BigDecimal(5000)));
        Assert.assertEquals(4, accountRepository.getList().size());
    }

    @Test
    public void testDelete() {
        accountRepository.delete("1");
        Assert.assertEquals(2, accountRepository.getList().size());
    }

    @Test
    public void testModify() {
        BigDecimal newBalance = new BigDecimal(1000);
        Account account = Account.builder()
                .id("2")
                .number("2")
                .client(CLIENT)
                .balance(newBalance)
                .build();
        accountRepository.modify(account);
        Assert.assertEquals(account.getBalance(), accountRepository.findById(account.getId()).getBalance());
    }

}
