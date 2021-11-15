package persistence;

import org.json.JSONObject;

public interface JsonWriteable {
    // EFFECTS: return this as a JSON Object
    JSONObject toJson();
}
