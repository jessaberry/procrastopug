package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PugTest {
    private Pug pug;

    @BeforeEach
    public void setup() {
        pug = new Pug("Pumpkin Spice Latte");
        assertEquals(pug.getPugName(),"Pumpkin Spice Latte");
    }

    @Test
    public void testEssentialsWalking() {
        pug.essentialActions("walk");
        assertTrue(pug.hasWalked());
    }

    @Test
    public void testEssentialsPlaying() {
        pug.essentialActions("play");
        assertTrue(pug.hasPlayed());
    }

    @Test
    public void testGiveOneTreat() {
        pug.giveTreat();
        assertEquals(pug.getTreats(),1);
        assertEquals(pug.getHappiness(),6);
    }

    @Test
    public void testGiveManyTreat() {
        pug.giveTreat();
        pug.giveTreat();
        assertEquals(pug.getTreats(),2);
        assertEquals(pug.getHappiness(),7);
    }

    @Test
    public void testSleepNoChanges() {
        pug.sleep();
        assertFalse(pug.hasWalked());
        assertFalse(pug.hasPlayed());
        assertEquals(pug.getTreats(),0);
        assertEquals(pug.getHappiness(),5);
    }

    @Test
    public void testSleepChanges() {
        pug.essentialActions("walk");
        pug.essentialActions("play");
        pug.giveTreat();
        assertTrue(pug.hasWalked());
        assertTrue(pug.hasPlayed());
        assertEquals(pug.getTreats(),1);
        assertEquals(pug.getHappiness(),8);

        pug.sleep();

        assertFalse(pug.hasWalked());
        assertFalse(pug.hasPlayed());
        assertEquals(pug.getTreats(),0);
        assertEquals(pug.getHappiness(),5);
    }
}