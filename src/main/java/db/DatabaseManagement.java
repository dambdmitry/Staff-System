package db;

import exceptions.DatabaseException;
import staff.Worker;

import java.sql.SQLException;
import java.util.Set;

public interface DatabaseManagement {
    int addWorker(String name, String patronymic, String lastName);
    void addWorkerYourself(int id, String name, String patronymic, String lastName) throws DatabaseException;
    Worker getWorker(int id);
    Set<Worker> getAllWorkers();
    void removeWorker(int id);
    boolean hasId(int id);
    void closeConnection() throws SQLException;

}
