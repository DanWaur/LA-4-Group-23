/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (netid)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

import java.util.Random;

// Flyweight implementation of Dice
public class Dice {
    private DiceValue face;
    private Random rand;
    
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
        assert dicePos > 0 && dicePos < 6;
        return diceArr[dicePos];
    }
    
    public void roll() {
        int randRoll = rand.nextInt(7);
        this.face = DiceValue.values()[randRoll];
    }

    public DiceValue getFace() {
        return this.face;
    }
    
}
