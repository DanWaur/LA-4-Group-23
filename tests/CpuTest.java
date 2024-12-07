/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (dreynaldo)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

package tests;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import model.Cpu;
import model.ScoreCategory;

public class CpuTest {
	
	Cpu cpu = new Cpu();
	int[] ones = {1,1,1,1,1};
	int[] twos = {2,2,2,2,2};
	int[] threes = {3,3,3,3,3};
	int[] fours = {4,4,4,4,4};
	int[] fives = {5,5,5,5,5};
	int[] sixes = {6,6,6,6,6};
	
	

    @Test
    public void testChooseAimCountDice() {

    	assertEquals(ScoreCategory.ONES, cpu.chooseAimFor(ScoreCategory.ONES, ones));
    	assertEquals(ScoreCategory.TWOS, cpu.chooseAimFor(ScoreCategory.TWOS, twos));
    	assertEquals(ScoreCategory.THREES, cpu.chooseAimFor(ScoreCategory.THREES, threes));
    	assertEquals(ScoreCategory.FOURS, cpu.chooseAimFor(ScoreCategory.FOURS, fours));
    	assertEquals(ScoreCategory.FIVES, cpu.chooseAimFor(ScoreCategory.FIVES, fives));
    	assertEquals(ScoreCategory.SIXES, cpu.chooseAimFor(ScoreCategory.SIXES, sixes));
    	
    }
    
    
    @Test
    public void testChooseAimYahtzee() {
    	int[] fourOfKind1 = {3,1,3,3,3};
    	int[] fourOfKind2 = {2,2,3,2,2};
    	int[] fourOfKind3 = {6,6,6,6,6};
    	assertEquals(ScoreCategory.YAHTZEE, cpu.chooseAimFor(ScoreCategory.YAHTZEE, fourOfKind1));
    	assertEquals(ScoreCategory.YAHTZEE, cpu.chooseAimFor(ScoreCategory.YAHTZEE, fourOfKind2));
    	assertEquals(ScoreCategory.YAHTZEE, cpu.chooseAimFor(ScoreCategory.YAHTZEE, fourOfKind3));
    	
    }
    
    @Test
    public void testChooseAimFullHouse() {
    	int[] a = {3,1,3,3,2};
    	int[] b = {2,2,3,2,1};
    	int[] c = {6,6,6,3,3};
    	assertEquals(ScoreCategory.FULL_HOUSE, cpu.chooseAimFor(ScoreCategory.YAHTZEE, a));
    	assertEquals(ScoreCategory.FULL_HOUSE, cpu.chooseAimFor(ScoreCategory.YAHTZEE, b));
    	assertEquals(ScoreCategory.FULL_HOUSE, cpu.chooseAimFor(ScoreCategory.YAHTZEE, c));
    	
    }
    
    @Test
    public void testChooseAimFourKind() {
    	int[] a = {3,1,3,3,2};
    	int[] b = {2,2,3,2,1};
    	int[] c = {6,6,6,3,3};
    	
    	cpu.chooseScore(ScoreCategory.FULL_HOUSE);
    	
    	
    	assertEquals(ScoreCategory.FOUR_OF_A_KIND, cpu.chooseAimFor(ScoreCategory.YAHTZEE, a));
    	assertEquals(ScoreCategory.FOUR_OF_A_KIND, cpu.chooseAimFor(ScoreCategory.YAHTZEE, b));
    	assertEquals(ScoreCategory.FOUR_OF_A_KIND, cpu.chooseAimFor(ScoreCategory.YAHTZEE, c));
    	
    }
    
    @Test
    public void testChooseAimThreeKind() {
    	int[] a = {3,1,3,2,2};
    	int[] b = {6,2,3,2,1};
    	int[] c = {3,2,3,4,4};
    	
    	
    	assertEquals(ScoreCategory.THREE_OF_A_KIND, cpu.chooseAimFor(ScoreCategory.YAHTZEE, a));
    	assertEquals(ScoreCategory.THREE_OF_A_KIND, cpu.chooseAimFor(ScoreCategory.YAHTZEE, b));
    	assertEquals(ScoreCategory.THREE_OF_A_KIND, cpu.chooseAimFor(ScoreCategory.YAHTZEE, c));
    	
    }
    
