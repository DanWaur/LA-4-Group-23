/*
* Juan Rogel Acedo (jarogelacedo)
* Daniel (netid)
* Marco (pena8)
* Devin Dinh (devdinh)
*/

package model;
import java.util.ArrayList;
import java.util.Random;

/* Flyweight implementation of Dice */
public class Dice {
    private DiceValue face;
    private Random rand = new Random();
    private boolean isHeld = false;

    /* private constructor */
    private Dice(DiceValue face) {
        this.face = face;
    }

    /* static store */
    private static ArrayList<Dice> diceArr = new ArrayList<Dice>();

    static {
        /* create exactly 5 dice for the game board */
        for (int i = 0; i < 5; i++) {
        	Dice newDice = new Dice(DiceValue.values()[i]);
        	diceArr.add(i, newDice);
        }
    }

    /*
     * static access method
     * @param dicePos - position of dice in array in the game board
     * @pre dicePos >= 1 || dicePos <= 5
     */
    public static Dice get(int dicePos) {
        assert dicePos > 0 && dicePos < 6;
        ArrayList<Dice> diceArrCopy = new ArrayList<Dice>(diceArr);
        return diceArrCopy.get(dicePos - 1);
    }


    /* general functionality methods */

    /* 
     * as long as dice is NOT held
     * changes current face value to new value 
     * (values can be repeated)
     */
    public void roll() {
        if (!isHeld) {
            int randRoll = rand.nextInt(6);
            this.face = DiceValue.values()[randRoll];
        }
    }

    /* @return the current face enum value */
    public DiceValue getFace() {
    	return this.face;
    }

    /* @return the corresponding integer value to the current face enum */
    public int getFaceVal() {
        return this.face.getVal();
    }

    /* switches the isHeld to the opposite boolean */
    public void toggleHold() {
    	isHeld = !isHeld;
    }

    /* @param hold - new boolean to assign to isHeld */
    public void setHold(boolean hold) {
        isHeld = hold;
    }

    /* @return the current isHeld boolean */
    public boolean isHeld() {
        return isHeld;
    }

}
