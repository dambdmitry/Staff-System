package db.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    public static final String DB_URL = "db.url";
    public static final String DB_LOGIN = "db.login";
    public static final String DB_PASSWORD = "db.password";

    public static Properties properties = new Properties();

    private static final String dbPropertiesFilePath = "./db/db.properties";

    public static String getProperty(String nameProperty){
        if(properties.isEmpty()){
            try(InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(dbPropertiesFilePath)){
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(nameProperty);
    }
}
