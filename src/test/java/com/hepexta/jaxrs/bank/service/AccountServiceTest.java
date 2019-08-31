package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;
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
    private final static String INSERT_ENDPOINT = "/account/insert";
    private static final String DELETE_ENDPOINT = "/account/delete/%s";
    private static final String MODIFY_ENDPOINT = "/account/modify/%s";
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
