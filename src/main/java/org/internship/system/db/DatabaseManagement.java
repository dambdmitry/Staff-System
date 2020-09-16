package org.internship.system.db;

import org.internship.system.exceptions.DatabaseException;
import org.internship.system.models.Worker;

import java.util.Set;

@Deprecated
public interface DatabaseManagement {
    int addWorker(String name, String patronymic, String lastName);
    int addPureWorker(Worker worker);
    void addWorkerYourself(int id, String name, String patronymic, String lastName) throws DatabaseException;
    Worker getWorker(int id);
    Set<Worker> getAllWorkers();
    void removeWorker(int id);
    boolean hasId(int id);
    void closeConnection();

}
