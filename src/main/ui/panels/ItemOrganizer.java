package ui.panels;

import model.Item;
import model.ItemList;
import ui.ProcrastopugGUI;

import javax.swing.*;
import java.awt.*;

// instantiates and modifies static JList and DefaultListModel lists
public class ItemOrganizer extends JPanel {
    private ProcrastopugGUI gui;
    private static JList<Item> incompleteItems;
    private static JList<Item> procrastinatedItems;
    private static JList<Item> completedItems;
    private static JList<Item> failedItems;
    private static DefaultListModel<Item> incompleteList;
    private static DefaultListModel<Item> procrastinatedList;
    private static DefaultListModel<Item> completedList;
    private static DefaultListModel<Item> failedList;

    // EFFECTS: instantiates and modifies static JList and DefaultListModel lists
    public ItemOrganizer(ProcrastopugGUI gui) {
        super(new BorderLayout());
        this.gui = gui;

        // incomplete items
        setIncompleteList(new DefaultListModel<>());
        setIncompleteItems(new JList<>(getIncompleteList()));

        // procrastinated items
        setProcrastinatedList(new DefaultListModel<>());
        setProcrastinatedItems(new JList<>(getProcrastinatedList()));

        // completed items
        setCompletedList(new DefaultListModel<>());
        setCompletedItems(new JList<>(getCompletedList()));

        // failed items
        setFailedList(new DefaultListModel<>());
        setFailedItems(new JList<>(getFailedList()));
    }

    // getters
    public ProcrastopugGUI getGUI() {
        return gui;
    }

    public ItemList getErrands() {
        return gui.getErrands();
    }

    public static DefaultListModel<Item> getFailedList() {
        return failedList;
    }

    public static DefaultListModel<Item> getCompletedList() {
        return completedList;
    }

    public static DefaultListModel<Item> getIncompleteList() {
        return incompleteList;
    }

    public static DefaultListModel<Item> getProcrastinatedList() {
        return procrastinatedList;
    }

    public static JList<Item> getFailedItems() {
        return failedItems;
    }

    public static JList<Item> getCompletedItems() {
        return completedItems;
    }

    public static JList<Item> getIncompleteItems() {
        return incompleteItems;
    }

    public static JList<Item> getProcrastinatedItems() {
        return procrastinatedItems;
    }

    // setters
    public static void setIncompleteItems(JList<Item> incompleteItems) {
        ItemOrganizer.incompleteItems = incompleteItems;
    }

    public static void setProcrastinatedItems(JList<Item> procrastinatedItems) {
        ItemOrganizer.procrastinatedItems = procrastinatedItems;
    }

    public static void setCompletedItems(JList<Item> completedItems) {
        ItemOrganizer.completedItems = completedItems;
    }

    public static void setFailedItems(JList<Item> failedItems) {
        ItemOrganizer.failedItems = failedItems;
    }

    public static void setIncompleteList(DefaultListModel<Item> incompleteList) {
        ItemOrganizer.incompleteList = incompleteList;
    }

    public static void setProcrastinatedList(DefaultListModel<Item> procrastinatedList) {
        ItemOrganizer.procrastinatedList = procrastinatedList;
    }

    public static void setCompletedList(DefaultListModel<Item> completedList) {
        ItemOrganizer.completedList = completedList;
    }

    public static void setFailedList(DefaultListModel<Item> failedList) {
        ItemOrganizer.failedList = failedList;
    }

    // MODIFIES: this
    // EFFECTS: removes an item from the static incomplete list
    public static void removeFromIncompleteList(Item item) {
        ItemOrganizer.incompleteList.removeElement(item);
    }

    // MODIFIES: this
    // EFFECTS: removes an item from the static procrastinated list
    public static void removeFromProcrastinatedList(Item item) {
        ItemOrganizer.procrastinatedList.removeElement(item);
    }

    // MODIFIES: this
    // EFFECTS: removes an item from the static completed list
    public static void removeFromCompletedList(Item item) {
        ItemOrganizer.completedList.removeElement(item);
    }

    // MODIFIES: this
    // EFFECTS: removes an item from the static failed list
    public static void removeFromFailedList(Item item) {
        ItemOrganizer.failedList.removeElement(item);
    }

    // MODIFIES: this
    // EFFECTS: adds an item from the static incomplete list
    public static void addToIncompleteList(Item item) {
        ItemOrganizer.incompleteList.addElement(item);
    }

    // MODIFIES: this
    // EFFECTS: adds an item from the static procrastinated list
    public static void addToProcrastinatedList(Item item) {
        ItemOrganizer.procrastinatedList.addElement(item);
    }

    // MODIFIES: this
    // EFFECTS: adds an item from the static completed list
    public static void addToCompletedList(Item item) {
        ItemOrganizer.completedList.addElement(item);
    }

    // MODIFIES: this
    // EFFECTS: adds an item from the static failed list
    public static void addToFailedList(Item item) {
        ItemOrganizer.failedList.addElement(item);
    }
}
