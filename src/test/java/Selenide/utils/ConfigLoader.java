package Selenide.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("config.properties not found");
            }
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new RuntimeException("Failed to load config.properties", exception);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}