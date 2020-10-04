package unit;

import org.internship.system.models.Worker;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WorkerTest {
    int id = 1;
    String lastName = "worker1";
    String firstName = "worker2";
    String patronymic = "worker3";
    Worker worker = new Worker(id, lastName, firstName, patronymic);


    @Test
    public void testToString() {
        String expectedString = id + " " + lastName + " " + firstName + " " + patronymic;
        assertEquals(expectedString, worker.toString());
    }

    @Test
    public void testEquals() {
        Worker trueWorker = new Worker(id, lastName, firstName, patronymic);
        Worker falseWorker = new Worker(-1, "1", "2", "3");
        assertEquals(worker, trueWorker);
        assertNotEquals(worker, falseWorker);
        assertNotEquals(worker, null);
    }

    @Test
    public void testHashCode(){
        Worker trueWorker = new Worker(id, lastName, firstName, patronymic);
        Worker falseWorker = new Worker(-1, "1", "2", "3");
        assertEquals(trueWorker.hashCode(), worker.hashCode());
        assertNotEquals(worker.hashCode(), falseWorker.hashCode());
    }
}