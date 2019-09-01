package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.Repository;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ClientServiceTest extends JerseyTest {

    private final static String GET_LIST_ENDPOINT = "/client/list";
    private final static String GET_BY_ID_ENDPOINT = "/client/findbyid/%s";
    private final static String INSERT_ENDPOINT = "/client/insert";
    private static final String DELETE_ENDPOINT = "/client/delete/%s";
    private static final String MODIFY_ENDPOINT = "/client/modify/%s";
    private final static int STATUS_OK = 200;

    private static final Repository<Client> clientRepository = mock(Repository.class);

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig().register(new ClientService(clientRepository));
    }

    @Test
    public void givenNoClients_whenFetchAllClients_thenEmptyList() {
        when(clientRepository.getList()).thenReturn(Collections.singletonList(Client.builder().build()));
        Response response = target(GET_LIST_ENDPOINT)
                .request()
                .get();

        List result = response.readEntity(List.class);
        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(1, result.size());
    }

    @Test
    public void givenClients_whenFetchById_thenClientReturned() {
        Client client = prepareClient();
        when(clientRepository.findById(any())).thenReturn(client);
        Response response = target(String.format(GET_BY_ID_ENDPOINT, client.getId()))
                .request()
                .get();

        Client result = response.readEntity(Client.class);
        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(client.getId(), result.getId());
        assertEquals(client.getName(), result.getName());
    }

    @Test
    public void givenNoClients_whenInsertClient_thenClientCreated() {
        Client client = prepareClient();
        when(clientRepository.findById(any())).thenReturn(client);
        Response response = target(INSERT_ENDPOINT)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(client, MediaType.APPLICATION_JSON));

        Client result = response.readEntity(Client.class);
        assertEquals(STATUS_OK, response.getStatus());
        assertEquals(client.getId(), result.getId());
        assertEquals(client.getName(), result.getName());
    }

    @Test
    public void givenClients_whenDeleteClient_thenClientDeleted() {
        String clientId = prepareClient().getId();
        when(clientRepository.delete(any())).thenReturn(true);
        Response response = target(String.format(DELETE_ENDPOINT, clientId))
                .request()
                .delete();

        assertEquals(STATUS_OK, response.getStatus());
    }

    @Test
    public void givenClients_whenModifyClient_thenClientModified() {
        String newName = "NEW NAME";
        Client client = prepareClient();
        when(clientRepository.modify(any())).thenReturn(true);
        when(clientRepository.findById(any())).thenReturn(client);

        Response response = target(String.format(MODIFY_ENDPOINT, client.getId()))
                .request()
                .put(Entity.entity(Client.builder().name(newName).build(), MediaType.APPLICATION_JSON));
        assertEquals(STATUS_OK, response.getStatus());
    }

    @Test
    public void givenNoClients_whenModifyClient_thenException() {
        when(clientRepository.findById(any())).thenReturn(null);
        Response response = target(String.format(MODIFY_ENDPOINT, "fakeId"))
                .request()
                .put(Entity.entity(Client.builder().build(), MediaType.APPLICATION_JSON));
        assertEquals(500, response.getStatus());
    }

    private Client prepareClient() {
        return Client.builder()
                .name("John Smith")
                .id("1")
                .build();
    }
}
