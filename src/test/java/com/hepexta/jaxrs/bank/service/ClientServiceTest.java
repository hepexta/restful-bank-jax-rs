package com.hepexta.jaxrs.bank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.service.ClientService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ClientServiceTest extends JerseyTest {

    private final static String GET_LIST_ENDPOINT = "/client/list";
    private final static String INSERT_ENDPOINT = "/client/insert";
    @Mock
    private Repository<Client> repository;
    private ClientService service;

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        service = new ClientService();
        repository = Mockito.mock(Repository.class);
        Whitebox.setInternalState(service, "clientRepository", repository);
        return new ResourceConfig(service.getClass());
    }

    @Test
    public void givenNoClients_whenFetchAllClients_thanEmptyList() {
        Response response = target(GET_LIST_ENDPOINT).request().get();
        assertEquals("Status", 200, response.getStatus());
        assertNotNull("Should return empty list", response.getEntity().toString());
    }

    @Test
    public void givenNoClients_whenInsertClient_thanClientCreated() throws JsonProcessingException {
        String t = "1001";
        ObjectMapper mapper = new ObjectMapper();
        Mockito.when(repository.insert(Mockito.any(Client.class))).thenReturn(t);
        Client client = Client.builder().name("TEST NAME").build();
        Response response = target(INSERT_ENDPOINT).request().post(Entity.entity(mapper.writeValueAsString(client), MediaType.APPLICATION_JSON));
        System.out.println(response.getEntity());
        assertEquals("Status", 200, response.getStatus());

        assertEquals("Should return empty list", t, response.getEntity().toString());
    }
}
