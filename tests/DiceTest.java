/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (dreynaldo)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

package tests;
import model.Dice;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class DiceTest {
    @Test
    public void testGetDice() {
    	for (int i = 0; i < 5; i++) {
    		Dice tempDice = Dice.get(i);
    		assertTrue(tempDice.getFaceVal() >= 1);
    		assertTrue(tempDice.getFaceVal() <= 6);
    	}
    }

	@Test
	public void testHold() {
		Dice testDice = Dice.get(1);
		assertFalse(testDice.isHeld());

		testDice.toggleHold();
		assertTrue(testDice.isHeld());

		testDice.setHold(false);
		assertFalse(testDice.isHeld());

		testDice.setHold(true);
		assertTrue(testDice.isHeld());
	}
}
