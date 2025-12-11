package config;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(new FileInputStream("src/test/resources/test.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar test.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}