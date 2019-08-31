package com.hepexta.jaxrs.cfg;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfiguration extends ResourceConfig {

    public JerseyConfiguration() {
        packages("com.hepexta");
    }
}