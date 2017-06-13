package com.joshrm94.librarium.comun.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author joshmr94
 */

public class Helper {

    private Helper() {
    }
    
    public static String getProperty(String resourcePath, String key) {
        return loadProperties(resourcePath).getProperty(key);
    }

    protected static Properties loadProperties(String resourcePath) {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().
                getResourceAsStream(resourcePath);
        try {
            props.load(is);
        } catch (IOException e) {
            throw new ReadConfigurationException(e);
        }
        return props;
    }
    
    public static Properties getProperties(String resourcePath) {
        return loadProperties(resourcePath);
    }
    
}
