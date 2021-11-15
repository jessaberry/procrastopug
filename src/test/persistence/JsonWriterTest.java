package persistence;

import exceptions.DuplicateItemException;
import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            ItemList list = new ItemList("coconut");
            JsonWriter writer = new JsonWriter("./data/\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            //expected
        }
    }

    @Test
    void testWriterEmptyItemList() {
        try {
            ItemList list = new ItemList("coconut");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyItemList.json");
            writer.open();
            writer.write(list);
            writer.close();
        } catch (IOException e) {
            fail("Should not have thrown IOException");
        }
    }

    @Test
    void testWriterGeneralItemList() {
        try {
            ItemList list = new ItemList("coconut");
            addSixItems(list);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralItemList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralItemList.json");
            list = reader.read();
            assertEquals("coconut",list.getPugName());
            List<Item> incomplete = list.getIncompleteItems();
            List<Item> procrastinated = list.getProcrastinatedItems();
            List<Item> completed = list.getCompletedItems();
            List<Item> failed = list.getFailedItems();
            assertEquals(incomplete.size(),2);
            assertEquals(procrastinated.size(),2);
            assertEquals(completed.size(),2);
            assertEquals(failed.size(),0);

            checkInfo("vigorously water plants", Type.RECURRING, incomplete.get(0));
            checkInfo("bake cookies", Type.ONETIME, incomplete.get(1));
            checkInfo("turn on AC", Type.RECURRING, procrastinated.get(0));
            checkInfo("teach yoga", Type.ONETIME, procrastinated.get(1));
            checkInfo("turn off AC", Type.RECURRING, completed.get(0));
            checkInfo("call the boss", Type.ONETIME, completed.get(1));

        } catch (IOException | DuplicateItemException e) {
            fail("Should not have thrown IOException");
        }
    }

    @Test
    void testWriterResetItemList() {
        try {
            ItemList list = new ItemList("coconut");
            addSixItems(list);
            list.reset();
            JsonWriter writer = new JsonWriter("./data/testWriterResetItemList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterResetItemList.json");
            list = reader.read();
            assertEquals("coconut",list.getPugName());
            List<Item> incomplete = list.getIncompleteItems();
            List<Item> procrastinated = list.getProcrastinatedItems();
            List<Item> completed = list.getCompletedItems();
            List<Item> failed = list.getFailedItems();
            assertEquals(incomplete.size(),3);
            assertEquals(procrastinated.size(),0);
            assertEquals(completed.size(),1);
            assertEquals(failed.size(),2);

            checkInfo("vigorously water plants", Type.RECURRING, incomplete.get(0));
            checkInfo("bake cookies", Type.ONETIME, failed.get(0));
            checkInfo("turn off AC", Type.RECURRING, incomplete.get(1));
            checkInfo("teach yoga", Type.ONETIME, failed.get(1));
            checkInfo("turn on AC", Type.RECURRING, incomplete.get(2));
            checkInfo("call the boss", Type.ONETIME, completed.get(0));

        } catch (IOException | DuplicateItemException e) {
            fail("Should not have thrown IOException");
        }
    }

    // EFFECTS: adds 6 items to ItemList, with the status of
    // one of each item type set to 'incomplete', 'completed', or 'procrastinated'
    private void addSixItems(ItemList list) {
        Item i1 = new RecurringItem("vigorously water plants");
        Item i2 = new RecurringItem("turn on AC");
        Item i3 = new RecurringItem("turn off AC");
        Item i4 = new OneTimeItem("bake cookies");
        Item i5 = new OneTimeItem("teach yoga");
        Item i6 = new OneTimeItem("call the boss");

        try {
            list.addItem(i1);
            list.addItem(i2);
            list.addItem(i3);
            list.addItem(i4);
            list.addItem(i5);
            list.addItem(i6);
        } catch (DuplicateItemException e) {
            fail("should not have thrown exception");
        }

        list.moveItem(Status.PROCRASTINATED, i2);
        list.moveItem(Status.PROCRASTINATED, i5);
        list.moveItem(Status.COMPLETED, i3);
        list.moveItem(Status.COMPLETED, i6);
    }
}
