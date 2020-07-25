package FileManagement;

import Exceptions.FileLoadException;
import Exceptions.FileSaveException;
import StaffSystem.Worker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class JsonDataFile implements DataFile {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();


    //Объект данного класса будет сохранятся в JSON.
    private static class StaffJSON {
        public List<Worker> staff = new ArrayList<Worker>();

        StaffJSON(Set<Worker> allWorker) {
            staff.addAll(allWorker);
        }

        StaffJSON(){}
    }


    @Override
    public void saveToFile(String path, Set<Worker> allWorker) throws FileSaveException {
        StaffJSON staff = new StaffJSON(allWorker);
        try (FileWriter writer = new FileWriter(path, true)) {
            gson.toJson(staff, writer);
        } catch (IOException e) {
            throw new FileSaveException("Ошибка сохранения в файл");
        }
    }


    //Извлечение данных из StaffJSON в Set.
    private Set<Worker> parseStaffJSON(StaffJSON staffJSON) {
        return new LinkedHashSet<Worker>(staffJSON.staff);
    }


    @Override
    public Set<Worker> loadFormFile(String path) throws FileLoadException {
        try (FileReader reader = new FileReader(path)) {
            StaffJSON staff = gson.fromJson(reader, StaffJSON.class);
            return parseStaffJSON(staff);
        } catch (IOException e) {
            throw new FileLoadException("Ошибка загрузки файла");
        }
    }
}
