package FileManagement;

import Exceptions.FileException;
import Exceptions.FileSaveException;
import StaffSystem.Worker;

import java.util.Set;

public interface DataFile {
    void saveToFile(String path, Set<Worker> allWorker) throws FileSaveException;
    Set<Worker> loadFormFile(String path) throws FileException;
}
