package edu.prctice.staff.db;


import edu.prctice.staff.config.ApplicationConfig;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
* По умолчанию компонент будет Singleton/.
* */
@Component
public class Connect {
    private Connection connection = null;

    public Connection getConnection() throws SQLException {
        if(connection == null) {
            connection = DriverManager.getConnection(
                    ApplicationConfig.getProperty(ApplicationConfig.DB_URL),
                    ApplicationConfig.getProperty(ApplicationConfig.DB_LOGIN),
                    ApplicationConfig.getProperty(ApplicationConfig.DB_PASSWORD));
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
