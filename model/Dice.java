package model;
/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (netid)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

import java.util.Random;

// Flyweight implementation of Dice
public class Dice {
    private DiceValue face = ;
    private Random rand;
    private boolean isHeld = false;
    
    // private constructor
    private Dice(DiceValue face) {
        this.face = face;
    }

    // static store
    private static Dice[] diceArr = new Dice[5];
    
    static {
        for (int i = 0; i < diceArr.length; i++) {
            diceArr[i] = new Dice(DiceValue.values()[i]);
        }
    }

    // static access method
    public static Dice get(int dicePos) {
        assert dicePos >= 1 && dicePos <= 6;
        return diceArr[dicePos - 1];
    }
    
    // general functionality methods
    public void roll() {
        if (isHeld == false) {
            int randRoll = rand.nextInt(6);
            this.face = DiceValue.values()[randRoll];
        }
    }

    public DiceValue getFace() {
        return this.face;
    }

    public void setHold(boolean hold) {
        isHeld = hold;
    }

    public boolean getHold() {
        return this.isHeld;
    }

    public boolean isHeld() {
        return isHeld; 
    }
    
}
