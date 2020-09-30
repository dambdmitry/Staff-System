package org.internship.system.db;

import org.internship.system.config.ApplicationConfig;
import org.internship.system.exceptions.DatabaseException;
import org.internship.system.exceptions.NotFoundWorkerException;
import org.internship.system.models.Worker;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Deprecated
public class DatabaseManagementImpl implements DatabaseManagement{

    private static final String INSERT_WORKER = "INSERT INTO worker(" +
            "worker_name, worker_patronymic, worker_lastname)" +
            "VALUES (?, ?, ?);";
    private static final String SELF_INSERT_WORKER = "INSERT INTO worker(" +
            "worker_id, worker_name, worker_patronymic, worker_lastname)" +
            "VALUES (?, ? , ? , ?);";
    private static final String GET_WORKER_BY_ID = "SELECT * FROM worker WHERE worker_id = ?;";
    private static final String GET_ALL_WORKERS = "SELECT * FROM worker;";
    private static final String REMOVE_WORKER_BY_ID = "DELETE FROM worker WHERE worker_id = ?;";

    private Connection connection = null;


    private Connection getConnection() throws SQLException{
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

    private void closeStatment(PreparedStatement stmt){
        try {
            if(stmt != null && !stmt.isClosed()){
                stmt.close();
            }
        } catch (SQLException throwables) {
        }
    }

    @Override
    public int addWorker(String lastName, String name, String patronymic) {
        int generatedId = -1;
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(INSERT_WORKER, new String[]{"worker_id"});

            statement.setString(1, name);
            statement.setString(2, patronymic);
            statement.setString(3, lastName);

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                generatedId = rs.getInt(1);
            }

        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
        return generatedId;
    }

    @Override
    public int addPureWorker(Worker worker) {
        int generatedId = -1;
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(INSERT_WORKER, new String[]{"worker_id"});

            statement.setString(1, worker.getFirstName());
            statement.setString(2, worker.getPatronymic());;
            statement.setString(3, worker.getLastName());

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                generatedId = rs.getInt(1);
            }

        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
        return generatedId;
    }

    @Override
    public void addWorkerYourself(int id, String lastName, String name, String patronymic) throws DatabaseException{
        if(hasId(id)){
            throw new DatabaseException("Работник с таким номером уже есть");
        }
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(SELF_INSERT_WORKER);

            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, patronymic);
            statement.setString(4,lastName);

            statement.executeUpdate();
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public Worker getWorker(int id) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(GET_WORKER_BY_ID);

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                String lastName = rs.getString("worker_lastname");
                String firstName = rs.getString("worker_name");
                String patronymic = rs.getString("worker_patronymic");

                Worker worker = new Worker(rs.getInt("worker_id"), lastName, firstName, patronymic);
                return worker;
            }else{
                throw new NotFoundWorkerException("Работника с таким номером нет");
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public Set<Worker> getAllWorkers() {
        Set<Worker> workers = new LinkedHashSet<>();
        PreparedStatement statement = null;
        try{
            Connection connection = getConnection();
            statement = connection.prepareStatement(GET_ALL_WORKERS);

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                String lastName = rs.getString("worker_lastname");
                String firstName = rs.getString("worker_name");
                String patronymic = rs.getString("worker_patronymic");

                Worker worker = new Worker(rs.getInt("worker_id"), lastName, firstName, patronymic);
                workers.add(worker);
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
        return workers;
    }

    @Override
    public void removeWorker(int id) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(REMOVE_WORKER_BY_ID);

            statement.setInt(1, id);
            int successfulRemove = statement.executeUpdate();
            if(successfulRemove == 0){
                throw new NotFoundWorkerException("Сотрудника с таким номером нет");
            }
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }

    @Override
    public boolean hasId(int id) {
        PreparedStatement statement = null;
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(GET_WORKER_BY_ID);

            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            closeConnection();
            throw new RuntimeException(throwables);
        } finally {
            closeStatment(statement);
        }
    }
}
