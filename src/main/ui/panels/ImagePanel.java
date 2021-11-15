package ui.panels;

import ui.ProcrastopugGUI;
import javax.swing.*;
import java.awt.*;

// creates the pug image in the middle of the app
public class ImagePanel extends JPanel {
    private static final int PUG_WIDTH = 400;
    private static final int PUG_HEIGHT = 400;
    private String currentMood;
    private ImageIcon happyImage;
    private ImageIcon devastatedImage;
    private ImageIcon mediocreImage;
    private final JPanel pugPanel;
    private JLabel pugLabel;

    // CREDITS: TrafficLightStarter for pugComboBox inspiration
    // EFFECTS: creates a pugComboBox that represents a pug
    public ImagePanel(ProcrastopugGUI gui) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String[] mood = {"happy", "devastated", "mediocre"};
        JComboBox<String> pugComboBox = new JComboBox<>(mood);
        pugComboBox.addActionListener(e -> {
            gui.addPug();
            gui.drawPug();
        });
        add(pugComboBox);
        pugComboBox.setVisible(false);

        pugPanel = new JPanel();
        pugPanel.setPreferredSize(new Dimension(PUG_WIDTH,PUG_HEIGHT));
        add(pugPanel);

        loadPugImages();
        changeMood(gui.getPugState());
    }

    // returns the mood of the pug
    public String getMood() {
        return currentMood;
    }

    // MODIFIES: this
    // EFFECTS: sets the mood of procrastopug to match chosen mood
    public void changeMood(String mood) {
        switch (mood) {
            case "happy":
                setHappyMood();
                break;
            case "devastated":
                setDevastatedMood();
                break;
            default:
                setMediocreMood();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the mood of procrastopug to happy
    public void setHappyMood() {
        removeExistingPugImage();
        pugLabel = new JLabel(happyImage);
        pugPanel.add(pugLabel);
        currentMood = "happy";
    }

    // MODIFIES: this
    // EFFECTS: sets the mood of procrastopug to devastated
    public void setDevastatedMood() {
        removeExistingPugImage();
        pugLabel = new JLabel(devastatedImage);
        pugPanel.add(pugLabel);
        currentMood = "devastated";
    }

    // MODIFIES: this
    // EFFECTS: sets the mood of procrastopug to mediocre
    public void setMediocreMood() {
        removeExistingPugImage();
        pugLabel = new JLabel(mediocreImage);
        pugPanel.add(pugLabel);
        currentMood = "mediocre";
    }

    // SOURCE OF IMAGES: https://www.stickpng.com/img/
    // EFFECTS: loads the pug images into JPanel
    private void loadPugImages() {
        String dir = System.getProperty("user.dir");
        String sep = System.getProperty("file.separator");
        happyImage = scale(new ImageIcon(dir + sep + "files" + sep + "happy.png"));
        devastatedImage = scale(new ImageIcon(dir + sep + "files" + sep + "devastated.png"));
        mediocreImage = scale(new ImageIcon(dir + sep + "files" + sep + "mediocre.jpg"));
    }

    // MODIFIES: this
    // EFFECTS: if there is an existing pug image in the JPanel, remove it, else do nothing
    private void removeExistingPugImage() {
        if (pugLabel != null) {
            pugPanel.remove(pugLabel);
        }
    }

    // source: https://www.codejava.net/java-se/graphics/how-to-resize-images-in-java
    // MODIFIES: this
    // EFFECTS: resizes an image to the dimensions that best fit the app
    public ImageIcon scale(ImageIcon img) {
        Image image = img.getImage();
        Image scaled = image.getScaledInstance(600, 400, Image.SCALE_SMOOTH);
        img = new ImageIcon(scaled);
        return img;
    }
}
