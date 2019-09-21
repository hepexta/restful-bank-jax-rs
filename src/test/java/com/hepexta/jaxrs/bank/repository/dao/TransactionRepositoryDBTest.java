package com.hepexta.jaxrs.bank.repository.dao;

import com.hepexta.jaxrs.bank.model.Transaction;
import com.hepexta.jaxrs.bank.repository.db.TransRepository;
import com.hepexta.jaxrs.bank.repository.db.TransactionRepositoryDBImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.hepexta.jaxrs.util.DBUtils.dataBaseInit;
import static com.hepexta.jaxrs.util.DBUtils.dataBasePopulate;

public class TransactionRepositoryDBTest {

    private TransRepository<Transaction> transRepository = TransactionRepositoryDBImpl.getInstance();

    @BeforeClass
    public static void before() {
        dataBaseInit();
        dataBasePopulate();
    }

    @Test
    public void testCreate() {
        Transaction transaction = Transaction.builder()
                .sourceAccountId("SOURCEID")
                .destAccountId("DESTID")
                .amount(BigDecimal.valueOf(1000))
                .operDate(LocalDate.now())
                .comment("COMMENT")
                .build();
        String accountId = transRepository.insert(transaction);
        Assert.assertEquals("4", accountId);
    }

    @Test
    public void testFindByAccountId() {
        List<Transaction> transList = transRepository.findByAccountId("3");
        Assert.assertEquals(2, transList.size());
    }
}