    @Test
    public void testChooseAimNone() {
    	int[] a = {1,2,3,4,5};
    	
    	assertEquals(ScoreCategory.LARGE_STRAIGHT, cpu.chooseAimFor(ScoreCategory.YAHTZEE, a));
    	
    }
    
    @Test
    public void testKeepCount() {
    	int[] a = {1,1,5,2,3};
    	
    	List<Integer> kept = cpu.getDiceKeepForNext(ScoreCategory.ONES, a);
    	assertEquals(2, kept.size());
    	assertEquals((Integer) 0, kept.get(0));
    	assertEquals((Integer) 1, kept.get(1));
    	
    	
    	int[] b = {1,2,5,2,2};
    	
    	kept = cpu.getDiceKeepForNext(ScoreCategory.TWOS, b);
    	assertEquals(3, kept.size());
    	assertEquals((Integer) 1, kept.get(0));
    	assertEquals((Integer) 3, kept.get(1));
    	assertEquals((Integer) 4, kept.get(2));
    	
    	
    	int[] c = {3,2,3,2,3};
    	
    	kept = cpu.getDiceKeepForNext(ScoreCategory.THREES, c);
    	assertEquals(3, kept.size());
    	assertEquals((Integer) 0, kept.get(0));
    	assertEquals((Integer) 2, kept.get(1));
    	assertEquals((Integer) 4, kept.get(2));
    	
    	
    	int[] d = {4,6,4,4,4};
    	
    	kept = cpu.getDiceKeepForNext(ScoreCategory.FOURS, d);
    	assertEquals(4, kept.size());
    	assertEquals((Integer) 0, kept.get(0));
    	assertEquals((Integer) 2, kept.get(1));
    	assertEquals((Integer) 3, kept.get(2));
    	assertEquals((Integer) 4, kept.get(3));
    	
    	
    	int[] e = {5,5,5,5,5};
    	
    	kept = cpu.getDiceKeepForNext(ScoreCategory.FIVES, e);
    	assertEquals(5, kept.size());
    	assertEquals((Integer) 0, kept.get(0));
    	assertEquals((Integer) 1, kept.get(1));
    	assertEquals((Integer) 2, kept.get(2));
    	assertEquals((Integer) 3, kept.get(3));
    	assertEquals((Integer) 4, kept.get(4));
    	
    	
    	int[] f = {6,5,6,6,5};
    	
    	kept = cpu.getDiceKeepForNext(ScoreCategory.SIXES, f);
    	assertEquals(3, kept.size());
    	assertEquals((Integer) 0, kept.get(0));
    	assertEquals((Integer) 2, kept.get(1));
    	assertEquals((Integer) 3, kept.get(2));
    }
    
    @Test
    public void testKeepThreeKind() {
    	int[] a = {1,1,5,1,3};
    	
    	List<Integer> kept = cpu.getDiceKeepForNext(ScoreCategory.THREE_OF_A_KIND, a);
    	assertEquals(3, kept.size());
    	assertEquals((Integer) 0, kept.get(0));
    	assertEquals((Integer) 1, kept.get(1));
    	assertEquals((Integer) 3, kept.get(2));
    	
    	
    	
    	int[] b = {6,1,1,6,1};
    	
    	kept = cpu.getDiceKeepForNext(ScoreCategory.THREE_OF_A_KIND, b);
    	assertEquals(3, kept.size());
    	assertEquals((Integer) 1, kept.get(0));
    	assertEquals((Integer) 2, kept.get(1));
    	assertEquals((Integer) 4, kept.get(2));
    }
    
