package unit;

import StaffSystem.ConsoleApp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.Assert.*;

public class ConsoleAppTest {
    private ConsoleApp consoleApp;
    private String path;
    private FileInputStream fis;
    private PrintStream ps;

    public ConsoleAppTest(){
        try {
            consoleApp = new ConsoleApp();
            path = System.getProperty("user.dir") + "/src/test/resources/TestConsoleApp";
            fis = new FileInputStream(path + "/commands.txt");
            ps = new PrintStream(path + "/actualText.txt");
            consoleApp.setInputStream(fis);
            consoleApp.setOutputStream(ps);
        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        fis.close();
        ps.close();
        File txtf = new File(path + "/actualTxt.txt");
        File xmlf = new File(path + "/actualXml.xml");
        File jsonf = new File(path + "/actualJson.json");
        File actualText = new File(path + "/actualText.txt");
        txtf.delete();
        xmlf.delete();
        jsonf.delete();
        actualText.delete();
    }

    @Test
    public void consoleRun() {
        consoleApp.consoleRun();
        assertTrue(new File(path + "/actualTxt.txt").exists()
                && new File(path + "/actualXml.xml").exists()
                && new File(path + "/actualJson.json").exists());

        String[] actualFilePaths = new String[]{path + "/actualTxt.txt",
                path + "/actualXml.xml",
                path + "/actualJson.json",
                path + "/actualText.txt"};
        String[] expectedFilePaths = new String[]{path + "/expectedTxt.txt",
                path + "/expectedXml.xml",
                path + "/expectedJson.json",
                path + "/expectedText.txt"};
        Scanner actualReader;
        Scanner expectedReader;
        for(int i = 0; i < expectedFilePaths.length; i++){
            try{
                actualReader = new Scanner(new File(actualFilePaths[i]));
                expectedReader = new Scanner(new File(expectedFilePaths[i]));
                while (actualReader.hasNextLine() && expectedReader.hasNextLine()){
                    assertEquals(expectedReader.nextLine(), actualReader.nextLine());
                }
                assertFalse(actualReader.hasNextLine() || expectedReader.hasNextLine());

                actualReader.close();
                expectedReader.close();
            } catch (FileNotFoundException e) {
                fail();
            }
        }
    }
}