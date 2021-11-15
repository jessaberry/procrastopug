package ui.panels;

import model.Item;
import model.ItemList;
import model.Pug;
import model.Status;
import ui.ProcrastopugGUI;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

// creates 2 panels (for completed and failed items) on the right side of pug image
public class ItemPanelRight extends JPanel {
    private final ItemOrganizer organizer;

    // EFFECTS: creates 2 panels (for completed and failed items) on the right side of pug image
    public ItemPanelRight(ItemOrganizer organizer) {
        super(new BorderLayout());
        this.organizer = organizer;

        JList<Item> completedItems = ItemOrganizer.getCompletedItems();
        JList<Item> failedItems = ItemOrganizer.getFailedItems();

        makePanel(completedItems, "Completed", BorderLayout.LINE_START);
        makePanel(failedItems, "Failed", BorderLayout.CENTER);
        makeBottomButton();
        setVisible(true);
    }

    // EFFECTS: initializes a generic item panel with a JScrollPane for Completed and Failed items
    public void makePanel(JList<Item> items, String title, String location) {
        JLabel label = new JLabel(title, JLabel.CENTER);
        items.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        items.setVisibleRowCount(10);

        JScrollPane scrollPane = new JScrollPane(items);
        scrollPane.setBorder(new CompoundBorder(new TitledBorder(
                new EmptyBorder(0,0,10,0), title), scrollPane.getBorder()));
        scrollPane.setPreferredSize(new Dimension(200,400));

        JPanel panel = new JPanel();
        panel.add(scrollPane);
        panel.revalidate();
        panel.repaint();
        add(panel, location);

        panel.revalidate();
        panel.repaint();
        setVisible(true);
    }

    // Source: http://www.iitk.ac.in/esc101/05Aug/tutorial/uiswing/layout/box.html
    // EFFECTS: makes panel for 'End the day' button, and implements the action listener to reset
    //          when YES is pressed, all non-completed one-time items will move to the 'Failed' panel, whereas
    //          all non-completed recurring items will move to the 'Incomplete' panel
    public void makeBottomButton() {
        Frame frame = new JFrame();
        ImageIcon icon = organizer.getGUI().scaleIcon("happy.png");
        JButton endButton = new JButton("End the day");
        endButton.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog(frame, "If you end your day right now, "
                    + "all one-time items that haven't been completed will be permanently failed! "
                    + "\nAre you sure you want to continue?","WARNING!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, icon);
            if (dialogResult == JOptionPane.YES_OPTION) {
                System.out.println("Your items have been reset for the day.");
                organizer.getGUI().getPug().sleep();
                reset(ItemOrganizer.getIncompleteItems());
                reset(ItemOrganizer.getCompletedItems());
                reset(ItemOrganizer.getProcrastinatedItems());
                reset(ItemOrganizer.getFailedItems());
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(endButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: ItemList, ItemPanel
    // EFFECTS: resets the status of recurring items to incomplete, and one-time items to completed or failed.
    private void reset(JList<Item> itemList) {
        int size = itemList.getModel().getSize();
        for (int i = size - 1; i >= 0; i--) {
            Item item = itemList.getModel().getElementAt(i);
            if (!item.getStatus().equals(Status.COMPLETED)) {
                if (!item.isRecurring()) {
                    organizer.getErrands().moveItem(Status.FAILED, item);
                    resetPanel(getInitialList(item), 3, item);
                } else {
                    organizer.getErrands().moveItem(Status.INCOMPLETE, item);
                    resetPanel(getInitialList(item), 0, item);
                }
            } else {
                if (item.isRecurring()) {
                    organizer.getErrands().moveItem(Status.INCOMPLETE, item);
                    resetPanel(getInitialList(item), 0, item);
                } else {
                    organizer.getErrands().moveItem(Status.COMPLETED, item);
                    resetPanel(getInitialList(item), 1, item);
                }
            }
        }
    }

    // EFFECTS: adjusts JPanels according to where they belong post-reset, and calls the 3 helper functions below
    private void resetPanel(int initialList, int finalList, Item select) {
        removeFromList(initialList, select);
        addToList(finalList, select);
        repaint();
        revalidate();
    }

    // MODIFIES: ItemPanel lists
    // EFFECTS: adds selected item into list
    private void addToList(int finalList, Item select) {
        switch (finalList) {
            case 3:
                ItemOrganizer.addToFailedList(select);
                break;
            case 2:
                ItemOrganizer.addToProcrastinatedList(select);
                break;
            case 1:
                ItemOrganizer.addToCompletedList(select);
                break;
            default:
                ItemOrganizer.addToIncompleteList(select);
                break;
        }
    }

    // MODIFIES: ItemPanel lists
    // EFFECTS: removes selected item from list
    private void removeFromList(int initialList, Item select) {
        switch (initialList) {
            case 0:
                ItemOrganizer.removeFromIncompleteList(select);
                break;
            case 1:
                ItemOrganizer.removeFromCompletedList(select);
                break;
            case 2:
                ItemOrganizer.removeFromProcrastinatedList(select);
                break;
            default:
                ItemOrganizer.removeFromFailedList(select);
                break;
        }
    }

    // EFFECTS: returns an int representing the DefaultListModel that the list is in
    private int getInitialList(Item item) {
        int model = 0; // represents incomplete items
        if (ItemOrganizer.getCompletedList().contains(item)) {
            model = 1;
        } else if (ItemOrganizer.getProcrastinatedList().contains(item)) {
            model = 2;
        } else if (ItemOrganizer.getFailedList().contains(item)) {
            model = 3;
        }
        return model;
    }
}
