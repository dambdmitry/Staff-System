package staff;

import db.DatabaseManagement;
import db.DatabaseManagementImpl;
import exceptions.*;
import files.DataFile;

import java.sql.SQLException;
import java.util.*;

public class Staff {
    private DatabaseManagement db = new DatabaseManagementImpl();

    //Имеется ли такой номер id в базе.
    public boolean hasId(int id){
        return db.hasId(id);
    }


    public void closeDatabase() throws DatabaseException {
        try {
            db.closeConnection();
        } catch (SQLException throwables) {
            throw new DatabaseException("Ошибка закрытия подключения к базе данных");
        }
    }

    //Добавить сотрудника в состав.
    public int add(String name, String patronymic, String lastname){
        return db.addWorker(name, patronymic, lastname);
    }


    //Удалить сотрудника с данным id.
    public void remove(int id) throws StaffException {
        db.removeWorker(id);
//        if(staff.containsKey(id)){
//            staff.remove(id);
//        }else{
//            throw new NotFoundWorkerException("Работника с таким номером нет");
//        }
    }


    //Возвращает объект работник по его id.
    public Worker getWorker(int id){
        return db.getWorker(id);
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
                String[] fullname = worker.getName().split(" ");
                try {
                    db.addWorkerYourself(worker.getId(), fullname[1], fullname[2], fullname[0]);
                }catch (DatabaseException ex){
                    continue;
                    //TODO выдать какое-нибудь уведомление что работник не добавлен.
                }
            }
        }
    }
}
