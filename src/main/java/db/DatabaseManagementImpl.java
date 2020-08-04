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


    private Connection getConnection() throws SQLException{
        Connection connection = DriverManager.getConnection(
                Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN),
                Config.getProperty(Config.DB_PASSWORD)
        );
        return connection;
    }

    @Override
    public int addWorker(String name, String patronymic, String lastName) {
        int generatedId = -1;
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_WORKER, new String[]{"worker_id"})) {

            statement.setString(1, name);
            statement.setString(2, patronymic);
            statement.setString(3, lastName);

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                generatedId = rs.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return generatedId;
    }

    @Override
    public void addWorkerYourself(int id, String name, String patronymic, String lastName) throws DatabaseException {
        if(hasId(id)){
            throw new DatabaseException("Работник с таким номером уже есть");
        }
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SELF_INSERT_WORKER)) {

            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, patronymic);
            statement.setString(4,lastName);

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    @Override
    public Worker getWorker(int id) {
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_WORKER_BY_ID)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Worker worker = new Worker(rs.getInt("worker_id"),
                          rs.getString("worker_lastname") + " " +
                                rs.getString("worker_name") + " " +
                                rs.getString("worker_patronymic"));
                return worker;
            }else{
                throw new NotFoundWorkerException("Работника с таким номером нет");
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    @Override
    public Set<Worker> getAllWorkers() {
        Set<Worker> workers = new LinkedHashSet<>();
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_WORKERS)) {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Worker worker = new Worker(rs.getInt("worker_id"),
                          rs.getString("worker_lastname") + " " +
                                rs.getString("worker_name") + " " +
                                rs.getString("worker_patronymic"));
                workers.add(worker);
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
        return workers;
    }

    @Override
    public void removeWorker(int id) {
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(REMOVE_WORKER_BY_ID)) {

            statement.setInt(1, id);
            int successfulRemove = statement.executeUpdate();
            if(successfulRemove == 0){
                throw new NotFoundWorkerException("Сотрудника с таким номером нет");
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    @Override
    public boolean hasId(int id) {
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_WORKER_BY_ID)) {

            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
