package unit;

import Exceptions.FileException;
import Exceptions.FileSaveException;
import FileManagement.TxtDataFile;
import StaffSystem.Worker;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.Assert.*;

public class TxtDataFileTest {
    TxtDataFile file = new TxtDataFile();
    String path = System.getProperty("user.dir") + "/src/test/resources/TestTXT";
    String testFileName = "/test.txt";
    Scanner sampleReader;
    Scanner reader;

    @After
    public void tearDown() throws Exception {
        File f = new File(path + testFileName);
        f.delete();
    }

    @Test
    public void saveToFile() {
        Set<Worker> workers = new LinkedHashSet<>();

        workers.add(new Worker(1, "Яблоков Анатолий Анатольевич"));
        workers.add(new Worker(2, "Сямжина Елена Алексеевна"));
        workers.add(new Worker(3, "Михайлов Даниил Александрович"));

        try{
            file.saveToFile(path + testFileName, workers);

            sampleReader = new Scanner(new File(path + "/expectedFileToSave.txt"));
            reader = new Scanner(new File(path + testFileName));

            while(reader.hasNextLine() && sampleReader.hasNextLine()){
                assertEquals(sampleReader.nextLine(), reader.nextLine());
            }
            assertFalse(reader.hasNextLine() || sampleReader.hasNextLine());
        } catch (FileNotFoundException | FileSaveException e) {
            fail();
        } finally {
            sampleReader.close();
            reader.close();
        }
    }

    @Test
    public void loadFormFile() {
        Worker[] expectedWorkers = new Worker[]{new Worker(1, "Рыжов Игорь Дмитриевич"),
                new Worker(2, "Мкртычян Арман Агасиевич"),
                new Worker(3, "Бирюков Дмитрий Михайлович")};
        try {
            Set<Worker> workers = file.loadFormFile(path + "/fileToLoad.txt");
            assertArrayEquals(expectedWorkers, workers.toArray());
        } catch (FileException e) {
            fail();
        }
    }
}