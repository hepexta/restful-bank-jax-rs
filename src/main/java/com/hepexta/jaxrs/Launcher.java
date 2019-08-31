package com.hepexta.jaxrs;

import com.hepexta.jaxrs.cfg.JerseyConfiguration;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

public class Launcher {

    private static final String JERSEY_SERVLET_NAME = "jersey-container-servlet";
    private static final String PORT = "8080";
    private static final String CONTEXT = "/*";

    public static void main(String[] args) throws Exception {
        new Launcher().start();
    }

    private void start() throws Exception {

        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = PORT;
        }

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
}