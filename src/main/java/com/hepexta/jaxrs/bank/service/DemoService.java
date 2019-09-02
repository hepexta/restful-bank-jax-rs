package com.hepexta.jaxrs.bank.service;

import org.h2.tools.RunScript;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import static com.hepexta.jaxrs.util.DBUtils.getConnection;

@Path("/demo")
public class DemoService {

    @GET
    @Produces("text/plain")
    public String getClichedMessage() {
        try (Connection conn = getConnection()) {
            RunScript.execute(conn, new InputStreamReader(getClass().getResourceAsStream("/database.sql")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Database Updated";
    }
}

