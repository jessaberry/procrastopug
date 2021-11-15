package model;

import exceptions.DuplicateItemException;
import org.json.*;
import persistence.JsonWriteable;

import java.util.ArrayList;

public class ItemList implements JsonWriteable {
    ArrayList<Item> incompleteItems;
    ArrayList<Item> procrastinatedItems;
    ArrayList<Item> completedItems;
    ArrayList<Item> failedItems;
    String pugName;

    // EFFECTS: initializes 4 lists of different item statuses
    public ItemList(String pugName) {
        this.pugName = pugName;
        incompleteItems = new ArrayList<>();
        procrastinatedItems = new ArrayList<>();
        completedItems = new ArrayList<>();
        failedItems = new ArrayList<>();
    }

    public String getPugName() {
        return pugName;
    }

    public ArrayList<Item> getIncompleteItems() {
        return incompleteItems;
    }

    public ArrayList<Item> getProcrastinatedItems() {
        return procrastinatedItems;
    }

    public ArrayList<Item> getCompletedItems() {
        return completedItems;
    }

    public ArrayList<Item> getFailedItems() {
        return failedItems;
    }

    public void setIncompleteItems(ArrayList<Item> items) {
        incompleteItems = items;
    }

    public void setProcrastinatedItems(ArrayList<Item> items) {
        procrastinatedItems = items;
    }

    public void setCompletedItems(ArrayList<Item> items) {
        completedItems = items;
    }

    public void setFailedItems(ArrayList<Item> items) {
        failedItems = items;
    }

    // MODIFIES: this
    // EFFECTS:  adds item to list of incomplete items
    public void addItem(Item newItem) throws DuplicateItemException {
        for (Item i : allItems()) {
            if (i.getName().equals(newItem.getName())) {
                throw new DuplicateItemException("You've already added this item!");
            }
        }
        incompleteItems.add(newItem);
    }

    // REQUIRES: item must exist in 1 of the 4 item lists
    // EFFECTS:  returns the list containing the specified item
    public ArrayList<Item> findItemList(Item item) {
        if (incompleteItems.contains(item)) {
            return incompleteItems;
        } else if (procrastinatedItems.contains(item)) {
            return procrastinatedItems;
        } else if (completedItems.contains(item)) {
            return completedItems;
        } else {
            return failedItems;
        }
    }

    // MODIFIES: this
    // EFFECTS:  moves item from from its original list to a new list
    public void moveItem(Status status, Item item) {
        ArrayList<Item> itemList = findItemList(item);
        itemList.remove(item);
        switch (status) {
            case PROCRASTINATED:
                procrastinatedItems.add(item);
                item.procrastinate();
                break;
            case COMPLETED:
                completedItems.add(item);
                item.complete();
                break;
            case FAILED:
                failedItems.add(item);
                item.fail();
                break;
            default:
                incompleteItems.add(item);
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS:  removes all one-time items from incompleteItems list, clears elements from incompleteItems or
    //           failedItems if list size > 20
    public void reset() {
        for (Item i : allItems()) {
            if (!i.isRecurring() && !i.getStatus().equals(Status.COMPLETED)) {
                moveItem(Status.FAILED, i);
            } else if (!i.isRecurring()) {
                moveItem(Status.COMPLETED,i);
            } else {
                moveItem(Status.INCOMPLETE, i);
            }
        }
    }

    // EFFECTS: returns items in all 4 list types
    public ArrayList<Item> allItems() {
        ArrayList<Item> allItems = new ArrayList<>();
        allItems.addAll(incompleteItems);
        allItems.addAll(completedItems);
        allItems.addAll(procrastinatedItems);
        return allItems;
    }

    // EFFECTS: translates ArrayList<Item> to JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("pug name", pugName);
        json.put("incomplete", listToJson(incompleteItems));
        json.put("completed", listToJson(completedItems));
        json.put("procrastinated", listToJson(procrastinatedItems));
        json.put("failed", listToJson(failedItems));
        return json;
    }

    // modified from: JSONSerializerDemo
    //           URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // EFFECTS: returns items in ItemList as a JSON array
    public JSONArray listToJson(ArrayList<Item> listType) {
        JSONArray jsonArray = new JSONArray();
        for (Item i : listType) {
            jsonArray.put(i.toJson());
        }
        return jsonArray;
    }
}
