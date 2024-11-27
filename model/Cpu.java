package model;

/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (dreynaldo)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */


import java.util.ArrayList;
import java.util.List;

public class Cpu extends Player {
	
	
	
    /**
     * Roll dice for cpu until the target category can be scored. If it cannot be scored
     * after 3 times, score with best point wise score available.
     * @param aimFor - category to continuosly attempt to get by rerolling unheld dice.
     */
	public void rollUntilAim(ScoreCategory aimFor) {
		boolean rolled = this.rollDice();
		while (rolled == true && scoreCard.calculateScoreForCategory(aimFor, this.getDiceValues()) == 0) {
			rolled = this.rollDice();
			
		}
		
		// Can score with aimed target score
		if (scoreCard.calculateScoreForCategory(aimFor, this.getDiceValues()) != 0) {
			this.chooseScore(aimFor);
		}
		// Otherwise, score with best available score (points wise)
		else {
			this.chooseScore(this.getBestScore());
		}
		
		
	}

    /**
     * Select which score to aim for based on the best current score.
     * @return bestCat - a ScoreCategory which has the most points
     */
	public void chooseAimFor() {
		ScoreCategory bestCurrent = this.getBestScore();
		int pairSize = tryGetPairFrom(5).size();
		ScoreCategory aimFor;
		
		if (bestCurrent == ScoreCategory.ONES || bestCurrent == ScoreCategory.TWOS 
				|| bestCurrent == ScoreCategory.THREES || bestCurrent == ScoreCategory.FOURS
				|| bestCurrent == ScoreCategory.FIVES || bestCurrent == ScoreCategory.SIXES) {
			aimFor = bestCurrent;
		}
		else if (pairSize >= 4 && !scoreCard.isScored(ScoreCategory.YAHTZEE)) {
			aimFor = ScoreCategory.YAHTZEE;
		}
		else if (pairSize == 3 && !scoreCard.isScored(ScoreCategory.FULL_HOUSE)) {
			aimFor = ScoreCategory.FULL_HOUSE;
		}
		else if (pairSize == 3 && !scoreCard.isScored(ScoreCategory.FOUR_OF_A_KIND)) {
			aimFor = ScoreCategory.FOUR_OF_A_KIND;
		}
		else if (pairSize == 2 && !scoreCard.isScored(ScoreCategory.THREE_OF_A_KIND)) {
			aimFor = ScoreCategory.THREE_OF_A_KIND;
		}
		else {
			aimFor = getBestScore();
		}
	}
	
    /**
     * Get the current best score in potential scoring categories.
     * @return bestCat - a ScoreCategory which has the most points
     */
	public ScoreCategory getBestScore() {
		ScoreCategory bestCat = null;
		int bestVal = 0;
		
		for (ScoreCategory category : ScoreCategory.values()) {
			
			if(!scoreCard.isScored(category)) {
				int current = scoreCard.calculateScoreForCategory(category, this.getDiceValues());
				
				if (current > bestVal) {
					bestCat = category;
					bestVal = current;
				}
			}
		}
		
		return bestCat;
		
	}
	
    /**
     * Based on a score to aim for, determine which dice to hold for next roll.
     * @param aimFor - a ScoreCategory to aim for
     */
	public void getDiceKeepForNext(ScoreCategory aimFor) {
		
		List<Integer> selectPos;
		
		switch (aimFor) {
			case ONES:
	        	selectPos = getOfValue(6);
	        	
	        	this.toggleDice(selectPos);
	        	
	        	break;

	        case TWOS:
	        	selectPos = getOfValue(2);
	        	
	        	this.toggleDice(selectPos);
	        	
	        	break;
	            
	        case THREES:
	        	selectPos = getOfValue(3);
	        	
	        	this.toggleDice(selectPos);
	        	
	        	break;
	            
	        case FOURS:
	        	selectPos = getOfValue(4);
	        	
	        	this.toggleDice(selectPos);
	        	
	        	break;
	            
	        case FIVES:
	        	selectPos = getOfValue(5);
	        	
	        	this.toggleDice(selectPos);
	        	
	        	break;
	            
	        case SIXES:
	        	selectPos = getOfValue(6);
	        	
	        	this.toggleDice(selectPos);
	        	
	        	break;
	            
	        case THREE_OF_A_KIND:
	        	selectPos = tryGetPairFrom(3);
	        	
	        	this.toggleDice(selectPos);
	        	
	        	break;
	        	
	        case FOUR_OF_A_KIND:
	        	selectPos = tryGetPairFrom(4);
	        	
	        	this.toggleDice(selectPos);
	        	
	        	break;
	        	
	        case FULL_HOUSE:
	        	selectPos = tryGetPairFrom(3);
	        	
	        	break;
	        	
	        case SMALL_STRAIGHT:
	        	break;
	        	
	        case LARGE_STRAIGHT:
	        	break;
	        	
	        case YAHTZEE:
	        	selectPos = tryGetPairFrom(5);
	        	
	        	break;
	        	
	        case CHANCE:
	        	
	        	break;

		}
	}
	
	
    /**
     * Continuously iterate down from a start pair size, trying
     * to get the indices of the pairs.
     * @param start - pair size to start from
     * @return - List<Integer> representing the indices in the dice array that are a pair.
     */
	private List<Integer> tryGetPairFrom(int start){
		
		List<Integer> startPos = new ArrayList<Integer>();
		
		int current = start;
		while (current >= 2 && startPos.size() == 0) {
			
			startPos = this.getOfKind(current);
			current--;
		}
		if (startPos.size() == 0) {
			startPos = this.getSingleHighest();
		}
		
		return startPos;
		
	}
	
    /**
     * Gets positions of a pair of size.
     * @param pairAmount - Size of pair to find
     * @return - List<Integer> representing the indices in the dice array that are a pair.
     */
	private List<Integer> getOfKind(int pairAmount) {
		int[] vals = this.getDiceValues();
		List<Integer> selectPos = new ArrayList<Integer>();
		
		// Count all dice amounts
		int count[] = new int[7];
		for (int val : vals) {
			count[val]++;
		}
		
		// Identify pair that fits pair amount
		int pairVal = 0;
		for (int i = 6; i > 0; i--) {
			if (count[i] >= pairAmount) {
				pairVal = pairAmount;
				break;
			}
		}
		// No pair found
		if (pairVal == 0) {
			return null;
		}
		
		// Otherwise, Collect positions of pair values
		for (int i = 0; i < vals.length; i++) {
			if (vals[i] == pairVal) {
				selectPos.add(i);
			}
		}
		
		return selectPos;
	}
	
    /**
     * Gets positions of a pair of a certain value
     * @param value
     * @return - List<Integer> representing the indices in the dice array that are a pair.
     */
	private List<Integer> getOfValue(int value) {
		int[] vals = this.getDiceValues();
		List<Integer> selectPos = new ArrayList<Integer>();

		for (int i = 0; i < vals.length; i++) {
			if (vals[i] == value) {
				selectPos.add(i);
			}
		}
		
		return selectPos;
	}
	
	
    /**
     * Gets the highest value in the dice list.
     * @return - List<Integer> representing the indices(single index) of the highest value
     */
	private List<Integer> getSingleHighest() {
		int[] vals = this.getDiceValues();
		List<Integer> selectPos = new ArrayList<Integer>();
		
		int pos = 0;
		int max = 0;
		int maxPos = 0;
		for (int val : vals) {
			if (val > max) {
				max = val;
				maxPos = pos;
			}
			
			pos++;
		}
		
		selectPos.add(maxPos);
		
		return selectPos;
		
		
	}
	
	
	
	
}
