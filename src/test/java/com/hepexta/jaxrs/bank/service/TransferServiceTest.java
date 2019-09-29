package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.ex.ErrorMessage;
import com.hepexta.jaxrs.bank.ex.TransactionExceptionHandler;
import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.model.Transfer;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.bank.repository.db.LockRepository;
import com.hepexta.jaxrs.bank.repository.db.TransRepository;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TransferServiceTest extends JerseyTest {

    private final static String EXECUTE_ENDPOINT = "/transaction/execute";
    private final static String FIND_BY_ACCID_ENDPOINT = "/transaction/findbyaccountid/%s";

    private static final Repository<Account> accountRepository = mock(Repository.class);
    private static final LockRepository<Account> lockRepository = mock(LockRepository.class);
    private static final TransRepository<Transfer> transRepository = mock(TransRepository.class);

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig()
                .register(TransactionExceptionHandler.class)
                .register(new TransferService(accountRepository, lockRepository, transRepository));
    }

    @Test
    public void givenTransactions_whenFetchByAccount_thenReturned() {
        String id = "1";
        String sourceid = "SOURCEID";
        Transfer transfer = Transfer.builder()
                .id(id)
                .sourceAccountId(sourceid)
                .destAccountId("DESTID")
                .amount(BigDecimal.valueOf(1000))
                .operDate(LocalDate.now())
                .comment("COMMENT")
                .build();
        when(transRepository.findByAccountId(eq(sourceid))).thenReturn(Collections.singletonList(transfer));
        Response response = target(String.format(FIND_BY_ACCID_ENDPOINT, sourceid))
                .request()
                .get();

        assertEquals(OK.getStatusCode(), response.getStatus());
        verify(transRepository, times(1)).findByAccountId(any());
    }

    @Test
    public void givenNoTransactions_whenInsertTransaction_thenTransactionCreated() {
        Account sourceAccount = prepareAccount("SOURCEID");
        Account destAccount = prepareAccount("DESTID");
        Transfer transfer = Transfer.builder()
                .sourceAccountId(sourceAccount.getId())
                .destAccountId(destAccount.getId())
                .amount(BigDecimal.valueOf(1000))
                .operDate(LocalDate.now())
                .comment("COMMENT")
                .build();
        when(accountRepository.modify(any(Account.class))).thenReturn(true);
        when(transRepository.insert(any(Transfer.class))).thenReturn("1");
        when(lockRepository.findByIdAndLock(eq(sourceAccount.getId()))).thenReturn(sourceAccount);
        when(lockRepository.findByIdAndLock(eq(destAccount.getId()))).thenReturn(destAccount);

        Response response = target(EXECUTE_ENDPOINT)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON));

        assertEquals(OK.getStatusCode(), response.getStatus());
        verify(accountRepository, times(2)).modify(any());
        verify(lockRepository, times(2)).findByIdAndLock(any());
        verify(transRepository, times(1)).insert(any());
    }

    @Test
    public void givenTransactionWithNegativeAmount_whenExecuteTransaction_thenError() {
        Mockito.reset(accountRepository, lockRepository, transRepository);
        Transfer transfer = Transfer.builder()
                .sourceAccountId("FAKEID1")
                .destAccountId("FAKEID2")
                .amount(BigDecimal.valueOf(-1000))
                .operDate(LocalDate.now())
                .comment("COMMENT")
                .build();
        Response response = target(EXECUTE_ENDPOINT)
                .request()
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON));

        assertEquals(ErrorMessage.ERROR_521.getCode(), response.getStatus());
        assertEquals(ErrorMessage.ERROR_521.getMessage(), response.getStatusInfo().getReasonPhrase());
        assertEquals(ErrorMessage.ERROR_521.getMessage(), response.readEntity(String.class));
        verify(accountRepository, times(0)).modify(any());
        verify(lockRepository, times(0)).findByIdAndLock(any());
        verify(transRepository, times(0)).insert(any());
    }

    @Test
    public void givenTransactionOnSameAccount_whenExecuteTransaction_thenError() {
        Mockito.reset(accountRepository, lockRepository, transRepository);
        Transfer transfer = Transfer.builder()
                .sourceAccountId("FAKEID")
                .destAccountId("FAKEID")
                .amount(BigDecimal.valueOf(1000))
                .operDate(LocalDate.now())
                .comment("COMMENT")
                .build();
        Response response = target(EXECUTE_ENDPOINT)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON));

        assertEquals(ErrorMessage.ERROR_528.getCode(), response.getStatus());
        verify(accountRepository, times(0)).modify(any());
        verify(lockRepository, times(0)).findByIdAndLock(any());
        verify(transRepository, times(0)).insert(any());
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
