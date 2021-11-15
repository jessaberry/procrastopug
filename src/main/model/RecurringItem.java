package model;

public class RecurringItem extends Item {
    // EFFECTS: creates a recurring item
    public RecurringItem(String itemName) {
        super(itemName,true);
    }

    // resets the ItemList, and the state of RecurringItem is affected (unlike OneTimeItem)
    // EFFECTS: sets the value of status to incomplete
    @Override
    public void reset() {
        status = Status.INCOMPLETE;
    }
}
