package org.internship.system.files;

import org.internship.system.exceptions.FileException;
import org.internship.system.exceptions.FileSaveException;
import org.internship.system.models.Worker;

import java.util.Set;

public interface DataFile {
    void saveToFile(String path, Set<Worker> allWorker) throws FileSaveException;
    Set<Worker> loadFormFile(String path) throws FileException;
}
