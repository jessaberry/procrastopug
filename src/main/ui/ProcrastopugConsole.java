package ui;

import exceptions.DuplicateItemException;
import model.*;
import persistence.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ProcrastopugConsole {
    private Pug pug;
    private ItemList errands;
    private static final String JSON_STORE = "./data/procrastopug.json";
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public static void main(String[] args) {
        new ProcrastopugConsole();
    }

    // MODIFIES: this
    // EFFECTS: initializes a pug
    public ProcrastopugConsole() {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runPugCommands();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runPugCommands() {
        String command;
        System.out.println("\n Welcome to Procrastopug!");
        System.out.println("\n What is your pug's name?\n");
        pug = new Pug(input.next());
        errands = new ItemList(pug.getPugName());
        while (true) {
            viewPugMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("q")) {
                break;
            } else {
                interactWithPug(command);
            }
        }
        System.out.println("\n" + String.format("%0" + errands.getCompletedItems().size()
                + "d", 0).replace("0", "Bark! "));
    }

    // EFFECTS: display menu of options to user
    private void viewPugMenu() {
        System.out.println("\nSelect one of the following options:");
        System.out.println("\ta -> add an errand you need to work on");
        System.out.println("\tv -> view your errands");
        System.out.println("\tu -> update the status of an errand");
        System.out.println("\tp -> check in on your pug");
        System.out.println("\tf -> finish your day");
        System.out.println("\ts -> save your pug & progress to file");
        System.out.println("\tl -> load your pug & progress from file");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: prompts user to select an action
    private void interactWithPug(String command) {
        switch (command) {
            case "a": addErrand();
                break;
            case "v": viewErrand();
                break;
            case "u": updateErrand();
                break;
            case "p": summary();
                break;
            case "f": resetErrands();
                break;
            case "s": save();
                break;
            case "l": load();
                break;
            default:
                System.out.println("Procrastopug doesn't understand what you're trying to do.");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds an item to a list of errands
    private void addErrand() {
        String errandType = addErrandMenu();
        String errandName;
        switch (errandType) {
            case "r":
                System.out.println("\tEnter the name of your recurring errand:");
                errandName = input.next();
                addItem(errandName, 0);
                System.out.println("\n" + errandName + " has been added to your list of incomplete items!");
                break;
            case "o":
                System.out.println("\tEnter the name of your one-time errand:");
                errandName = input.next();
                addItem(errandName, 1);
                System.out.println("\n" + errandName + " has been added to your list of incomplete items!");
                break;
            case "b":
                break;
            default:
                System.out.println("\nThat option does not exist.");
                addErrand();
                break;
        }
    }

    private void addItem(String errandName, int type) {
        try {
            if (type == 0) {
                errands.addItem(new RecurringItem(errandName));
            } else if (type == 1) {
                errands.addItem(new OneTimeItem(errandName));
            }
        } catch (DuplicateItemException e) {
            System.out.println("You've already added this item before!");
            addErrand();
        }
    }

    // EFFECTS: displays menu for adding an errand
    private String addErrandMenu() {
        System.out.println("\nSelect one of the following options:");
        System.out.println("\tr -> add a recurring errand");
        System.out.println("\to -> add a one-time errand");
        System.out.println("\tb -> return to main menu");
        return input.next();
    }

    // EFFECTS: displays the name and status of every errand
    private void viewErrand() {
        for (Item errand : errands.allItems()) {
            System.out.println("\tName: " + errand.getName()
                    + " Type: " + getType(errand) + " Status: " + errand.getStatus());
        }
    }

    // EFFECTS: returns the type of an errand
    private String getType(Item i) {
        if (i.isRecurring()) {
            return "recurring";
        }
        return "one-time";
    }

    // EFFECTS: moves the errand from its original category to its desired category
    private void moveErrand(String newStatus, Item errand) {
        switch (newStatus) {
            case "p":
                procrastinateErrand(errand);
                System.out.println("\t" + errand.getName() + " has been procrastinated.");
                break;
            case "c":
                completeErrand(errand);
                System.out.println("\t" + errand.getName() + " has been completed!");
                break;
            case "b":
                break;
            default:
                System.out.println("\nThat option does not exist.");
                updateErrand();
                break;
        }
    }

    // REQUIRES: errand name must be equal to the name of an errand in errands
    // MODIFIES: this
    // EFFECTS: moves a specified errand from incomplete to procrastinated / completed
    private void updateErrand() {
        System.out.println("\nEnter the name of the errand you'd like to update:");
        String name = input.next();
        for (Item errand : errands.allItems()) {
            if (errand.getName().equals(name)) {
                String newStatus = updateErrandMenu(errand);
                moveErrand(newStatus,errand);
            }
        }
    }

    // EFFECTS: shows menu of options for updating the status of an errand
    private String updateErrandMenu(Item i) {
        System.out.println("\nSelect one of the following options:");
        System.out.println("\tp -> procrastinate on " + i.getName());
        System.out.println("\tc -> complete " + i.getName());
        System.out.println("\tb -> return to main menu");
        return input.next();
    }

    // REQUIRES: Item i must be in errands
    // MODIFIES: this
    // EFFECTS:  procrastinate on an errand
    private void procrastinateErrand(Item i) {
        errands.moveItem(Status.PROCRASTINATED,i);
    }

    // REQUIRES: Item i must be in errands
    // MODIFIES: this
    // EFFECTS:  complete an errand and provide pug with associated reward (walk or play).
    //           If a 3rd errand is completed, pug is rewarded a treat
    private void completeErrand(Item i) {
        errands.moveItem(Status.COMPLETED,i);
        if (i.isRecurring()) { // recurring case
            if (!pug.hasWalked()) {
                pug.essentialActions("walk");
            } else if (!pug.hasPlayed()) {
                pug.essentialActions("play");
            } else {
                pug.giveTreat();
            }
        } else {
            pug.giveTreat(); // one-time case
        }
    }

    // MODIFIES: this
    // EFFECTS:  resets the statuses of pug and errands, and saves the file
    private void resetErrands() {
        pug.sleep();
        errands.reset();
        System.out.println("\n Status report for " + pug.getPugName() + ": ");
        System.out.println("\t Treats given today: " + pug.getTreats());
        System.out.println("\t Total number of treats given: " + errands.getCompletedItems().size());
        System.out.println(pug.getPugName() + " is going to sleep, and can't wait to see "
                + "you again tomorrow. Goodnight!");
        save();
        // quit the application
    }

    // EFFECTS: prints out the day's summary, resets recurring items to incomplete,
    //          and clears one-time items
    public void summary() {
        numTreats();
        pugHealthUpdate();
        happinessMeter();
    }

    // REQUIRES: totalItems > 0
    // EFFECTS: measures the happiness of the pug in terms of treats given and amount neglected during the day
    private void happinessMeter() {
        // 100% is completedItems == totalItems
        // happiness meter is measured by completedItems  totalItems
        double percentHappy;
        percentHappy = ((double)errands.getCompletedItems().size()) / (double)errands.allItems().size() * 100;
        System.out.println("Because of you, " + pug.getPugName() + " is " + percentHappy + "% happy!");
    }

    // EFFECTS: returns acknowledgement of the number of one-time items completed
    private void numTreats() {
        System.out.println(errands.getCompletedItems().size()  + " treats were given to your pug "
                + pug.getPugName() + " since your care started.");
    }

    // EFFECTS: returns acknowledgement of the number of recurring items completed
    private void pugHealthUpdate() {
        if (pug.hasWalked() && pug.hasPlayed()) {
            System.out.println(pug.getPugName() + " enjoyed an amazing walk and playdate today!");
        }
        if (pug.hasPlayed() && !pug.hasWalked()) {
            System.out.println(pug.getPugName() + " had a wonderful playdate today, but "
                    + pug.getPugName() + " would like to go for a walk tomorrow.");
        }
        if (pug.hasWalked() && !pug.hasPlayed()) {
            System.out.println(pug.getPugName() + " had a wonderful walk today, but "
                    + pug.getPugName() + " would like to go for a playdate tomorrow.");
        } else {
            System.out.println(pug.getPugName() + " is grumpy. " + pug.getPugName()
                    + " didn't get to go for a walk or a playdate today, but hopes they will tomorrow.");
        }
    }

    // code from: JSONSerializerDemo
    //       URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // EFFECTS: saves ItemList errands to file
    private void save() {
        try {
            jsonWriter.open();
            jsonWriter.write(errands);
            jsonWriter.close();
            System.out.println("Saved " + pug.getPugName() + "'s records to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write " + JSON_STORE + "to file.");
        }
    }

    // code from: JSONSerializerDemo
    //       URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // MODIFIES: this
    // EFFECTS: loads ItemList errands from file
    private void load() {
        try {
            errands = jsonReader.read();
            System.out.println("Loaded " + pug.getPugName() + "'s records from " + JSON_STORE);
        } catch (IOException | DuplicateItemException e) {
            System.out.println("Unable to load " + JSON_STORE + "from file.");
        }
    }
}