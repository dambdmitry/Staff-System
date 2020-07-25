package unit;

import StaffSystem.Worker;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {
    int id = 1;
    String name = "worker";
    Worker worker = new Worker(id, name);


    @Test
    public void testToString() {
        String expectedString = id + " " + name;
        assertEquals(expectedString, worker.toString());
    }

    @Test
    public void testEquals() {
        Worker trueWorker = new Worker(id, name);
        Worker falseWorker = new Worker(-1, "");
        assertEquals(worker, trueWorker);
        assertNotEquals(worker, falseWorker);
        assertNotEquals(worker, null);
    }

    @Test
    public void testHashCode(){
        Worker trueWorker = new Worker(id, name);
        Worker falseWorker = new Worker(-1, "");
        assertEquals(trueWorker.hashCode(), worker.hashCode());
        assertNotEquals(worker.hashCode(), falseWorker.hashCode());
    }

}