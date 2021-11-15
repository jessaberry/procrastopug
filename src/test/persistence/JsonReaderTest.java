package persistence;

import exceptions.DuplicateItemException;
import model.Item;
import model.ItemList;
import model.Type;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonexistentFile() {
        JsonReader reader = new JsonReader("./data/fileDoesNotExist.json");
        try {
            ItemList list = reader.read();
            fail("IOException expected");
        } catch (IOException | DuplicateItemException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyItemList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyItemList.json");
        try {
            ItemList list = reader.read();
            assertEquals("diablo", list.getPugName());
        } catch (IOException | DuplicateItemException e) {
            fail("Unable to read from file");
        }
    }

    @Test
    void testReaderGeneralItemList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralItemList.json");
        try {
            ItemList list = reader.read();
            assertEquals("diablo", list.getPugName());
            List<Item> incomplete = list.getIncompleteItems();
            List<Item> procrastinated = list.getProcrastinatedItems();
            List<Item> completed = list.getCompletedItems();
            List<Item> failed = list.getFailedItems();
            assertEquals(2, incomplete.size());
            assertEquals(0, procrastinated.size());
            assertEquals(1, completed.size());
            assertEquals(0, failed.size());
            checkInfo("rain fire", Type.RECURRING, incomplete.get(0));
            checkInfo("hell on earth", Type.ONETIME, incomplete.get(1));
            checkInfo("take a break", Type.RECURRING, completed.get(0));

        } catch (IOException | DuplicateItemException e) {
            fail("Unable to read from file");
        }
    }
}
