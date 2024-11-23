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
        assertEquals(null, game.getWinners());
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
}

