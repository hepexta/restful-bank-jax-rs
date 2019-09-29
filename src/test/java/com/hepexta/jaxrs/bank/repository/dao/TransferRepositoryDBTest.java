package com.hepexta.jaxrs.bank.repository.dao;

import com.hepexta.jaxrs.bank.model.Transfer;
import com.hepexta.jaxrs.bank.repository.db.TransRepository;
import com.hepexta.jaxrs.bank.repository.db.TransferRepositoryDBImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.hepexta.jaxrs.util.DBUtils.dataBaseInit;
import static com.hepexta.jaxrs.util.DBUtils.dataBasePopulate;

public class TransferRepositoryDBTest {

    private TransRepository<Transfer> transRepository = TransferRepositoryDBImpl.getInstance();

    @BeforeClass
    public static void before() {
        dataBaseInit();
        dataBasePopulate();
    }

    @Test
    public void testCreate() {
        Transfer transfer = Transfer.builder()
                .sourceAccountId("SOURCEID")
                .destAccountId("DESTID")
                .amount(BigDecimal.valueOf(1000))
                .operDate(LocalDate.now())
                .comment("COMMENT")
                .build();
        String accountId = transRepository.insert(transfer);
        Assert.assertEquals("4", accountId);
    }

    @Test
    public void testFindByAccountId() {
        List<Transfer> transList = transRepository.findByAccountId("3");
        Assert.assertEquals(2, transList.size());
    }
}