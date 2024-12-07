/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (dreynaldo)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.DiceValue;
import model.Player;
import model.ScoreCategory;

public class TestPlayer {
	
	Player player = new Player("P1");
	Player playerDef = new Player();
	
	@Test
	public void testGetName() {
		assertEquals(player.getName(), "P1");
	}
	
	@Test
	public void testRollDice() {
		
		assertFalse(player.hasRolled());
		
		assertTrue(player.rollDice());
		assertTrue(player.rollDice());
		assertTrue(player.rollDice());
		assertFalse(player.rollDice());
		
		assertTrue(player.hasRolled());
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
	
	@Test
	public void testSetRolled() {
		Player p2 = new Player("P2");
		p2.chooseScore(ScoreCategory.CHANCE);
		assertTrue(player.compareTo(p2) < 0);
	}
	
	@Test
	public void testScoreCheck() {
		
		int x = player.calculateScoreForCategory(ScoreCategory.YAHTZEE);
		assertTrue(x == 0 || x == 50);
		
		assertFalse(player.isScored(ScoreCategory.YAHTZEE));
		assertTrue(player.chooseScore(ScoreCategory.YAHTZEE));
		assertTrue(player.isScored(ScoreCategory.YAHTZEE));
		

	}
	
	@Test
	public void testGetFacesString() {
		
		
		player.rollDice();
		List<String> faces = player.getDiceFacesAsStrings();
		
		assertEquals(5, faces.size());
		
		String first = faces.get(0);
		
		assertTrue(DiceValue.valueOf(first) != null);
		

	}
	
	@Test
	public void testResetTurn() {
		
		assertTrue(player.rollDice());
		assertTrue(player.rollDice());
		
		
		List<Integer> toggle = new ArrayList<Integer>();
		toggle.add(0);
		toggle.add(1);
		
		player.toggleDice(toggle);
		assertTrue(player.getDiceHoldStates().get(0));
		assertTrue(player.getDiceHoldStates().get(1));
		assertFalse(player.getDiceHoldStates().get(2));
		assertFalse(player.getDiceHoldStates().get(3));
		assertFalse(player.getDiceHoldStates().get(4));
		
		assertTrue(player.rollDice());
		assertFalse(player.rollDice());
		
		player.resetTurn();
		
		assertTrue(player.rollDice());
		assertFalse(player.getDiceHoldStates().get(0));
		assertFalse(player.getDiceHoldStates().get(1));
		assertFalse(player.getDiceHoldStates().get(2));
		assertFalse(player.getDiceHoldStates().get(3));
		assertFalse(player.getDiceHoldStates().get(4));
		

	}

}
