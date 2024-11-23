package model;
/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (netid)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

import java.util.ArrayList;
import java.util.Random;

// Flyweight implementation of Dice
public class Dice {
    private DiceValue face;
    private Random rand = new Random();
    private boolean isHeld = false;

    // private constructor
    private Dice(DiceValue face) {
        this.face = face;
    }

    // static store
    private static ArrayList<Dice> diceArr = new ArrayList<Dice>();

    static {
        for (int i = 0; i < 5; i++) {
        	Dice newDice = new Dice(DiceValue.values()[i]);
        	diceArr.add(i, newDice);
        }
    }

    // static access method
    public static Dice get(int dicePos) {
        assert dicePos > 0 && dicePos < 6;
        ArrayList<Dice> diceArrCopy = new ArrayList<Dice>(diceArr);
        return diceArrCopy.get(dicePos - 1);
    }

    // general functionality methods
    public void roll() {
        if (!isHeld) {
            int randRoll = rand.nextInt(6);
            this.face = DiceValue.values()[randRoll];
        }
    }

    public DiceValue getFace() {
    	return this.face;
    }

    public int getFaceVal() {
        return this.face.getVal();
    }

    public void toggleHold() {
    	isHeld = !isHeld;
    }

    public void setHold(boolean hold) {
        isHeld = hold;
    }

    public boolean isHeld() {
        return isHeld;
    }

}
