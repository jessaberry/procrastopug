package model;

public class OneTimeItem extends Item {
    // EFFECTS: creates a one-time item
    public OneTimeItem(String itemName) {
        super(itemName,false);
    }

    // EFFECTS: resets the ItemList, and the state of OneTimeItem does not change.
    public void reset() {
        // nothing occurs here; a one-time item does not reset to status incomplete
    }
}
