package model;

import org.json.JSONObject;
import persistence.JsonWriteable;

import java.util.Objects;

// abstract class for an interface
public abstract class Item implements JsonWriteable {
    protected String itemName;
    protected Type type; // recurring or one-time
    protected Status status;
    protected boolean recurring; // true for recurring item, false for one-time item

    // MODIFIES: this
    // EFFECTS: creates a new calendar item
    public Item(String itemName, boolean recurring) {
        this.itemName = itemName;
        this.recurring = recurring;
        status = Status.INCOMPLETE;

        if (recurring) {
            type = Type.RECURRING;
        } else {
            type = Type.ONETIME;
        }
    }

    // EFFECTS: sets the values of completed, procrastinated, and failed to false if they were previously modified
    public abstract void reset();

    public String getName() {
        return itemName;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    // MODIFIES: this
    // EFFECTS: sets the value of 'completed' to true
    public void complete() {
        status = Status.COMPLETED;
    }

    // MODIFIES: this
    // EFFECTS: sets the value of 'procrastinated' to true
    public void procrastinate() {
        status = Status.PROCRASTINATED;
    }

    // MODIFIES: this
    // EFFECTS: sets the value of 'failed' to true
    public void fail() {
        status = Status.FAILED;
    }

    // MODIFIES: this
    // EFFECTS: sets the status of an item
    public void setStatus(Status status) {
        this.status = status;
    }

    // EFFECTS: returns string representation of this item
    public String toString() {
        return getType() + ": " + itemName;
    }

    // modified from: JSONSerializerDemo
    //           URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("item name", getName());
        json.put("type", getType());
        return json;
    }

    // EFFECTS: overrides equals for items with the same item name, regardless of status and type of item
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        Item item = (Item) o;
        return itemName.equals(item.itemName);
    }

    // EFFECTS: overrides hashcode
    @Override
    public int hashCode() {
        return Objects.hash(itemName);
    }
}
