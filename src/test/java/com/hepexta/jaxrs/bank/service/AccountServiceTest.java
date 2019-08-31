package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.model.OperationAmount;
import com.hepexta.jaxrs.bank.repository.ClientFactory;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.bank.repository.cache.AccountRepositoryCache;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class AccountServiceTest extends JerseyTest {

    private final static String GET_LIST_ENDPOINT = "/account/list";
    private final static String GET_BY_ID_ENDPOINT = "/account/findbyid/%s";
    private final static String INSERT_ENDPOINT = "/account/insert";
    private static final String DELETE_ENDPOINT = "/account/delete/%s";
    private static final String DEPOSIT_ENDPOINT = "/account/deposit";
    private static final String WITHDRAWAL_ENDPOINT = "/account/withdrawal";
    private final static int STATUS_OK = 200;

    private AccountRepositoryCache accountRepository = AccountRepositoryCache.getINSTANCE();
    private Repository<Client> clientRepository = ClientFactory.getClientRepository();

    @Before
    public void init(){
        accountRepository.clearCache();
    }

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(AccountService.class);
    }

    @Test
    public void givenNoAccounts_whenFetchAllAccounts_thenEmptyList() {
        Response response = target(GET_LIST_ENDPOINT)
                .request()
                .get();

        assertEquals(STATUS_OK, response.getStatus());
        assertNotNull(response.getEntity().toString());
    }

    @Test
    public void givenAccounts_whenFetchById_thenAccountReturned() {
        clientRepository.insert(prepareClient());
        Account account = prepareAccount();
        String accountId = accountRepository.insert(account);
        Response response = target(String.format(GET_BY_ID_ENDPOINT, accountId))
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
        String clientId = clientRepository.insert(prepareClient());
        Account account = prepareAccount();
        account.getClient().setId(clientId);

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
        clientRepository.insert(prepareClient());
        String accountId = accountRepository.insert(prepareAccount());

        Response response = target(String.format(DELETE_ENDPOINT,accountId))
                .request()
                .delete();

        assertEquals(STATUS_OK, response.getStatus());
        assertTrue(accountRepository.getList().isEmpty());
    }

    @Test
    public void givenAccounts_whenDeposit_thenBalanceIncreased() {
        clientRepository.insert(prepareClient());
        Account account = prepareAccount();
        String accountId = accountRepository.insert(account);
        BigDecimal balance = account.getBalance();
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        OperationAmount operationAmount = OperationAmount.builder()
                .id(accountId)
                .amount(depositAmount)
                .build();
        Response response = target(WITHDRAWAL_ENDPOINT)
                .request()
                .post(Entity.entity(operationAmount, MediaType.APPLICATION_JSON));
        assertEquals(STATUS_OK, response.getStatus());
        Account modifiedAccount = accountRepository.findById(accountId);
        assertEquals(balance.subtract(depositAmount), modifiedAccount.getBalance());
    }

    @Test
    public void givenAccounts_whenWithdrawal_thenBalanceDecreased() {
        clientRepository.insert(prepareClient());
        Account account = prepareAccount();
        String accountId = accountRepository.insert(account);
        BigDecimal balance = account.getBalance();
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        OperationAmount operationAmount = OperationAmount.builder()
                .id(accountId)
                .amount(depositAmount)
                .build();
        Response response = target(DEPOSIT_ENDPOINT)
                .request()
                .post(Entity.entity(operationAmount, MediaType.APPLICATION_JSON));
        assertEquals(STATUS_OK, response.getStatus());
        Account modifiedAccount = accountRepository.findById(accountId);
        assertEquals(balance.add(depositAmount), modifiedAccount.getBalance());
    }

    @Test
    public void givenNoAccounts_whenInsertAccountWithInvalidClient_thenException() {
        Account account = prepareAccount();

        Response response = target(INSERT_ENDPOINT)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON));

        assertEquals(500, response.getStatus());
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
                .number("ACC001")
                .client(prepareClient())
                .balance(balance)
                .build();
    }
}
