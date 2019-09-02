package com.hepexta.jaxrs.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Utils {

    private final static Properties properties = loadConfig();

    public static Properties loadConfig() {

        Properties properties = new Properties();
        try (final InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("database.properties");){
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
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
