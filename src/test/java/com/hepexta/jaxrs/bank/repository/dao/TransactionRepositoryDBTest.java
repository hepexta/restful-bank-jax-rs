package com.hepexta.jaxrs.bank.repository.dao;

import com.hepexta.jaxrs.bank.model.Transaction;
import com.hepexta.jaxrs.bank.repository.db.TransRepository;
import com.hepexta.jaxrs.bank.repository.db.TransactionRepositoryDBImpl;
import org.h2.tools.RunScript;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static com.hepexta.jaxrs.util.DBUtils.getConnection;

public class TransactionRepositoryDBTest {

    private TransRepository<Transaction> transRepository = TransactionRepositoryDBImpl.getINSTANCE();

    @BeforeClass
    public static void before() {
        try (Connection conn = getConnection()) {
            RunScript.execute(conn, new InputStreamReader(TransactionRepositoryDBTest.class.getResourceAsStream("/database.sql")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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