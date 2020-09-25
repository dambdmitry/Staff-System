package org.internship.system.db;

import org.internship.system.exceptions.NotFoundWorkerException;
import org.internship.system.models.Employee;
import org.internship.system.models.ModelFactory;
import org.internship.system.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UsersTable implements UsersTableManagement{
    @Autowired
    private Connect connection;

    private final static String SELECT_USER_BY_USERNAME = "SELECT username, user_password, user_role\n" +
            "\tFROM users WHERE username = ?;";

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
    public User getUserByUsername(String username) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELECT_USER_BY_USERNAME);

            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                //String login = rs.getString("last_name");
                String password = rs.getString("user_password");
                String role = rs.getString("user_role");

                User user = ModelFactory.createUser(username, password, role);
                return user;
            }else{
                throw new UsernameNotFoundException("Пользователя с таким именем нет");
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }
}
