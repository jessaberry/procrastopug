package ui.panels;

import exceptions.DuplicateItemException;
import model.Item;
import model.ItemList;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.ProcrastopugGUI;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// panel where the user can save and store a file
public class PersistencePanel extends JPanel {
    private final ProcrastopugGUI procrastopug;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private static final String JSON_STORE = "./data/procrastopug.json";

    // EFFECTS: creates save/load button & implements them
    public PersistencePanel(ProcrastopugGUI gui) {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        procrastopug = gui;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel persistenceLabel = new JLabel("Load or save your progress below:", JLabel.CENTER);
        add(persistenceLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loadButton());
        buttonPanel.add(saveButton());
        add(buttonPanel,BorderLayout.WEST);
    }

    // source: https://www.codejava.net/java-se/swing/jbutton-basic-tutorial-and-examples
    // EFFECTS: creates a button to save data using JSON framework
    private JButton saveButton() {
        JButton saveButton = new JButton("Save to file");
        saveButton.addActionListener(evt -> {
            save(); // saves info into gui
        });
        return saveButton;
    }

    // source: https://www.codejava.net/java-se/swing/jbutton-basic-tutorial-and-examples
    // EFFECTS: creates a button to load data using JSON framework
    private JButton loadButton() {
        JButton loadButton = new JButton("Load from file");
        loadButton.addActionListener(evt -> {
            try {
                load(); // loads info from gui
            } catch (IOException | DuplicateItemException e) {
                e.printStackTrace();
            }
        });
        return loadButton;
    }

    // MODIFIES: ItemPanel
    // EFFECTS: loads ItemList errands from file, exception is thrown if the pug of given name is not found
    public void load() throws IOException, DuplicateItemException {
        ItemList errands = jsonReader.read();
        procrastopug.setErrands(errands);
        for (Item i: errands.getIncompleteItems()) {
            ItemOrganizer.getIncompleteList().addElement(i);
        }
        for (Item i: errands.getProcrastinatedItems()) {
            ItemOrganizer.getProcrastinatedList().addElement(i);
        }
        for (Item i: errands.getCompletedItems()) {
            ItemOrganizer.getCompletedList().addElement(i);
        }
        for (Item i: errands.getFailedItems()) {
            ItemOrganizer.getFailedList().addElement(i);
        }
        System.out.println("Loaded Taco's records from " + JSON_STORE);
    }

    // MODIFIES: errands
    // EFFECTS: saves ItemList errands to file
    public void save() {
        procrastopug.getErrands().setIncompleteItems(store(ItemOrganizer.getIncompleteItems()));
        procrastopug.getErrands().setProcrastinatedItems(store(ItemOrganizer.getProcrastinatedItems()));
        procrastopug.getErrands().setCompletedItems(store(ItemOrganizer.getCompletedItems()));
        procrastopug.getErrands().setFailedItems(store(ItemOrganizer.getFailedItems()));

        try {
            jsonWriter.open();
            jsonWriter.write(procrastopug.getErrands());
            jsonWriter.close();
            System.out.println("Saved Taco's records to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write " + JSON_STORE + "to file.");
        }
    }

    // EFFECTS: transforms JList<Item> into ArrayList<Item>
    public ArrayList<Item> store(JList<Item> itemList) {
        ArrayList<Item> errands = new ArrayList<>();
        int size = itemList.getModel().getSize();
        for (int i = 0; i < size; i++) {
            Item item = itemList.getModel().getElementAt(i);
            errands.add(item);
        }
        return errands;
    }
}