    @Test
    public void testKeepFourKind() {
    	int[] a = {2,3,2,2,2};
    	
    	List<Integer> kept = cpu.getDiceKeepForNext(ScoreCategory.FOUR_OF_A_KIND, a);
    	assertEquals(4, kept.size());
    	assertEquals((Integer) 0, kept.get(0));
    	assertEquals((Integer) 2, kept.get(1));
    	assertEquals((Integer) 3, kept.get(2));
    	assertEquals((Integer) 4, kept.get(3));
    	
    	
    	int[] b = {4,5,5,5,5};
    	
    	kept = cpu.getDiceKeepForNext(ScoreCategory.FOUR_OF_A_KIND, b);
    	assertEquals(4, kept.size());
    	assertEquals((Integer) 1, kept.get(0));
    	assertEquals((Integer) 2, kept.get(1));
    	assertEquals((Integer) 3, kept.get(2));
    	assertEquals((Integer) 4, kept.get(3));
	

    	
    }
    
    @Test
    public void testKeepFullHouse() {
    	int[] a = {2,3,2,1,2};
    	
    	List<Integer> kept = cpu.getDiceKeepForNext(ScoreCategory.FULL_HOUSE, a);
    	assertEquals(3, kept.size());
    	assertEquals((Integer) 0, kept.get(0));
    	assertEquals((Integer) 2, kept.get(1));
    	assertEquals((Integer) 4, kept.get(2));
    	
    	
    	int[] b = {4,5,5,5,5};
    	
    	kept = cpu.getDiceKeepForNext(ScoreCategory.FULL_HOUSE, b);
    	assertEquals(3, kept.size());
    	assertEquals((Integer) 1, kept.get(0));
    	assertEquals((Integer) 2, kept.get(1));
    	assertEquals((Integer) 3, kept.get(2));
	
    }
    
    @Test
    public void testKeepStraightsAndChance() {
    	int[] a = {1,2,3,1,2};
    	
    	List<Integer> kept = cpu.getDiceKeepForNext(ScoreCategory.SMALL_STRAIGHT, a);
    	assertEquals(0, kept.size());
    	
    	
    	int[] b = {1,2,3,4,1};
    	
    	kept = cpu.getDiceKeepForNext(ScoreCategory.LARGE_STRAIGHT, b);
    	assertEquals(0, kept.size());
    	
    	
    	int[] c = {6,6,6,5,5};
    	
    	kept = cpu.getDiceKeepForNext(ScoreCategory.CHANCE, c);
    	assertEquals(1, kept.size());
	
    }
    
    @Test
    public void testKeepYahtzee() {
    	int[] a = {3,3,3,3,3};
    	
    	List<Integer> kept = cpu.getDiceKeepForNext(ScoreCategory.YAHTZEE, a);
    	assertEquals(5, kept.size());
    	assertEquals((Integer) 0, kept.get(0));
    	assertEquals((Integer) 1, kept.get(1));
    	assertEquals((Integer) 2, kept.get(2));
    	assertEquals((Integer) 3, kept.get(3));
    	assertEquals((Integer) 4, kept.get(4));
    }
    
    @Test
    public void testKeepChance() {
    	
    	int[] a = {2,3,5,4,1};
    	
    	List<Integer> kept = cpu.getDiceKeepForNext(ScoreCategory.CHANCE, a);
    	assertEquals(1, kept.size());
    	assertEquals((Integer) 2, kept.get(0));		// position 2, 5 is highest


    }
    
    @Test
    public void testCpuGame() {

    	
    	// Run the game 13 times until all categories are filled
    	for (int i = 0; i < ScoreCategory.values().length; i++) {

    		// Cpu Decision per round
    		cpu.resetTurn();
        	ScoreCategory aim = cpu.firstRoll();

        	
        	ScoreCategory result = cpu.makeDecision(aim);
        	while (result == null) {

        		result = cpu.makeDecision(aim);
        	}
        	
        	cpu.chooseScore(result);
    		
    		
    		// Count categories scored
        	int catsScored = 0;
        	for (ScoreCategory cat : ScoreCategory.values()) {
        		if (cpu.isScored(cat)) {
        			catsScored++;
        		}
        	}
        	
        	
        	assertEquals(i+1, catsScored);

    	}
    	
    	

    }
}
