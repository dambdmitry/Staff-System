package staff;

import exceptions.FileException;
import files.DataFile;
import files.JsonDataFile;
import files.TxtDataFile;
import files.XmlDataFile;

import java.io.*;
import java.util.Scanner;
import java.util.Set;

public class ConsoleApp {
    private Staff staff = new Staff();
    private InputStream inputStream = System.in;
    private PrintStream outputStream = System.out;
    private static final int REQUIRED_NUMBER_OF_ARGS_TO_ADD = 3;
    private static final int REQUIRED_NUMBER_OF_ARGS_TO_REMOVE = 1;


    public void setInputStream(InputStream inputStream){
        this.inputStream = inputStream;
    }
    
    
    public void setOutputStream(PrintStream outputStream){
        this.outputStream = outputStream;
    }


    public void consoleRun(){
        Scanner in = new Scanner(inputStream);
        String command = "";
        while(!command.equals("exit")){
            outputStream.print("Введите команду: ");
            String[] commandAndArguments = in.nextLine().split(" ");
            command = commandAndArguments[0];
            switch (command){
                case "add":
                    addWorker(commandAndArguments);
                    break;
                case "remove":
                    removeWorker(commandAndArguments);
                    break;
                case "print":
                    if(commandAndArguments.length == 1){
                        printAll();
                    }else if(commandAndArguments.length == 2){
                        int id;
                        try{
                            id = Integer.parseInt(commandAndArguments[1]);
                            print(id);
                        }catch (NumberFormatException e){
                            outputStream.println("Не верный аргумент");
                        }
                    }else{
                        outputStream.println("Не верно введен аргумент");
                    }
                    break;
                case "save":
                    save(commandAndArguments);
                    break;
                case "load":
                    load(commandAndArguments);
                    break;
                case "exit":
                    break;
                default:
                    outputStream.println("Не верно введена команда");
                    break;
            }
        }
    }

    
    //Добавление сотрудника.
    private void addWorker(String[] args){
        if(args.length - 1 == REQUIRED_NUMBER_OF_ARGS_TO_ADD){
            String[] fullName = new String[3];
            int id;
            for(int i = 1; i <= 3; i++){
                fullName[i-1] = args[i];
            }

            String name = fullName[1];
            String patronymic = fullName[2];
            String lastname = fullName[0];

            id = staff.add(name, patronymic, lastname);
            outputStream.println(id);
        }else{
            outputStream.println("Не верный аргумент");
        }
    }


    private void removeWorker(String[] args){
        if(args.length - 1 == REQUIRED_NUMBER_OF_ARGS_TO_REMOVE){
            int id;
            try {
                id = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                outputStream.println("Не верный аргумент табельного номера");
                return;
            }

            if((staff.hasId(id))){
                staff.remove(id);
                outputStream.println("Сотрудник успешно удален");
            }else {
                outputStream.println("Сотрудника с таким номером нет");
            }

        }else{
            outputStream.println("Не верный аргумент");
        }
    }


    private void printAll(){
        Set<Worker> allWorker = staff.getAllWorker();
        for(Worker worker: allWorker){
            outputStream.println(worker.toString());
        }
    }


    private void print(int id){
        if(staff.hasId(id)){
            Worker worker = staff.getWorker(id);
            outputStream.println(worker.toString());
        }else{
            outputStream.println("Сотрудника с таким номером нет");
        }
    }


    private DataFile getDataFileByFormat(String format){
        DataFile df;
        switch (format) {
            case "txt" :
                df = new TxtDataFile();
                break;
            case "xml" :
                df = new XmlDataFile();
                break;
            case "json" :
                df = new JsonDataFile();
                break;
            default :
                df = null;
                break;
        };
        return df;
    }


    private void save(String[] args){
        String path = "";
        DataFile file = getDataFileByFormat(args[args.length - 1]);

        //Если расширение не задано, то по умолчанию сохраняем в txt
        if(file == null){
            if(args.length > 1){
                for(int i = 1; i <= args.length - 1; i++){
                    path += " " + args[i];
                }
                try {
                    path = path.trim();
                    staff.save(path, new TxtDataFile());
                    outputStream.println("Данные успешно сохранены в файл");
                } catch (FileException e) {
                    outputStream.println(e.getMessage());
                }
            }else{
                outputStream.println("Укажите путь к файлу");
            }
        }else{
            if(args.length > 2){
                for(int i = 1; i <= args.length - 2; i++){
                    path += " " + args[i];
                }
                try {
                    path = path.trim();
                    staff.save(path, file);
                    outputStream.println("Данные успешно сохранены в файл");
                } catch (FileException e) {
                    outputStream.println(e.getMessage());
                }
            }
        }
    }


    private void load(String[] args){
        DataFile file = getDataFileByFormat(args[args.length - 1]);
        String path = "";

        if(file == null){
            if(args.length > 1){
                for(int i = 1; i <= args.length - 1; i++){
                    path += " " + args[i];
                }
                try {
                    path = path.trim();
                    staff.load(path, new TxtDataFile());
                    outputStream.println("Данные успешно загружены из файла");
                } catch (FileException e) {
                    outputStream.println(e.getMessage());
                }
            }else{
                outputStream.println("Укажите путь к файлу");
            }
        }else{
            if(args.length > 2){
                for(int i = 1; i <= args.length - 2; i++){
                    path += " " + args[i];
                }
                try {
                    path = path.trim();
                    staff.load(path, file);
                    outputStream.println("Данные успешно загружены");
                } catch (FileException e) {
                    outputStream.println(e.getMessage());
                }
            }
        }
    }
}
