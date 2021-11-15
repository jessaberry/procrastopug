package ui;

import exceptions.DuplicateItemException;
import model.*;
import ui.panels.*;
import javax.swing.*;
import java.awt.*;

// main frame
public class ProcrastopugGUI extends JFrame {
    private Pug pug;
    private ImagePanel pugImage;
    private PersistencePanel pugStats;
    private ItemOrganizer pugItems;
    private ItemList errands;

    // EFFECTS: runs procrastopug GUI
    public static void main(String[] args) {
        new ProcrastopugGUI();
    }

    // https://docs.oracle.com/javase/tutorial/uiswing/components/toplevel.html#general
    // EFFECTS: creates a GUI for procrastopug
    public ProcrastopugGUI() {
        super("Procrastopug");
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        addItem();
        errands = new ItemList(pug.getPugName());
        pugImage = new ImagePanel(this);
        pugItems = new ItemOrganizer(this);
        pugStats = new PersistencePanel(this);

        ItemPanelLeft pugItemsLeft = new ItemPanelLeft(pugItems);
        ItemPanelRight pugItemsRight = new ItemPanelRight(pugItems);

        contentPane.add(pugImage, BorderLayout.CENTER);
        contentPane.add(pugItemsLeft, BorderLayout.WEST);
        contentPane.add(pugItemsRight, BorderLayout.EAST);
        contentPane.add(pugStats, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // MODIFIES: ItemPanel
    // EFFECTS: displays a pop-up that saves the name of the pug, a header label, and adds the top panel to the
    //          content pane
    private void addItem() {
        pug = new Pug(initPugName(new JFrame()));
        JLabel header = new JLabel(pug.getPugName() + " is feeling " + getPugState() + " right now.");
        header.setHorizontalAlignment(JLabel.CENTER);

        JButton addButton = new JButton("Add a new item");
        addButton.addActionListener(evt -> newItem(new JFrame()));

        JPanel panel = new JPanel();
        panel.add(Box.createVerticalGlue());
        panel.add(header);
        panel.add(addButton);
        add(panel,BorderLayout.NORTH);
    }

    // MODIFIES: this, itemPanel
    // EFFECTS: adds the pop-up's item to incompleteList in ItemPanel
    // adds a replacement item if the original item cannot be added
    public void newItem(JFrame frame) {
        ImageIcon happyIcon = scaleIcon("happy.png");
        ImageIcon sadIcon = scaleIcon("devastated.png");

        Object[] options = {"Recurring", "One-time", "Cancel"};
        int result = JOptionPane.showOptionDialog(frame,
                "\n First, select the type of item you'd like to add:\n", "Add a new item",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                happyIcon, options, "");

        if (result == JOptionPane.OK_OPTION) {
            try {
                addNewItem(frame, happyIcon, 0);
            } catch (DuplicateItemException e) {
                e.printStackTrace();
            }
        } else if (result == JOptionPane.YES_NO_CANCEL_OPTION) {
            try {
                addNewItem(frame, happyIcon, 1);
            } catch (DuplicateItemException e) {
                e.printStackTrace();
            }
        } else {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        // System.out.println(itemName + " was successfully added.");
    }

    // EFFECTS: create a new item and adds it to the incomplete list
    // Throws a DuplicateItemException if an item with the same name exists in Procrastopug
    private void addNewItem(JFrame frame, ImageIcon sadIcon, int itemType) throws DuplicateItemException {
        Item i = null;
        String itemName = initItem(frame, sadIcon);
        if (itemType == 0) {
            i = new RecurringItem(itemName);
        } else if (itemType == 1) {
            i = new OneTimeItem(itemName);
        }
        if (ItemOrganizer.getIncompleteList().contains(i) || ItemOrganizer.getCompletedList().contains(i)
                || ItemOrganizer.getProcrastinatedList().contains(i) || ItemOrganizer.getFailedList().contains(i)) {
            throw new DuplicateItemException("An item named " + itemName + " was previously added!");
        } else {
            ItemOrganizer.getIncompleteList().addElement(i);
        }
    }

    // EFFECTS: shows the menu for adding an item with different item name
    private String initItem(JFrame frame, ImageIcon sadIcon) {
        return (String) JOptionPane.showInputDialog(frame,
                "Enter the name of your new item here:",
                "Add a new item", JOptionPane.PLAIN_MESSAGE, sadIcon, null,
                "drown in sadness");
    }

    // EFFECTS: returns the ItemList errands
    public ItemList getErrands() {
        return errands;
    }

    // EFFECTS: sets the state (mental wellness) of the pug as a string
    public String getPugState() {
        if (pug.getHappiness() == 5) {
            return "lonely";
        } else if (pug.getHappiness() > 5) {
            return "happy";
        } else {
            return "devastated";
        }
    }

    // https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
    // EFFECTS: returns the pug name from a pop-up
    private String initPugName(JFrame frame) {
        ImageIcon icon = scaleIcon("happy.png");
        String s = (String) JOptionPane.showInputDialog(frame,
                "\n What is your pug's name?\n", "Welcome to Procrastopug!",
                JOptionPane.PLAIN_MESSAGE, icon, null,
                "Taco");
        if ((s != null) && (s.length() > 0)) {
            return s;
        } else {
            return "Taco";
        }
    }

    // source: https://www.codejava.net/java-se/graphics/how-to-resize-images-in-java
    // MODIFIES: this
    // EFFECTS: resizes the pug icon used in pop-ups
    public ImageIcon scaleIcon(String s) {
        String dir = System.getProperty("user.dir");
        String sep = System.getProperty("file.separator");
        ImageIcon img = new ImageIcon(dir + sep + "files" + sep + s);
        Image scaled = img.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // EFFECTS: adds the pug image to the frame
    public void addPug() {
        pugImage = new ImagePanel(this);
        add(pugImage, BorderLayout.CENTER);
    }

    // EFFECTS: makes image return the frame
    public void drawPug() {
        pugImage.changeMood(pugImage.getMood());
        validate();
        repaint();
    }

    // EFFECTS: gets this pug
    public Pug getPug() {
        return pug;
    }

    // EFFECTS: sets errands
    public void setErrands(ItemList errands) {
        this.errands = errands;
    }
}
