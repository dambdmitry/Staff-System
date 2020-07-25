package StaffSystem;

import Exceptions.*;
import FileManagement.*;

import java.util.*;

public class Staff {
    private Map<Integer, Worker> staff = new HashMap<Integer, Worker>(); //Список всех сотрудников.
    private int freeId = 1; //Свободный номер.


    //Имеется ли такой номер id в базе.
    public boolean hasId(int id){
        return staff.containsKey(id);
    }


    //Генерация номера для нового сотрудника.
    private int getFreeId(){
        while(staff.containsKey(freeId)){
            freeId++;
        }
        return freeId;
    }


    //Добавить сотрудника в состав.
    public int add(String name){
        Worker worker = new Worker(getFreeId(), name);
        staff.put(worker.getId(), worker);
        return worker.getId();
    }


    //Удалить сотрудника с данным id.
    public void remove(int id) throws StaffException {
        if(staff.containsKey(id)){
            staff.remove(id);
        }else{
            throw new NotFoundWorkerException("Работника с таким номером нет");
        }
    }


    //Возвращает объект работник по его id.
    public Worker getWorker(int id){
        if(staff.containsKey(id)){
            return staff.get(id);
        }else{
            throw new NotFoundWorkerException("Работника с таким номером нет");
        }
    }


    //Возвращает всех работников.
    public Set<Worker> getAllWorker(){
        return new LinkedHashSet<Worker>(staff.values());
    }


    //Сохранить данные o сотрудниках в файл.
    public void save(String path, DataFile file) throws FileException {
        file.saveToFile(path, getAllWorker());
    }


    //Загрузка сотрудников из файла.
    public void load(String path, DataFile file) throws FileException {
        Set<Worker> allWorker = file.loadFormFile(path);
        for(Worker worker: allWorker){
            if(!staff.containsKey(worker.getId())){
                staff.put(worker.getId(), worker);
            }
        }
    }
}
