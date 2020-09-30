package org.internship.system.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class MessagesTable implements MessagesTableManagement{
    @Autowired
    private Connect connection;

    private final static String INSERT_MESSAGE = "INSERT INTO messages(\n" +
            "\tlogin, user_action, action_object)\n" +
            "\tVALUES (?, ?, ?);";

    private Connection getConnection() throws SQLException {
        return connection.getConnection();
    }

    @PreDestroy
    public void closeConnection() {
        connection.closeConnection();
    }

    private void closeStatment(PreparedStatement stmt){
        try {
            if(stmt != null && !stmt.isClosed()){
                stmt.close();
            }
        } catch (SQLException throwables) {
        }
    }

    @Override
    public void addMessage(String login, String action, String object) {
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(INSERT_MESSAGE);

            statement.setString(1, login);
            statement.setString(2, action);
            statement.setString(3, object);

            statement.executeUpdate();

        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }
}
