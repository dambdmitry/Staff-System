package unit;

import org.internship.system.exceptions.FileException;
import org.internship.system.exceptions.NotFoundWorkerException;
import org.internship.system.files.DataFile;
import org.internship.system.files.JsonDataFile;
import org.internship.system.files.TxtDataFile;
import org.internship.system.files.XmlDataFile;
import org.internship.system.models.Worker;
import org.internship.system.staff.Staff;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.Assert.*;

public class StaffTest {
    Staff staff = new Staff();
    Staff initStaff = new Staff();

    Scanner txtSampleReader;
    Scanner txtReader;
    Scanner xmlSampleReader;
    Scanner xmlReader;
    Scanner jsonSampleReader;
    Scanner jsonReader;


    @AfterClass
    public static void createDB() throws IOException, URISyntaxException {
        DatabaseInit.startNewDB();
    }

    @Before
    public void setUp() throws IOException, URISyntaxException {
        DatabaseInit.startNewDB();
    }

    @Test
    public void testHasId() {
        staff.add("worker", "workers", "work");
        staff.add("secondworker", "secondworkers", "secondwork");
        assertTrue(staff.hasId(1));
        assertTrue(staff.hasId(2));
        assertFalse(staff.hasId(-1));
    }

    @Test
    public void testAdd() {
        int id = staff.add("worker", "workers", "work");

        int expectedId = 1;
        String expectedLastName = "worker";
        String expectedFirstName = "workers";
        String expectedPatronymic = "work";
        Worker expectedWorker = new Worker(expectedId, expectedLastName, expectedFirstName, expectedPatronymic);

        assertEquals(expectedId, id); //Проверка возвращаемого значения метода.
        assertEquals(expectedWorker, staff.getWorker(expectedId)); //Проверка того, что ожидаемый работник добавился в org.internship.system.staff.
        assertTrue(staff.hasId(expectedId));
    }


    @Test(expected = NotFoundWorkerException.class)
    public void testRemoveException() {
        staff.remove(-1);
    }


    @Test
    public void testRemove() {
        int idForRemove = staff.add("worker", "workers", "work");
        int id = staff.add("secondworker", "secondworkers", "work");

        staff.remove(idForRemove);

        assertFalse(staff.hasId(idForRemove));
        assertTrue(staff.hasId(id));
    }

    @Test(expected = NotFoundWorkerException.class)
    public void testGetWorkerException() {
        staff.getWorker(-1);
    }

    @Test
    public void testGetWorker() {
        Worker expectedWorker = new Worker(2, "worker", "workers", "work"); //Имитация объекта который войдет в Staff.
        staff.add("firstworker", "workers", "work");
        int id = staff.add("worker", "workers", "work");
        staff.add("test", "test", "test");
        Worker worker = staff.getWorker(id);
        assertEquals(expectedWorker, worker);
        assertEquals(expectedWorker.toString(), worker.toString());
    }

    @Test
    public void getAllWorker() {
        Set<Worker> expectedSet = new LinkedHashSet<Worker>();
        assertEquals(expectedSet,  staff.getAllWorker());
        Worker[] expectedWorkers = new Worker[]{new Worker(1, "firstworker", "fworkers", "fwork"),
                new Worker(2, "secondworker", "sworkers", "swork")};
        staff.add("firstworker", "fworkers", "fwork");
        staff.add("secondworker", "sworkers", "swork");
        assertArrayEquals(staff.getAllWorker().toArray(), expectedWorkers);

    }

    @Test
    public void save() {
        String txtPath = System.getProperty("user.dir") + "/src/test/resources/TestStaff/Txt/test.txt";
        String xmlPath = System.getProperty("user.dir") + "/src/test/resources/TestStaff/Xml/test.xml";
        String jsonPath = System.getProperty("user.dir") + "/src/test/resources/TestStaff/Json/test.json";

        String txtSamplePath = System.getProperty("user.dir") + "/src/test/resources/TestStaff/Txt/sampleSave.txt";
        String xmlSamplePath = System.getProperty("user.dir") + "/src/test/resources/TestStaff/Xml/sampleSave.xml";
        String jsonSamplePath = System.getProperty("user.dir") + "/src/test/resources/TestStaff/Json/sampleSave.json";
        staff.add("Бирюков" , "Дмитрий","Михайлович");
        staff.add("Строеньев","Степан","Дмитриевич");
        staff.add("Глазов", "Александр", "Андреевич");

        try {
            staff.save(txtPath, new TxtDataFile());
            staff.save(xmlPath, new XmlDataFile());
            staff.save(jsonPath, new JsonDataFile());
            assertTrue(new File(txtPath).exists() && new File(xmlPath).exists() && new File(jsonPath).exists());

            txtSampleReader = new Scanner(new File(txtSamplePath));
            txtReader = new Scanner(new File(txtPath));
            while(txtSampleReader.hasNextLine() && txtReader.hasNextLine()){
                assertEquals(txtSampleReader.nextLine(), txtReader.nextLine());
            }
            assertFalse(txtSampleReader.hasNextLine() || txtReader.hasNextLine());
            txtReader.close();
            txtSampleReader.close();

            xmlSampleReader = new Scanner(new File(xmlSamplePath));
            xmlReader = new Scanner(new File(xmlPath));
            while(xmlSampleReader.hasNextLine() && xmlReader.hasNextLine()){
                assertEquals(xmlSampleReader.nextLine(), xmlReader.nextLine());
            }
            assertFalse(xmlSampleReader.hasNextLine() || xmlReader.hasNextLine());
            xmlReader.close();
            xmlSampleReader.close();


            jsonSampleReader = new Scanner(new File(jsonSamplePath));
            jsonReader = new Scanner(new File(jsonPath));
            while(jsonSampleReader.hasNextLine() && jsonReader.hasNextLine()){
                assertEquals(jsonSampleReader.nextLine(), jsonReader.nextLine());
            }
            assertFalse(jsonSampleReader.hasNextLine() || jsonReader.hasNextLine());
            jsonReader.close();
            jsonSampleReader.close();

            new File(txtPath).delete();
            new File(xmlPath).delete();
            new File(jsonPath).delete();
        } catch (FileException | FileNotFoundException e) {
            fail();
        }


    }

    @Test
    public void load() {
        String txtPath = System.getProperty("user.dir") + "/src/test/resources/TestStaff/Txt/load.txt";
        String xmlPath = System.getProperty("user.dir") + "/src/test/resources/TestStaff/Xml/load.xml";
        String jsonPath = System.getProperty("user.dir") + "/src/test/resources/TestStaff/Json/load.json";

        Worker firstWorker = new Worker(1, "Бирюков", "Дмитрий", "Михайлович");
        Worker secondWorker = new Worker(2, "Строеньев", "Степан", "Дмитриевич");
        Worker thirdWorker = new Worker(3, "Глазов", "Александр", "Андреевич");

        try {
            DataFile[] dataFiles = new DataFile[]{new TxtDataFile(), new XmlDataFile(), new JsonDataFile()};
            String[] filePaths = new String[]{txtPath, xmlPath, jsonPath};

            for(int i = 0; i < dataFiles.length; i++){
                staff.load(filePaths[i], dataFiles[i]);
                assertTrue(staff.hasId(1) && staff.hasId(2) && staff.hasId(3));
                assertEquals(staff.getWorker(firstWorker.getId()), firstWorker);
                assertEquals(staff.getWorker(secondWorker.getId()), secondWorker);
                assertEquals(staff.getWorker(thirdWorker.getId()), thirdWorker);
            }
        } catch (FileException e) {
            fail();
        }
    }
}