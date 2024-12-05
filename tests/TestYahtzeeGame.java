package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import model.Player;
import model.ScoreCategory;
import model.YahtzeeGame;

public class TestYahtzeeGame {
    
    // No CPU
    YahtzeeGame game = new YahtzeeGame(2, false);


    @Test
    public void testGameInitialization() {
        assertEquals(1, game.getCurrentRound());
        assertFalse(game.isGameOver());
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
    public void testAdvanceTurn() {
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
    public void testGetPlayerScores() {
    	for (ScoreCategory sc : ScoreCategory.values()) {
    		// score all categories for both players
    		game.chooseScore(sc);
    		game.chooseScore(sc);
    	}
    	
    	Map<String, Integer> playerScores = game.getPlayerScores();
    	assertEquals(2, playerScores.size());
    	assertTrue(playerScores.containsKey("Player 1"));
    	assertTrue(playerScores.containsKey("Player 2"));
    }
    
    @Test
    public void testGetPlayerScore() {
    	assertEquals(0, game.getPlayerScore(0));
    	// will score no matter what the dice are initialized to
    	assertTrue(game.chooseScore(ScoreCategory.CHANCE)); 
    	assertTrue(game.getPlayerScore(0) > 0);
    }
    
    // Against CPU
    YahtzeeGame gameCpu = new YahtzeeGame(2, true);

    @Test
    public void testGameInitializationCpu() {
        assertEquals(1, gameCpu.getCurrentRound());
        assertFalse(gameCpu.isGameOver());
        assertFalse(gameCpu.isCurrentPlayerCPU());
        gameCpu.chooseScore(ScoreCategory.SMALL_STRAIGHT);
        assertTrue(gameCpu.isCurrentPlayerCPU());
    }
    
    @Test
    public void testSimulateTurn() {
    	gameCpu.rollDice();
    	gameCpu.rollDice();
    	
    	gameCpu.chooseScore(ScoreCategory.FIVES);
    	
    	// player turn over, now CPU
    	assertTrue(gameCpu.chooseScore(null));
    	assertEquals(2, gameCpu.getCurrentRound());
    }
    
    @Test
    public void simulateGame() {
    	// all rounds, all categories
    	for (ScoreCategory sc : ScoreCategory.values()) {
    		gameCpu.rollDice();
    		gameCpu.chooseScore(sc); // player
    		gameCpu.chooseScore(null); //CPU
    	}

    	System.out.println(gameCpu.getPlayerScores());
    	assertTrue(gameCpu.isGameOver());
    	assertEquals(2, gameCpu.getPlayerScores().size());
    }
    
}

