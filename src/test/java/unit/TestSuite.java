package unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ConsoleAppTest.class, JsonDataFileTest.class, StaffTest.class, TxtDataFileTest.class, WorkerTest.class, XmlDataFileTest.class})
public class TestSuite {
}
