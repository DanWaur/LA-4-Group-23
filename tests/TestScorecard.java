package tests;

import model.ScoreCategory;
import model.Scorecard;


import org.junit.Test;
import static org.junit.Assert.*;

public class TestScorecard {
    Scorecard scorecard = new Scorecard();
    int[] diceValues = new int[5];

    @Test
    public void testIsComplete_False(){
        assertFalse(scorecard.isComplete());
    }

    @Test
    public void testOnes(){
        diceValues[0] = 1;
        diceValues[1] = 4;
        diceValues[2] = 3;
        diceValues[3] = 1;
        diceValues[4] = 1;
        assertTrue(scorecard.score(ScoreCategory.ONES, diceValues));
        assertEquals(3, scorecard.getTotalScore());
    }

    @Test
    public void testTwos(){
        diceValues[0] = 6;
        diceValues[1] = 2;
        diceValues[2] = 5;
        diceValues[3] = 1;
        diceValues[4] = 2;
        assertTrue(scorecard.score(ScoreCategory.TWOS, diceValues));
        assertEquals(4, scorecard.getTotalScore());
    }

    @Test
    public void testThrees(){
        diceValues[0] = 3;
        diceValues[1] = 3;
        diceValues[2] = 3;
        diceValues[3] = 4;
        diceValues[4] = 3;
        assertTrue(scorecard.score(ScoreCategory.THREES, diceValues));
        assertEquals(12, scorecard.getTotalScore());
    }

    @Test
    public void testFours(){
        diceValues[0] = 1;
        diceValues[1] = 1;
        diceValues[2] = 1;
        diceValues[3] = 1;
        diceValues[4] = 4;
        assertTrue(scorecard.score(ScoreCategory.FOURS, diceValues));
        assertEquals(4, scorecard.getTotalScore());
    }

    @Test
    public void testFives(){
        diceValues[0] = 5;
        diceValues[1] = 3;
        diceValues[2] = 6;
        diceValues[3] = 5;
        diceValues[4] = 2;
        assertTrue(scorecard.score(ScoreCategory.FIVES, diceValues));
        assertEquals(10, scorecard.getTotalScore());
    }
    
    @Test
    public void testSixes(){
        diceValues[0] = 4;
        diceValues[1] = 6;
        diceValues[2] = 6;
        diceValues[3] = 2;
        diceValues[4] = 6;
        assertTrue(scorecard.score(ScoreCategory.SIXES, diceValues));
        assertEquals(18, scorecard.getTotalScore());
    }

    @Test
    public void testThreeOfAKind(){
        diceValues[0] = 4;
        diceValues[1] = 1;
        diceValues[2] = 2;
        diceValues[3] = 1;
        diceValues[4] = 1;
        assertTrue(scorecard.score(ScoreCategory.THREE_OF_A_KIND, diceValues));
        assertEquals(9, scorecard.getTotalScore());
    }

    @Test
    public void testFourOfAKind(){
        diceValues[0] = 2;
        diceValues[1] = 5;
        diceValues[2] = 2;
        diceValues[3] = 2;
        diceValues[4] = 2;
        assertTrue(scorecard.score(ScoreCategory.FOUR_OF_A_KIND, diceValues));
        assertEquals(13, scorecard.getTotalScore());
    }

    @Test
    public void testFullHouse(){
        diceValues[0] = 3;
        diceValues[1] = 6;
        diceValues[2] = 3;
        diceValues[3] = 3;
        diceValues[4] = 6;
        assertTrue(scorecard.score(ScoreCategory.FULL_HOUSE, diceValues));
        assertEquals(25, scorecard.getTotalScore());
    }

    @Test
    public void testSmallStraight(){
        diceValues[0] = 1;
        diceValues[1] = 2;
        diceValues[2] = 3;
        diceValues[3] = 4;
        diceValues[4] = 5;
        assertTrue(scorecard.score(ScoreCategory.SMALL_STRAIGHT, diceValues));
        assertEquals(30, scorecard.getTotalScore());
    }

    @Test
    public void testLargeStraight(){
        diceValues[0] = 5;
        diceValues[1] = 3;
        diceValues[2] = 2;
        diceValues[3] = 4;
        diceValues[4] = 1;
        assertTrue(scorecard.score(ScoreCategory.LARGE_STRAIGHT, diceValues));
        assertEquals(40, scorecard.getTotalScore());
    }

}