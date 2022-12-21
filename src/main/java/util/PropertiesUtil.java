package util;

import exception.DataBaseException;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtil {
    public static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil() {
    }

    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(new DataBaseException("Can't read application.properties file"));
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
