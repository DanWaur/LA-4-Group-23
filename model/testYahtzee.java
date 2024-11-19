import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;


public class testYahtzee {

	@Test
	public void testDice() {
		Dice dice = new Dice();
		
		assertEquals(1, dice.getValue());
		assertFalse(dice.isHeld());
		
		for (int i = 0; i < 1000; i++) {
			dice.roll();
			assertTrue(dice.getValue()>=1);
			assertTrue(dice.getValue()<=6);
		}
		
		dice.setHeld(true);
		assertTrue(dice.isHeld());
	}
	
	@Test
	public void testScorecardSingles() {
		Scorecard score = new Scorecard();
		
		int[] diceVals = {1, 3, 4, 1, 2};
		
		assertFalse(score.isComplete());
		assertEquals(0, score.getTotalScore());
		
		assertTrue(score.score(ScoreCategory.ONES, diceVals));
		assertEquals(2, score.getTotalScore());
		
		assertTrue(score.score(ScoreCategory.TWOS, diceVals));
		assertEquals(4, score.getTotalScore());
		
		assertTrue(score.score(ScoreCategory.THREES, diceVals));
		assertEquals(7, score.getTotalScore());

		assertTrue(score.score(ScoreCategory.FOURS, diceVals));
		assertEquals(8, score.getTotalScore());

		
	}
	
	
	
}
