package com.habittracker.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private String url;
    private String username;
    private String password;
    private String driver;

    public DatabaseConfig(String configFilePath) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
            this.url = properties.getProperty("db.url");
            this.username = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");
            this.driver = properties.getProperty("db.driver");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}