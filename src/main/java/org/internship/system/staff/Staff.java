package org.internship.system.staff;

import org.internship.system.db.DatabaseManagement;
import org.internship.system.db.DatabaseManagementImpl;
import org.internship.system.exceptions.*;
import org.internship.system.files.DataFile;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.*;

@Component
public class Staff {
    private DatabaseManagement db = new DatabaseManagementImpl();

    //Имеется ли такой номер id в базе.
    public boolean hasId(int id) {
        return db.hasId(id);
    }

    @PreDestroy
    public void closeDatabase(){
        db.closeConnection();
    }

    //Добавить сотрудника в состав.
    public int add(String lastName, String name, String patronymic){
        return db.addWorker(lastName, name, patronymic);
    }

    public int addWorker(Worker worker){
        return db.addPureWorker(worker);
    }

    //Удалить сотрудника с данным id.
    public void remove(int id) throws StaffException {
        db.removeWorker(id);
    }


    //Возвращает объект работник по его id.
    public Worker getWorker(int id){
        if(db.hasId(id)){
            return db.getWorker(id);
        }else{
            throw new NotFoundWorkerException("Работника с таким номером нет");
        }
    }


    //Возвращает всех работников.
    public Set<Worker> getAllWorker(){
        return db.getAllWorkers();
    }


    //Сохранить данные o сотрудниках в файл.
    public void save(String path, DataFile file) throws FileException {
        file.saveToFile(path, getAllWorker());
    }


    //Загрузка сотрудников из файла.
    public void load(String path, DataFile file) throws FileException {
        Set<Worker> allWorker = file.loadFormFile(path);
        for(Worker worker: allWorker){
            if(!db.hasId(worker.getId())){
                try {
                    db.addWorkerYourself(worker.getId(),
                            worker.getLastName(),
                            worker.getFirstName(),
                            worker.getPatronymic());
                }catch (DatabaseException ex){
                    continue;
                    //TODO выдать какое-нибудь уведомление что работник не добавлен.
                }
            }
        }
    }
}
