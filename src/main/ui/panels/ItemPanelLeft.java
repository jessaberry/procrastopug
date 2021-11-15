package ui.panels;

import model.*;
import ui.ProcrastopugGUI;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// creates 2 panels (for incomplete and procrastinated items) on the left side of pug image
public class ItemPanelLeft extends JPanel {
    private ItemOrganizer organizer;

    // EFFECTS: creates 2 panels (for incomplete and procrastinated items) on the left side of pug image
    public ItemPanelLeft(ItemOrganizer organizer) {
        super(new BorderLayout());
        this.organizer = organizer;

        JList<Item> incompleteItems = ItemOrganizer.getIncompleteItems();
        JList<Item> procrastinatedItems = ItemOrganizer.getProcrastinatedItems();

        makePanel(incompleteItems, "Incomplete", BorderLayout.LINE_START);
        makePanel(procrastinatedItems, "Procrastinated", BorderLayout.CENTER);
        makeBottomButtons();
        setVisible(true);
    }

    // EFFECTS: initializes a generic item panel with a JScrollPane for Incomplete and Procrastinated items
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
    // EFFECTS: makes panel for 'Procrastinate' and 'Complete' buttons
    public void makeBottomButtons() {
        JButton procrastinateButton = implementButton("Complete", ItemOrganizer.getCompletedList());
        JButton completeButton = implementButton("Procrastinate", ItemOrganizer.getProcrastinatedList());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(procrastinateButton);
        buttonPanel.add(completeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: ItemList errands, ItemPanel lists
    // EFFECTS: adds action listener for Procrastinate and Complete buttons, and
    //          moves the mouse-selected item from Incomplete panel to desired panel
    public JButton implementButton(String s, DefaultListModel<Item> list) {
        JButton button = new JButton(s);
        button.addActionListener(e -> {
            Item select = getItem();
            list.addElement(select);
            if (s.equals("Procrastinate")) {
                playSound("bad.wav"); // Source: Robinhood76 https://freesound.org/people/Robinhood76/sounds/334914/
                organizer.getErrands().moveItem(Status.PROCRASTINATED, select);
                repaint();
                revalidate();
            } else if (s.equals("Complete")) {
                playSound("good.wav"); // Source: Iluiset7 https://freesound.org/people/lluiset7/sounds/141334/
                organizer.getErrands().moveItem(Status.COMPLETED, select);
                repaint();
                revalidate();
            }
            System.out.println(select.getName() + " was " + select.getStatus() + ".");
        });
        return button;
    }

    // EFFECTS: retrieves the item selected by a mouse and removes it from its initial list
    private Item getItem() {
        Item select = ItemOrganizer.getIncompleteItems().getSelectedValue();
        if (select == null) {
            select = ItemOrganizer.getProcrastinatedItems().getSelectedValue();
            if (select == null) {
                throw new NullPointerException("You've already completed this item! Move on.");
            }
            ItemOrganizer.getProcrastinatedList().removeElement(select);
        }
        ItemOrganizer.getIncompleteList().removeElement(select);
        return select;
    }

    // SOURCE: http://suavesnippets.blogspot.com/2011/06/add-sound-on-jbutton-click-in-java.html
    // EFFECTS: plays sound on button click
    //          plays a positive sound when 'Completed' is clicked, and negative sound when 'Procrastinated' is clicked
    public void playSound(String sound) {
        String dir = System.getProperty("user.dir");
        String sep = System.getProperty("file.separator");
        try {
            AudioInputStream input =
                    AudioSystem.getAudioInputStream(new File(dir + sep + "files" + sep + sound));
            Clip clip = AudioSystem.getClip();
            clip.open(input);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
