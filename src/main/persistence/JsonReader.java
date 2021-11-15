package persistence;

import exceptions.DuplicateItemException;
import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// source of code: JSONSerializerDemo
//                 URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Represents a reader that reads JSON representation of Procrastopug from file
public class JsonReader {
    private String source;

    // EFFECTS: makes reader that reads an ItemList from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads ItemList from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ItemList read() throws IOException, DuplicateItemException {
        String data = readFile(source);
        JSONObject object = new JSONObject(data);
        return parseItemList(object);
    }

    // EFFECTS: reads source file as a string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(sb::append);
        }
        return sb.toString();
    }

    // EFFECTS: parses ItemList from JSON object and returns it
    private ItemList parseItemList(JSONObject jsonObject) throws DuplicateItemException {
        String name = jsonObject.getString("pug name");
        ItemList list = new ItemList(name);
        addItems(list, jsonObject, Status.INCOMPLETE, "incomplete");
        addItems(list, jsonObject, Status.PROCRASTINATED, "procrastinated");
        addItems(list, jsonObject, Status.COMPLETED, "completed");
        addItems(list, jsonObject, Status.FAILED, "failed");
        return list;
    }

    // MODIFIES: ItemList
    // EFFECTS: parses items from JSON object and adds them to ItemList
    private void addItems(ItemList list, JSONObject jsonObject, Status status, String key)
            throws DuplicateItemException {
        JSONArray jsonArray = jsonObject.getJSONArray(key);
        for (Object json : jsonArray) {
            JSONObject next = (JSONObject) json;
            addItem(list, next, status);
        }
    }

    // MODIFIES: ItemList
    // EFFECTS: parses an item from JSON object and adds it to ItemList
    private void addItem(ItemList list, JSONObject jsonObject, Status status) throws DuplicateItemException {
        String name = jsonObject.getString("item name");
        Type type = Type.valueOf(jsonObject.getString("type"));
        Item i;
        if (type == Type.RECURRING) {
            i = new RecurringItem(name);
        } else {
            i = new OneTimeItem(name);
        }
        i.setStatus(status);
        list.addItem(i);
        list.moveItem(status, i);
    }
}
