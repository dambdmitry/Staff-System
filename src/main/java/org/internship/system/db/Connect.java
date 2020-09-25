package org.internship.system.db;

import org.internship.system.db.config.Config;
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
                    Config.getProperty(Config.DB_URL),
                    Config.getProperty(Config.DB_LOGIN),
                    Config.getProperty(Config.DB_PASSWORD));
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
