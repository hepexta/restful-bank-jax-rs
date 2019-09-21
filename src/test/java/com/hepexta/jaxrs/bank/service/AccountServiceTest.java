package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.ex.ErrorMessage;
import com.hepexta.jaxrs.bank.ex.TransactionExceptionHandler;
import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.model.OperationAmount;
import com.hepexta.jaxrs.bank.repository.Repository;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AccountServiceTest extends JerseyTest {

    private final static String GET_LIST_ENDPOINT = "/account/list";
    private final static String GET_BY_ID_ENDPOINT = "/account/findbyid/%s";
    private final static String INSERT_ENDPOINT = "/account/insert";
    private static final String DELETE_ENDPOINT = "/account/delete/%s";
    private static final String DEPOSIT_ENDPOINT = "/account/deposit";
    private static final String WITHDRAWAL_ENDPOINT = "/account/withdrawal";
    private final static int STATUS_OK = 200;

    private static final Repository<Account> accountRepository = mock(Repository.class);
    private static final Repository<Client> clientRepository = mock(Repository.class);

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig()
                .register(TransactionExceptionHandler.class)
                .register(new AccountService(accountRepository, clientRepository));
    }

    @Test
    public void givenNoAccounts_whenFetchAllAccounts_thenEmptyList() {
        when(accountRepository.getList()).thenReturn(Collections.singletonList(prepareAccount()));
        Response response = target(GET_LIST_ENDPOINT)
                .request()
                .get();

        List result = response.readEntity(List.class);
        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(1, result.size());
    }

    @Test
    public void givenAccounts_whenFetchById_thenAccountReturned() {
        Account account = prepareAccount();
        when(accountRepository.findById(any())).thenReturn(account);
        Response response = target(String.format(GET_BY_ID_ENDPOINT, account.getId()))
                .request()
                .get();

        Account result = response.readEntity(Account.class);
        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(account.getId(), result.getId());
        assertEquals(account.getNumber(), result.getNumber());
        assertEquals(account.getBalance(), result.getBalance());
    }

    @Test
    public void givenNoAccounts_whenInsertAccount_thenAccountCreated() {
        Account account = prepareAccount();
        when(clientRepository.findById(eq(account.getClient().getId()))).thenReturn(account.getClient());
        when(accountRepository.insert(any(Account.class))).thenReturn(account.getId());
        when(accountRepository.findById(eq(account.getId()))).thenReturn(account);

        Response response = target(INSERT_ENDPOINT)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON));

        Account result = response.readEntity(Account.class);
        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(account.getNumber(), result.getNumber());
        assertEquals(account.getBalance(), result.getBalance());
    }

    @Test
    public void givenAccounts_whenDeleteAccount_thenAccountDeleted() {
        Account account = prepareAccount();
        when(accountRepository.findById(eq(account.getId()))).thenReturn(account);
        when(accountRepository.delete(eq(account.getId()))).thenReturn(true);

        Response response = target(String.format(DELETE_ENDPOINT, account.getId()))
                .request()
                .delete();

        assertEquals(STATUS_OK, response.getStatus());
    }

    @Test
    public void givenNoAccounts_whenDeleteAccount_thenException() {
        Account account = prepareAccount();
        when(accountRepository.findById(eq(account.getId()))).thenReturn(null);
        Response response = target(String.format(DELETE_ENDPOINT, account.getId()))
                .request()
                .delete();

        assertEquals(ErrorMessage.ERROR_520.getCode(), response.getStatus());
    }

    @Test
    public void givenNoAccounts_whenInsertAccountWithInvalidClient_thenException() {
        Account account = prepareAccount();
        when(clientRepository.findById(eq(account.getClient().getId()))).thenReturn(null);
        Response response = target(INSERT_ENDPOINT)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON));

        assertEquals(ErrorMessage.ERROR_522.getCode(), response.getStatus());
    }

    @Test
    public void givenAccounts_whenDeposit_thenBalanceIncreased() {
        Account account = prepareAccount();
        when(clientRepository.findById(eq(account.getClient().getId()))).thenReturn(account.getClient());
        when(accountRepository.findById(eq(account.getId()))).thenReturn(account);
        when(accountRepository.modify(any())).thenReturn(true);
        BigDecimal balance = account.getBalance();
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        OperationAmount operationAmount = OperationAmount.builder()
                .id(account.getId())
                .amount(depositAmount)
                .build();
        Response response = target(DEPOSIT_ENDPOINT)
                .request()
                .post(Entity.entity(operationAmount, MediaType.APPLICATION_JSON));
        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(balance.add(depositAmount), account.getBalance());
        verify(accountRepository, atLeastOnce()).modify(any());
    }

    @Test
    public void givenAccounts_whenWithdrawal_thenBalanceDecreased() {
        Account account = prepareAccount();
        when(clientRepository.findById(eq(account.getClient().getId()))).thenReturn(account.getClient());
        when(accountRepository.findById(eq(account.getId()))).thenReturn(account);
        when(accountRepository.modify(any())).thenReturn(true);
        BigDecimal balance = account.getBalance();
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        OperationAmount operationAmount = OperationAmount.builder()
                .id(account.getId())
                .amount(depositAmount)
                .build();
        Response response = target(WITHDRAWAL_ENDPOINT)
                .request()
                .post(Entity.entity(operationAmount, MediaType.APPLICATION_JSON));
        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(balance.subtract(depositAmount), account.getBalance());
        verify(accountRepository, atLeastOnce()).modify(any());
    }

    private Client prepareClient() {
        return Client.builder()
                .id("1")
                .name("John Smith")
                .build();
    }

    private Account prepareAccount() {
        BigDecimal balance = new BigDecimal(1000);
        return Account.builder()
                .id("1")
                .number("ACC001")
                .client(prepareClient())
                .balance(balance)
                .build();
    }
}
