package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import controller.YahtzeeController;
import model.DiceValue;
import model.Player;
import model.ScoreCategory;
import model.YahtzeeGame;

public class TestYahtzeeGame {
    
    // No CPU
    YahtzeeController game = new YahtzeeController(2, false);


    @Test
    public void testGameInitialization() {
        assertEquals(1, game.getCurrentRound());
        assertFalse(game.isGameOver());
    }

    @Test
    public void testRollDice() {
    	
    	assertFalse(game.hasPlayerRolled("Player 1"));
    	
        assertEquals(true, game.rollDice());
        assertEquals(true, game.rollDice());
        assertEquals(true, game.rollDice());
        // used up 3 rolls
        assertEquals(false, game.rollDice());
        
        
        assertTrue(game.hasPlayerRolled("Player 1"));
        assertFalse(game.hasPlayerRolled("Player 2"));
    }

    @Test
    public void testToggleDice() {
    	


        List<Integer> diceIndices = new ArrayList<>();
        diceIndices.add(0);
        diceIndices.add(1);
        diceIndices.add(3);
        assertFalse(game.toggleDice(diceIndices));
        game.rollDice();
        assertTrue(game.toggleDice(diceIndices)); 

        game.rollDice();
    }

    @Test
    public void testChooseScore() {
    	game.rollDice();
        ScoreCategory scoreChoice = ScoreCategory.FOURS;
        boolean scored = game.chooseScore(scoreChoice);
        assertTrue(scored);
        assertFalse(game.chooseScore(scoreChoice));
        
        // other player, same category
        game.advanceToNextPlayer();
        game.rollDice();
        scored = game.chooseScore(scoreChoice);
        assertTrue(scored);
        
       
        // first player again, already scored category
        game.advanceToNextPlayer();
        scored = game.chooseScore(scoreChoice);
        assertFalse(scored);
    }
    
    @Test
    public void testAdvanceTurn() {
    	int currentRound = game.getCurrentRound();
    	
    	assertEquals(0, game.getCurrentPlayerIndex());
        
    	// both turns
    	game.chooseScore(ScoreCategory.SMALL_STRAIGHT);
    	game.advanceToNextPlayer();
    	game.chooseScore(ScoreCategory.FIVES);
    	
    	assertEquals(1, game.getCurrentPlayerIndex());
    	
    	assertEquals(currentRound, game.getCurrentRound());
    	
    	game.advanceToNextPlayer();
    	assertEquals(0, game.getCurrentPlayerIndex());
    	assertEquals(currentRound + 1, game.getCurrentRound());
    }
    
