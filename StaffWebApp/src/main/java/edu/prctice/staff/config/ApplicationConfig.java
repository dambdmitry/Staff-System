package edu.prctice.staff.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
    public static final String DB_URL = "db.url";
    public static final String DB_LOGIN = "db.login";
    public static final String DB_PASSWORD = "db.password";

    public static final String KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap-servers";
    public static final String KAFKA_TOPIC = "kafka.topic";
    public static final String KAFKA_GROUP_ID = "kafka.group-id";

    public static Properties properties = new Properties();

    private static final String applicationPropertiesPath = "application.properties";

    public static String getProperty(String nameProperty){
        if(properties.isEmpty()){
            try(InputStream inputStream = ApplicationConfig.class.getClassLoader().getResourceAsStream(applicationPropertiesPath)){
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(nameProperty);
    }

}
