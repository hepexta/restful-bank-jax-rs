package com.hepexta.jaxrs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Utils {

    private static final Properties properties = loadConfig();
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    public static Properties loadConfig() {

        Properties properties = new Properties();
        try (final InputStream fis = Utils.class.getResourceAsStream("/database.properties")){
            properties.load(fis);
        } catch (IOException e) {
            LOG.error("Error", e);
        }
        return properties;
    }

    public static String getStringProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            value = System.getProperty(key);
        }
        return value;
    }

}
