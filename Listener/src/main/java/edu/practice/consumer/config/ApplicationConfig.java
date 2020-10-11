package edu.practice.consumer.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
    public static final String DB_URL = "db.url";
    public static final String DB_LOGIN = "db.login";
    public static final String DB_PASSWORD = "db.password";

    public static Properties properties = new Properties();

    private static final String applicationPropertiesPath = "application.properties";

    public static String getProperty(String nameProperty){
        if(properties.isEmpty()){
            try(InputStream inputStream = edu.practice.consumer.config.ApplicationConfig.class.getClassLoader().getResourceAsStream(applicationPropertiesPath)){
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(nameProperty);
    }

}
