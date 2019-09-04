package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.model.Transaction;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.bank.repository.db.LockRepository;
import com.hepexta.jaxrs.bank.repository.db.TransRepository;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TransactionServiceTest extends JerseyTest {

    private final static String EXECUTE_ENDPOINT = "/transaction/execute";
    private final static String FIND_BY_ACCID_ENDPOINT = "/transaction/findbyaccountid/%s";
    private final static int STATUS_OK = 200;

    private static final Repository<Account> accountRepository = mock(Repository.class);
    private static final LockRepository<Account> lockRepository = mock(LockRepository.class);
    private static final TransRepository<Transaction> transRepository = mock(TransRepository.class);

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig().register(new TransactionService(accountRepository, lockRepository, transRepository));
    }

    @Test
    public void givenTransactions_whenFetchByAccount_thenReturned() {
        String id = "1";
        String sourceid = "SOURCEID";
        Transaction transaction = Transaction.builder()
                .id(id)
                .sourceAccountId(sourceid)
                .destAccountId("DESTID")
                .amount(BigDecimal.valueOf(1000))
                .operDate(LocalDate.now())
                .comment("COMMENT")
                .build();
        when(transRepository.findByAccountId(eq(sourceid))).thenReturn(Collections.singletonList(transaction));
        Response response = target(String.format(FIND_BY_ACCID_ENDPOINT, sourceid))
                .request()
                .get();

        assertEquals(STATUS_OK, response.getStatus());
        verify(transRepository, times(1)).findByAccountId(any());
    }

    @Test
    public void givenNoTransactions_whenInsertTransaction_thenTransactionCreated() {
        Account sourceAccount = prepareAccount("SOURCEID");
        Account destAccount = prepareAccount("DESTID");
        Transaction transaction = Transaction.builder()
                .sourceAccountId(sourceAccount.getId())
                .destAccountId(destAccount.getId())
                .amount(BigDecimal.valueOf(1000))
                .operDate(LocalDate.now())
                .comment("COMMENT")
                .build();
        when(accountRepository.modify(any(Account.class))).thenReturn(true);
        when(transRepository.insert(any(Transaction.class))).thenReturn("1");
        when(lockRepository.findByIdAndLock(eq(sourceAccount.getId()))).thenReturn(sourceAccount);
        when(lockRepository.findByIdAndLock(eq(destAccount.getId()))).thenReturn(destAccount);

        Response response = target(EXECUTE_ENDPOINT)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(transaction, MediaType.APPLICATION_JSON));

        assertEquals(STATUS_OK, response.getStatus());
        verify(accountRepository, times(2)).modify(any());
        verify(lockRepository, times(2)).findByIdAndLock(any());
        verify(transRepository, times(1)).insert(any());
    }

    private Account prepareAccount(String accId) {
        return Account.builder()
                .id(accId)
                .number(accId)
                .client(prepareClient())
                .balance(BigDecimal.valueOf(1000))
                .build();
    }

    private Client prepareClient() {
        return Client.builder()
                .id("1")
                .name("John Smith")
                .build();
    }


}
