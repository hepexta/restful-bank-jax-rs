package com.hepexta.jaxrs;

import com.hepexta.jaxrs.cfg.JerseyConfiguration;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;
import org.h2.tools.RunScript;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import static com.hepexta.jaxrs.util.AppConstants.CONTEXT;
import static com.hepexta.jaxrs.util.AppConstants.DB;
import static com.hepexta.jaxrs.util.AppConstants.JERSEY_SERVLET_NAME;
import static com.hepexta.jaxrs.util.AppConstants.PORT;
import static com.hepexta.jaxrs.util.DBUtils.dataBaseInit;
import static com.hepexta.jaxrs.util.DBUtils.getConnection;

public class Launcher {


    public static void main(String[] args) throws Exception {
        new Launcher().start();
    }

    private void start() throws Exception {

        String port = getPort();
        setenv();
        prepareDatabase();

        String contextPath = "";
        String appBase = ".";

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.valueOf(port));
        tomcat.getHost().setAppBase(appBase);

        Context context = tomcat.addContext(contextPath, appBase);
        Tomcat.addServlet(context, JERSEY_SERVLET_NAME, new ServletContainer(new JerseyConfiguration()));
        context.addServletMappingDecoded(CONTEXT, JERSEY_SERVLET_NAME);
        tomcat.start();
        tomcat.getServer().await();
    }

    private String getPort() {
        String port = System.getProperty("PORT");
        if (port == null || port.isEmpty()) {
            port = PORT;
        }
        return port;
    }

    private void setenv() {
        String env = System.getProperty("ENV");
        if (env == null || env.isEmpty()) {
            System.setProperty("ENV", DB);
        }
    }

    private void prepareDatabase() {
        String env = System.getProperty("ENV");
        if (env.equals(DB)) {
            dataBaseInit();
        }
    }
}