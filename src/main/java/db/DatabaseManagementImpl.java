package db;

import db.config.Config;
import exceptions.DatabaseException;
import exceptions.NotFoundWorkerException;
import org.postgresql.Driver;
import staff.Worker;
import org.postgresql.Driver.*;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

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

    private void closeStatment(PreparedStatement stmt){
        try {
            if(stmt != null && !stmt.isClosed()){
                stmt.close();
            }
        } catch (SQLException throwables) {
        }
    }

    @Override
    public int addWorker(String name, String patronymic, String lastName) {
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
    public void addWorkerYourself(int id, String name, String patronymic, String lastName) throws DatabaseException{
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
        StringBuilder sbWorkerFullName = new StringBuilder();
        try {
            Connection connection = getConnection();
            statement = connection.prepareStatement(GET_WORKER_BY_ID);

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                sbWorkerFullName.append(rs.getString("worker_lastname")).append(" ")
                        .append(rs.getString("worker_name")).append(" ")
                        .append(rs.getString("worker_patronymic"));

                Worker worker = new Worker(rs.getInt("worker_id"), sbWorkerFullName.toString());
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
                StringBuilder sbWorkerFullName = new StringBuilder();

                sbWorkerFullName.append(rs.getString("worker_lastname")).append(" ")
                        .append(rs.getString("worker_name")).append(" ")
                        .append(rs.getString("worker_patronymic"));

                Worker worker = new Worker(rs.getInt("worker_id"), sbWorkerFullName.toString());
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
