package model;

public class Pug {
    private String pugName;
    private int treats;
    private boolean walk;  // set to true if person completes a daily exercise
    private boolean play;  // set to true if person drinks water
    private int happiness;

    // EFFECTS: creates a new pug with name pugName; once awake, the pug needs to go for a walk and played with;
    //          on the happiness scale, the pug feels neutral after waking up
    public Pug(String pugName) {
        this.pugName = pugName;
        walk = false;
        play = false;
        happiness = 5;
        treats = 0;
    }

    public String getPugName() {
        return pugName;
    }

    public int getTreats() {
        return treats;
    }

    public int getHappiness() {
        return happiness;
    }

    public boolean hasWalked() {
        return walk;
    }

    public boolean hasPlayed() {
        return play;
    }

    // MODIFIES: this
    // EFFECTS:  if the person completes a recurring item on their list, the pug gets to go for a walk or play with toy
    public void essentialActions(String action) {
        if (action.equals("walk")) {
            walk = true;
        } else {
            play = true;
        }
        happiness++;
    }

    // MODIFIES: this
    // EFFECTS:  if the person completes a one-time item on their list, the pug is fed a treat
    //           and his happiness meter goes up by 1 unit
    public void giveTreat() {
        treats++;
        happiness++;
    }

    // MODIFIES: this
    // EFFECTS: resets the pug's state
    public void sleep() {
        treats = 0;
        happiness = 5;
        walk = false;
        play = false;
    }
}
