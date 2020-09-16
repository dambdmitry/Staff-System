package unit;

import org.internship.system.exceptions.FileException;
import org.internship.system.exceptions.FileSaveException;
import org.internship.system.files.XmlDataFile;
import org.internship.system.models.Worker;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.Assert.*;

public class XmlDataFileTest {
    XmlDataFile file = new XmlDataFile();
    String path = System.getProperty("user.dir") + "/src/test/resources/TestXML";
    String testFileName = "/test.xml";
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

        workers.add(new Worker(1, "Шилова", "Галина", "Николаевна"));
        workers.add(new Worker(2, "Задворная", "Ольга", "Николаевна"));
        workers.add(new Worker(3, "Пучков", "Данил", "Евгеньевич"));

        try{
            file.saveToFile(path + testFileName, workers);

            sampleReader = new Scanner(new File(path + "/fileToSave.xml"));
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
        Worker[] expectedWorkers = new Worker[]{new Worker(1, "Строеньев", "Степан", "Дмитриевич"),
                new Worker(2, "Глазов", "Александр", "Михайлович"),
                new Worker(3, "Озеров", "Дмитрий", "Андреевич")};
        try {
            Set<Worker> workers = file.loadFormFile(path + "/fileToLoad.xml");
            assertArrayEquals(expectedWorkers, workers.toArray());
        } catch (FileException e) {
            fail();
        }
    }
}