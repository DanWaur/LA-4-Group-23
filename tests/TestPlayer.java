package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Player;
import model.ScoreCategory;

public class TestPlayer {
	
	Player player = new Player("P1");
	
	@Test
	public void testGetName() {
		assertEquals(player.getName(), "P1");
	}
	
	@Test
	public void testRollDice() {
		assertTrue(player.rollDice());
		assertTrue(player.rollDice());
		assertTrue(player.rollDice());
		assertFalse(player.rollDice());
	}
	
	@Test
	public void testToggleDice_holdStates() {
		List<Integer> dicePositions = new ArrayList<>();
		dicePositions.add(2);
		dicePositions.add(4);
		
		player.toggleDice(dicePositions);
		
		List<Boolean> heldStates = player.getDiceHoldStates();
		
		assertFalse(heldStates.get(0));
		assertFalse(heldStates.get(1));
		assertTrue(heldStates.get(2));
		assertFalse(heldStates.get(3));
		assertTrue(heldStates.get(4));
		
		player.toggleDice(dicePositions);
		
		heldStates = player.getDiceHoldStates();
		assertFalse(heldStates.get(2));
		assertFalse(heldStates.get(4));
	}
	
	@Test
	public void testChooseScore() {
		assertTrue(player.chooseScore(ScoreCategory.FOUR_OF_A_KIND));
		assertFalse(player.chooseScore(ScoreCategory.FOUR_OF_A_KIND));
	}
	
	@Test
	public void testResetGame() {
		List<Integer> dicePositions = new ArrayList<>();
		dicePositions.add(2);
		dicePositions.add(4);
		
		player.toggleDice(dicePositions);
		player.chooseScore(ScoreCategory.FOUR_OF_A_KIND);
		
		player.resetGame();
		
		assertTrue(player.chooseScore(ScoreCategory.FOUR_OF_A_KIND));
	}
	
	@Test
	public void testResetTurn() {
		List<Integer> dicePositions = new ArrayList<>();
		dicePositions.add(0);
		dicePositions.add(3);
		
		player.rollDice();
		player.rollDice();
		player.toggleDice(dicePositions);
		
		player.rollDice();
		
		player.resetTurn();
		
		assertTrue(player.rollDice());
	}
	
	@Test
	public void testRollsLeft() {
		assertEquals(3, player.getRollsLeft());
		player.rollDice();
		assertEquals(2, player.getRollsLeft());
		player.rollDice();
		assertEquals(1, player.getRollsLeft());
		player.rollDice();
		assertEquals(0, player.getRollsLeft());
	}
	
	@Test
	public void testGetDiceFaces() {
		assertEquals(5, player.getDiceFaces().size());
	}
	
	@Test
	public void testScoreForCategory() {
		player.chooseScore(ScoreCategory.CHANCE);
		assertTrue(player.getScoreForCategory(ScoreCategory.CHANCE) > 0);
	}
	
	@Test
	public void testTotalScore() {
		assertEquals(0,player.getTotalScore());
		player.chooseScore(ScoreCategory.CHANCE);
		assertTrue(player.getTotalScore() > 0);
	}
	
	@Test
	public void testCompareTo() {
		Player p2 = new Player("P2");
		p2.chooseScore(ScoreCategory.CHANCE);
		assertTrue(player.compareTo(p2) < 0);
	}

}
