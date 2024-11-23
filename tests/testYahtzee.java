package tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import model.Dice;
import model.DiceValue;
import model.ScoreCategory;
import model.Scorecard;


public class testYahtzee {
	
	Scorecard score = new Scorecard();

	@Test
	public void testDice() {
		Dice dice = Dice.get(1);
		
		assertEquals(1, dice.getFaceVal());
		assertFalse(dice.isHeld());
		
		for (int i = 0; i < 1000; i++) {
			dice.roll();
			assertTrue(dice.getFaceVal() >= 1);
			assertTrue(dice.getFaceVal() <= 6);
		}
		
		dice.setHold(true);
		assertTrue(dice.isHeld());
	}
	
	@Test
	public void testScorecardSingles() {

		
		int[] diceVals1 = {1, 3, 4, 1, 2};
		
		assertFalse(score.isComplete());
		assertEquals(0, score.getTotalScore());
		
		assertTrue(score.score(ScoreCategory.ONES, diceVals1));
		assertEquals(2, score.getTotalScore());
		
		assertTrue(score.score(ScoreCategory.TWOS, diceVals1));
		assertEquals(4, score.getTotalScore());
		
		assertTrue(score.score(ScoreCategory.THREES, diceVals1));
		assertEquals(7, score.getTotalScore());

		assertTrue(score.score(ScoreCategory.FOURS, diceVals1));
		assertEquals(11, score.getTotalScore());
		
		int[] diceVals2 = {1, 5, 5, 6, 6};
		
		assertTrue(score.score(ScoreCategory.FIVES, diceVals2));
		assertEquals(21, score.getTotalScore());
		
		assertTrue(score.score(ScoreCategory.SIXES, diceVals2));
		assertEquals(33, score.getTotalScore());

		
	}
	
	@Test
	public void testScorecard3and4OfKind() {

		
		int[] diceVals1 = {3, 3, 3, 3, 2};
		
		assertTrue(score.score(ScoreCategory.FOUR_OF_A_KIND, diceVals1));
		assertEquals(14, score.getTotalScore());
		

		int[] diceVals2 = {4, 4, 4, 6, 6};
		
		assertFalse(score.score(ScoreCategory.FOUR_OF_A_KIND, diceVals2));
		assertTrue(score.score(ScoreCategory.THREE_OF_A_KIND, diceVals2));
		
		assertEquals(38, score.getTotalScore());

		
	}
	
	@Test
	public void testScorecardFullHouse() {

		
		int[] diceVals1 = {3, 2, 2, 2, 2};
		
		assertTrue(score.score(ScoreCategory.FULL_HOUSE, diceVals1));
		/*
		 * Was previously assertFalse but how the score method works makes
		 * it return true even if a a hand does not meet the requirements
		 * for a non-zero scoring hand.
		 */
		assertEquals(0, score.getTotalScore());
		
		int[] diceVals2 = {5, 5, 5, 5, 5};
		
		assertFalse(score.score(ScoreCategory.FULL_HOUSE, diceVals2));

		int[] diceVals3 = {4, 4, 4, 6, 6};
		
		assertFalse(score.score(ScoreCategory.FULL_HOUSE, diceVals3));
		/*
		 * False is flagged because technically the Full House score was registered
		 * previously by a zero scoring hand.
		 */
		assertEquals(0, score.getTotalScore());
		

		
		
	}

	
	@Test
	public void testScorecardStraight() {

		
		int[] diceVals1 = {1, 3, 2, 4, 6};
		
		assertTrue(score.score(ScoreCategory.SMALL_STRAIGHT, diceVals1));
		assertEquals(30, score.getTotalScore());
		
		int[] diceVals2 = {2, 4, 3, 1, 5};
		
		assertTrue(score.score(ScoreCategory.LARGE_STRAIGHT, diceVals2));
		assertEquals(70, score.getTotalScore());

		
	}
	
	@Test
	public void testScorecardYahtzee() {

		
		int[] diceVals1 = {1, 3, 2, 4, 6};
		
		assertTrue(score.score(ScoreCategory.YAHTZEE, diceVals1));
		/*
		 * Was previously assertFalse but how the score method works makes
		 * it return true even if a hand does not meet the requirements
		 * for a non-zero scoring.
		 */
		assertEquals(0, score.getTotalScore());
		
		int[] diceVals2 = {4, 4, 4, 4, 4};
		
		assertFalse(score.score(ScoreCategory.YAHTZEE, diceVals2));
		/*
		 * False is flagged because technically the Yahtzee score was registered
		 * previously by a zero scoring hand.
		 */
		assertEquals(0, score.getTotalScore());


		
	}
	
	@Test
	public void testScorecardChance() {

		
		int[] diceVals1 = {1, 3, 2, 4, 6};
		
		assertTrue(score.score(ScoreCategory.CHANCE, diceVals1));
		assertEquals(16, score.getTotalScore());


		
	}
	
	
	
}