    @Test
    public void testIsGameOver_True() {
    	for (ScoreCategory sc : ScoreCategory.values()) {
    		// score all categories for both players
    		game.rollDice();
    		game.chooseScore(sc);
    		game.advanceToNextPlayer();
    		game.rollDice();
    		game.chooseScore(sc);
    		game.advanceToNextPlayer();
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
    	assertEquals(0, game.getPlayerScoreByIndex(0));
    	
    	// will score no matter what the dice are initialized to
    	game.rollDice();
    	assertTrue(game.chooseScore(ScoreCategory.CHANCE)); 
    	assertTrue(game.getPlayerScoreByIndex(0) > 0);
    	assertTrue(game.getPlayerScoreByIndex(0) > 0);
    }
    
    
    
    // Against CPU
    YahtzeeController gameCpu = new YahtzeeController(2, true);

    @Test
    public void testGameInitializationCpu() {
        assertEquals(1, gameCpu.getCurrentRound());
        assertFalse(gameCpu.isGameOver());
        assertFalse(gameCpu.isCurrentPlayerCPU());
        gameCpu.rollDice();
        gameCpu.chooseScore(ScoreCategory.SMALL_STRAIGHT);
        gameCpu.advanceToNextPlayer();
        assertTrue(gameCpu.isCurrentPlayerCPU());
    }
    
    @Test
    public void testSimulateTurn() {
    	
    	gameCpu.rollDice();
    	assertEquals(null, gameCpu.getCpuAim());	// current player not cpu
    	assertEquals(null, gameCpu.iterateCpuChoices(ScoreCategory.CHANCE));	// cpu hasn't made first roll
    	gameCpu.chooseScore(ScoreCategory.FIVES);
    	gameCpu.advanceToNextPlayer();
    	
    	ScoreCategory aim = gameCpu.getCpuAim();
    	assertEquals(null, gameCpu.getCpuAim());	// cpu already rolled 
    	
    	assertNotEquals(null, aim);
	    ScoreCategory result = gameCpu.iterateCpuChoices(aim);
	    while (result == null) {
	    	result = gameCpu.iterateCpuChoices(aim);
	    }
    	
    	assertTrue(gameCpu.chooseScore(result));
    }
    
    @Test
    public void testSimulateGame() {
    	// all rounds, all categories
    	for (ScoreCategory sc : ScoreCategory.values()) {
    		
    		gameCpu.rollDice();
    		gameCpu.chooseScore(sc); // player
    		gameCpu.advanceToNextPlayer();
    		
    		ScoreCategory aim = gameCpu.getCpuAim();
    	    ScoreCategory result = gameCpu.iterateCpuChoices(aim);
    	    while (result == null) {
    	    	result = gameCpu.iterateCpuChoices(aim);
    	    }
    	    
    	    
    	    assertTrue(gameCpu.chooseScore(result));
    	    gameCpu.advanceToNextPlayer();
    	}

    	assertTrue(gameCpu.isGameOver());
    	assertEquals(2, gameCpu.getPlayerScores().size());
    }
    
    @Test
    public void testPlayers() {
    	List<Player> players = game.getPlayers();
    	
    	assertEquals(2, players.size());
    	assertEquals("Player 1",players.get(0).getName());
    }
    
    @Test
    public void testGetFacesAndHolds() {
    	
    	game.rollDice();
    	
    	List<DiceValue> dice = game.getCurrentDiceFaces();
    	

    	List<Integer> holdPos = new ArrayList<Integer>();
    	holdPos.add(0);
    	holdPos.add(1);
    	holdPos.add(3);
    	
    	
    	assertTrue(game.toggleDice(holdPos));
    	List<Boolean> holds = game.getCurrentDiceHolds();

    	assertTrue(holds.get(0));
    	assertTrue(holds.get(1));
    	assertFalse(holds.get(2));
    	assertTrue(holds.get(3));
    	assertFalse(holds.get(4));
    }
    
    @Test
    public void testGetPotential() {
    	
    	game.rollDice();
    	
    	Map<ScoreCategory, Integer> map = game.getPotentialScores("Player 1");
    	
    	assertTrue(map.get(ScoreCategory.CHANCE) > 0);

    }
    
    @Test
    public void testScoreSelected() {
    	
    	game.rollDice();

    	assertTrue(game.scoreSelectedCategory("Player 1", "ONES (0)"));
    	assertTrue(game.scoreSelectedCategory("Player 1", "THREE_OF_A_KIND (0)"));

    }
    
    @Test
    public void testGetName() {
    	
    	game.rollDice();
    	game.chooseScore(ScoreCategory.ONES);
    	assertEquals("Player 1", game.getCurrentPlayerName());
    	game.advanceToNextPlayer();
    	assertEquals("Player 2", game.getCurrentPlayerName());

    }
    
    
    @Test
    public void testGetCategoryScoreAndSelectable() {
    	
    	game.rollDice();
    	game.chooseScore(ScoreCategory.CHANCE);
    	assertTrue(game.getCategoryScoreForPlayer("Player 1", ScoreCategory.CHANCE) > 0);
    	
    	List<String> categories = game.getSelectableCategories("Player 1");
    	
    	for (String s : categories) {
    		assertFalse(s.contains("CHANCE"));
    	}

    }
    
}

