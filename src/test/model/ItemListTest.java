package model;

import exceptions.DuplicateItemException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemListTest {
    private ItemList items;
    private Item i; // one-time item
    private Item i2; // recurring item

    @BeforeEach
    public void setup() {
        items = new ItemList("Jabba");
        i = new OneTimeItem("wash dishes");
        i2 = new RecurringItem("50 push-ups");
        assertEquals(items.getPugName(),"Jabba");
    }

    @Test
    public void testAddItem() {
        try {
            items.addItem(i);
            assertTrue(items.getIncompleteItems().contains(i));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testAddExistingItemSameNameSameType() {
        Item i2 = new OneTimeItem("wash dishes");
        try {
            items.addItem(i);
            items.addItem(i2);
            fail();
        } catch (DuplicateItemException e) { // expected
            assertTrue(items.getIncompleteItems().contains(i));
            assertEquals(items.getIncompleteItems().size(), 1);
        }
    }

    @Test
    public void testAddExistingItemSameNameDifferentType() {
        Item i2 = new RecurringItem("wash dishes");
        try {
            items.addItem(i);
            items.addItem(i2);
            fail();
        } catch (DuplicateItemException e) { // expected
            assertTrue(items.getIncompleteItems().contains(i));
            assertEquals(items.getIncompleteItems().size(), 1);
        }
    }

    @Test
    public void testAddExistingItemSameNameDifferentStatus() {
        Item i2 = new OneTimeItem("wash dishes");
        try {
            items.addItem(i);
            items.moveItem(Status.PROCRASTINATED, i);
            items.addItem(i2);
            fail();
        } catch (DuplicateItemException e) { // expected
            assertTrue(items.getProcrastinatedItems().contains(i));
            assertEquals(items.getProcrastinatedItems().size(), 1);
            assertEquals(items.getIncompleteItems().size(), 0);
        }
    }

    @Test
    public void testAddExistingItemDifferentType() {
        try {
            items.addItem(i);
            items.addItem(i2);
        } catch (DuplicateItemException e) {
            fail("should not have thrown exception");
        } finally {
            assertTrue(items.getIncompleteItems().contains(i));
            assertTrue(items.getIncompleteItems().contains(i2));
            assertEquals(items.getIncompleteItems().size(), 2);
        }
    }

    @Test
    public void testFindItemList() {
        assertEquals(items.findItemList(i),items.getIncompleteItems());
    }

    @Test
    public void testDefaultSwitchIncompleteItem() {
        try {
            items.addItem(i);
            assertTrue(items.getIncompleteItems().contains(i));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testProcrastinateItem() {
        try {
            items.addItem(i);
            items.moveItem(Status.PROCRASTINATED, i);
            assertFalse(items.getIncompleteItems().contains(i));
            assertTrue(items.getProcrastinatedItems().contains(i));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testCompleteItem() {
        try {
            items.addItem(i);
            items.moveItem(Status.COMPLETED, i);
            assertFalse(items.getIncompleteItems().contains(i));
            assertTrue(items.getCompletedItems().contains(i));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testFailedItem() {
        try {
            items.addItem(i);
            items.moveItem(Status.FAILED, i);
            assertFalse(items.getIncompleteItems().contains(i));
            assertTrue(items.getFailedItems().contains(i));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testSetters() {
        try {
            items.addItem(i);
            items.addItem(i2);
            items.moveItem(Status.PROCRASTINATED, i2);

            items.setCompletedItems(items.getIncompleteItems());
            items.setFailedItems(items.getProcrastinatedItems());
            items.setProcrastinatedItems(items.getCompletedItems());
            items.setIncompleteItems(items.getFailedItems());

            assertEquals(items.getIncompleteItems().size(),1);
            assertEquals(items.getProcrastinatedItems().size(),1);
            assertEquals(items.getCompletedItems().size(),1);
            assertEquals(items.getFailedItems().size(),1);

            assertTrue(items.getIncompleteItems().contains(i2));
            assertTrue(items.getProcrastinatedItems().contains(i));
            assertTrue(items.getCompletedItems().contains(i));
            assertTrue(items.getFailedItems().contains(i2));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testMoveManyItems() {
        Item i2 = new OneTimeItem("wash doorknob");
        Item i3 = new OneTimeItem("42km jog");
        try {
            items.addItem(i);
            items.addItem(i2);
            items.addItem(i3);
            items.moveItem(Status.COMPLETED, i);
            items.moveItem(Status.PROCRASTINATED, i2);
            items.moveItem(Status.FAILED, i3);
            assertEquals(items.getIncompleteItems().size(), 0);
            assertTrue(items.getCompletedItems().contains(i));
            assertTrue(items.getProcrastinatedItems().contains(i2));
            assertTrue(items.getFailedItems().contains(i3));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testMoveSameItemTwice() {
        try {
            items.addItem(i);
            items.moveItem(Status.PROCRASTINATED,i);
            assertTrue(items.getProcrastinatedItems().contains(i));
            items.moveItem(Status.COMPLETED,i);
            assertTrue(items.getCompletedItems().contains(i));
            assertEquals(items.getIncompleteItems().size(), 0);
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testResetOneTimeItemProcrastinated() {
        try {
            items.addItem(i);
            items.moveItem(Status.PROCRASTINATED,i);
            items.reset();
            assertTrue(items.getFailedItems().contains(i));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testResetOneTimeItemFailed() {
        try {
            items.addItem(i);
            items.reset();
            assertTrue(items.getFailedItems().contains(i));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testResetOneTimeItemCompleted() {
        try {
            items.addItem(i);
            items.moveItem(Status.COMPLETED,i);
            items.reset();
            assertTrue(items.getCompletedItems().contains(i));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testResetRecurringItem() {
        try {
            items.addItem(i2);
            items.reset();
            assertTrue(items.getIncompleteItems().contains(i2));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }

    @Test
    public void testToJsonEmpty() {
        JSONObject json = new JSONObject();
        Item i = new OneTimeItem("wash dishes");
        json.put("pug name", "Jabba");
        json.put("incomplete", items.listToJson(items.getIncompleteItems()));
        json.put("completed", items.listToJson(items.getCompletedItems()));
        json.put("procrastinated", items.listToJson(items.getProcrastinatedItems()));
        json.put("failed", items.listToJson(items.getFailedItems()));
        assertEquals(json.toString(),items.toJson().toString());
    }

    @Test
    public void testToJsonNonempty() {
        Item i = new OneTimeItem("wash dishes");
        Item i2 = new OneTimeItem("wash doorknob");
        Item i3 = new OneTimeItem("42km jog");

        try {
            items.addItem(i);
            items.addItem(i2);
            items.addItem(i3);
            items.moveItem(Status.PROCRASTINATED, i2);
            items.moveItem(Status.COMPLETED, i3);

            JSONObject json = new JSONObject();
            json.put("pug name", "Jabba");
            json.put("incomplete", items.listToJson(items.getIncompleteItems()));
            json.put("completed", items.listToJson(items.getCompletedItems()));
            json.put("procrastinated", items.listToJson(items.getProcrastinatedItems()));
            json.put("failed", items.listToJson(items.getFailedItems()));
            assertEquals(json.toString(), items.toJson().toString());
            assertTrue(items.toJson().toString().contains(i.toJson().toString()));
            assertTrue(items.toJson().toString().contains(i2.toJson().toString()));
            assertTrue(items.toJson().toString().contains(i3.toJson().toString()));
        } catch (DuplicateItemException e) {
            fail("Should not have thrown duplicate name exception");
        }
    }
}