package tests;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import model.Player;
import model.ScoreCategory;
import model.YahtzeeGame;

public class TestYahtzeeGame {
    
    YahtzeeGame game = new YahtzeeGame(2);


    @Test
    public void testGameInitialization() {
        assertEquals(1, game.getCurrentRound());
        assertTrue(game.getWinners().isEmpty());
    }

    @Test
    public void testRollDice() {
        assertEquals(true, game.rollDice());
        assertEquals(true, game.rollDice());
        assertEquals(true, game.rollDice());
        // used up 3 rolls
        assertEquals(false, game.rollDice());
    }

    @Test
    public void testToggleDice() {
        game.rollDice();

        List<Integer> diceIndices = new ArrayList<>();
        diceIndices.add(0);
        diceIndices.add(1);
        diceIndices.add(3);
        game.toggleDice(diceIndices); 

        game.rollDice();
    }

    @Test
    public void testChooseScore() {
        ScoreCategory scoreChoice = ScoreCategory.FOURS;
        boolean scored = game.chooseScore(scoreChoice);
        assertTrue(scored);
        
        // other player, same category
        scored = game.chooseScore(scoreChoice);
        assertTrue(scored);
        
        // first player again, already scored category
        scored = game.chooseScore(scoreChoice);
        assertFalse(scored);
    }
    
    @Test
    public void tesAdvanceTurn() {
    	int currentRound = game.getCurrentRound();
        
    	// both turns
    	game.chooseScore(ScoreCategory.SMALL_STRAIGHT);
    	game.chooseScore(ScoreCategory.FIVES);
    	
    	assertEquals(currentRound + 1, game.getCurrentRound());
    }
    
    @Test
    public void testIsGameOver_True() {
    	for (ScoreCategory sc : ScoreCategory.values()) {
    		// score all categories for both players
    		game.chooseScore(sc);
    		game.chooseScore(sc);
    	}
    	assertTrue(game.isGameOver());
    }
    
    @Test
    public void testIsGameOver_False() {
    	for (int i = 0; i < 7; i++) {
    		ScoreCategory sc = ScoreCategory.values()[i];
    		// score all categories for both players
    		game.chooseScore(sc);
    		game.chooseScore(sc);
    	}
    	assertFalse(game.isGameOver());
    }
    
    @Test
    public void testGetWinners() {
    	for (ScoreCategory sc : ScoreCategory.values()) {
    		// score all categories for both players
    		game.chooseScore(sc);
    		game.chooseScore(sc);
    	}
    	// tie since tests did not make anyone roll, same score in all
    	assertEquals(2, game.getWinners().size());
    }
    
    @Test
    public void testGetPlayerScore() {
    	assertEquals(0, game.getPlayerScore(0));
    	// will score no matter what the dice are initialized to
    	assertTrue(game.chooseScore(ScoreCategory.CHANCE)); 
    	assertTrue(game.getPlayerScore(0) > 0);
    }
    
    
}

