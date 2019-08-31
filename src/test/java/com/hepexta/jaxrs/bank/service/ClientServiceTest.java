package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.cache.ClientRepositoryCache;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class ClientServiceTest extends JerseyTest {

    private final static String GET_LIST_ENDPOINT = "/client/list";
    private final static String GET_BY_ID_ENDPOINT = "/client/findbyid/%s";
    private final static String INSERT_ENDPOINT = "/client/insert";
    private static final String DELETE_ENDPOINT = "/client/delete/%s";
    private static final String MODIFY_ENDPOINT = "/client/modify/%s";
    private final static int STATUS_OK = 200;

    private ClientRepositoryCache clientRepository = ClientRepositoryCache.getINSTANCE();

    @Before
    public void init(){
        clientRepository.clearCache();
    }

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(ClientService.class);
    }

    @Test
    public void givenNoClients_whenFetchAllClients_thenEmptyList() {
        Response response = target(GET_LIST_ENDPOINT)
                .request()
                .get();

        assertEquals(STATUS_OK, response.getStatus());
        assertNotNull(response.getEntity().toString());
    }

    @Test
    public void givenClients_whenFetchById_thenClientReturned() {
        Client client = Client.builder().name("John Smith").build();
        String clientId = clientRepository.insert(client);
        Response response = target(String.format(GET_BY_ID_ENDPOINT, clientId))
                .request()
                .get();

        Client result = response.readEntity(Client.class);
        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(clientId, result.getId());
        assertEquals(client.getName(), result.getName());
    }

    @Test
    public void givenNoClients_whenInsertClient_thenClientCreated() {
        String expectedClientID = "1";
        Client client = Client.builder().name("TEST NAME").build();

        Response response = target(INSERT_ENDPOINT)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(client, MediaType.APPLICATION_JSON));

        Client result = response.readEntity(Client.class);
        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(expectedClientID, result.getId());
        assertEquals(client.getName(), result.getName());
    }

    @Test
    public void givenClients_whenDeleteClient_thenClientDeleted() {
        String clientId = clientRepository.insert(Client.builder().name("John Smith").build());

        Response response = target(String.format(DELETE_ENDPOINT, clientId))
                .request()
                .delete();

        assertEquals(STATUS_OK, response.getStatus());
        assertTrue(clientRepository.getList().isEmpty());
    }

    @Test
    public void givenClients_whenModifyClient_thenClientModified() {
        String newName = "NEW NAME";
        String oldName = "OLD NAME";
        String clientId = clientRepository.insert(Client.builder().name(oldName).build());

        Response response = target(String.format(MODIFY_ENDPOINT,clientId))
                .request()
                .put(Entity.entity(Client.builder().name(newName).build(), MediaType.APPLICATION_JSON));

        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(1, clientRepository.getList().size());
        assertEquals(newName, clientRepository.findById("1").getName());
    }

    @Test
    public void givenNoClients_whenModifyClient_thenException() {
        Response response = target(String.format(MODIFY_ENDPOINT,"fakeId"))
                .request()
                .put(Entity.entity(Client.builder().build(), MediaType.APPLICATION_JSON));
        assertEquals(500, response.getStatus());
    }
}
