package persistence;

import model.ItemList;
import org.json.JSONObject;
import java.io.*;

// source of code: JSONSerializerDemo
//                 URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Represents a writer that writes JSON representation of Procrastopug to file
public class JsonWriter {
    private PrintWriter writer;
    private String destination;

    // EFFECTS: writer to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS:  opens writer, throws FileNotFoundException if the destination file cannot be written into
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS:  writes JSON representation of ItemList to file
    public void write(ItemList itemList) {
        JSONObject jsonObject = itemList.toJson();
        writer.print(jsonObject.toString(4));
    }

    // MODIFIES: this
    // EFFECTS:  closes writer
    public void close() {
        writer.close();
    }
}
