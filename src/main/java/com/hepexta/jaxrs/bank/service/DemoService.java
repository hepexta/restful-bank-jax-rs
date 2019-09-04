package com.hepexta.jaxrs.bank.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static com.hepexta.jaxrs.util.DBUtils.dataBasePopulate;

@Path("/dbinit")
public class DemoService {

    @GET
    @Produces("text/plain")
    public String getClichedMessage() {
        dataBasePopulate();
        return "Database Updated";
    }
}

