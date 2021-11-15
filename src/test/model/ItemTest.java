package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
    private Item homework;
    private Item workout;

    // one-time item: 'homework' has name "Math assignment", and its status is incomplete at initialization
    // recurring item: 'workout' has name "Leg day", and its status is incomplete at initialization
    @BeforeEach
    public void setup() {
        homework = new OneTimeItem("Math assignment");
        workout = new RecurringItem("Leg day");
    }

    @Test
    public void testOneTimeConstructor() {
        assertEquals(homework.getName(),"Math assignment");
        assertFalse(homework.isRecurring());
        assertEquals(homework.getType(),Type.ONETIME);
        assertEquals(homework.getStatus(), Status.INCOMPLETE);
    }

    @Test
    public void testRecurringConstructor() {
        assertEquals(workout.getName(),"Leg day");
        assertTrue(workout.isRecurring());
        assertEquals(workout.getType(),Type.RECURRING);
        assertEquals(homework.getStatus(),Status.INCOMPLETE);
    }

    @Test
    public void testProcrastinated() {
        homework.procrastinate();
        assertEquals(homework.getStatus(), Status.PROCRASTINATED);
    }

    @Test
    public void testCompleted() {
        homework.complete();
        assertEquals(homework.getStatus(), Status.COMPLETED);
    }

    @Test
    public void testFailed() {
        homework.fail();
        assertEquals(homework.getStatus(),Status.FAILED);
    }

    @Test
    public void testIncompleteReset() {
        homework.reset();
        assertEquals(homework.getStatus(),Status.INCOMPLETE);
    }

    @Test
    public void testProcrastinatedReset() {
        homework.procrastinate();
        homework.reset();
        assertEquals(homework.getStatus(),Status.PROCRASTINATED);
    }

    @Test
    public void testOneTimeCompletedReset() {
        homework.complete();
        homework.reset();
        assertEquals(homework.getStatus(),Status.COMPLETED);
    }

    @Test
    public void testRecurringCompletedReset() {
        workout.complete();
        workout.reset();
        assertEquals(workout.getStatus(), Status.INCOMPLETE);
    }

    @Test
    public void testFailedReset() {
        homework.fail();
        homework.reset();
        assertEquals(homework.getStatus(),Status.FAILED);
    }

    @Test
    public void testSetStatus() {
        homework.setStatus(Status.COMPLETED);
        assertEquals(homework.getStatus(), Status.COMPLETED);
    }

    @Test
    public void testToString() {
        assertEquals(homework.toString(), "ONETIME: Math assignment");
    }

    @Test
    public void testToJson() {
        JSONObject json = new JSONObject();
        json.put("item name", "Math assignment");
        json.put("type", Type.ONETIME);
        assertEquals(json.toString(), homework.toJson().toString());
    }

    @Test
    public void testEqualsItemType() {
        Item workout = new RecurringItem("Leg day");
        Item legDay = new RecurringItem("Leg day");
        assertEquals(workout, legDay);
    }

    @Test
    public void testNotEqualsItemName() {
        Item workout = new RecurringItem("Leg day");
        Item legDay = new RecurringItem("Leg daeeee");
        assertNotEquals(workout, legDay);
    }

    @Test
    public void testNotEqualsItemType() {
        Item workout = new RecurringItem("Leg day");
        Item legDay = new OneTimeItem("Leg day");
        assertEquals(workout, legDay);
    }

    @Test
    public void testEqualsHashcode() {
        Item workout = new RecurringItem("Leg day");
        assertNotEquals(workout, new Object());
        assertEquals(workout.hashCode(), this.workout.hashCode());
    }
}
