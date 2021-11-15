package persistence;

import model.Item;
import model.Type;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkInfo(String name, Type type, Item item) {
        assertEquals(name, item.getName());
        assertEquals(type, item.getType());
    }
}
