package com.hepexta.jaxrs.bank.service;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class DemoServiceTest extends JerseyTest {

    private final static String DEMO_ENDPOINT = "/demo";
    private final static int STATUS_OK = 200;

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig().register(DemoService.class);
    }

    @Test
    public void givenNoClients_whenFetchAllClients_thenEmptyList() {
        Response response = target(DEMO_ENDPOINT)
                .request()
                .get();

        assertEquals(STATUS_OK, response.getStatus());
    }


}
