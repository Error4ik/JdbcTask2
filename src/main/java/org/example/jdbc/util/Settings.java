package org.example.jdbc.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {

    private static final Logger logger = LogManager.getLogger();
    private final Properties properties = new Properties();

    public Settings() {
        this.load();
    }

    private void load() {
        ClassLoader loader = this.getClass().getClassLoader();
        try (InputStream io = loader.getResourceAsStream("jdbc.properties")) {
            this.properties.load(io);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public String getValue(final String key) {
        return this.properties.getProperty(key);
    }
}
