/*
 * Juan Rogel Acedo (jarogelacedo)
 * Name (netid)
 * Name (netid)
 * Name (netid)
 */
import java.util.Random;

public class Dice{
    private int value;
    private boolean isHeld;
    private static final Random random = new Random();
    
    public Dice(){
        this.value = 1;
        this.isHeld = false;
    }

    public void roll(){
        this.value = random.nextInt(6) + 1;
    }

    /*
     * @return the value of the dice
     */
    public int getValue(){
        return this.value;
    }

    /*
     * @return true if the dice is held, false otherwise
     */
    public boolean isHeld(){
        return this.isHeld;
    }   

    /*
     * @param hold true if the dice is held, false otherwise
     */
    public void setHeld(boolean hold){
        this.isHeld = hold;
    }
}
