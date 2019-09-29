package com.hepexta.jaxrs.bank.util;

import com.hepexta.jaxrs.util.Utils;
import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
    @Test
    public void testConfig() {
        Utils.loadConfig();
        String stringProperty = Utils.getStringProperty("database.user");
        Assert.assertNotNull(stringProperty);
    }
}
