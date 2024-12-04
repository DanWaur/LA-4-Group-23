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
	
	
    public Cpu() {
    	super("CPU");
    }
	
    
    /** Determines what dice to keep and what category to aim for on a first roll
     * 
     * @pre This is to be the first action by the Cpu on its turn
     * @return ScoreCategory representing the category to aim for
     */
    public ScoreCategory firstRoll() {
    	boolean rolled = this.rollDice();
    	if (rolled == false) {
    		return null;
    	}
    	
    	// Score category to aim for is determined on best possible score, with an additional adjustment
    	ScoreCategory aim = chooseAimFor(this.getBestScore(), this.getDiceValues());
    	
    	List<Integer> kept = this.getDiceKeepForNext(aim, this.getDiceValues());
    	this.toggleDice(kept);
    	
    	return aim;
    	
    }
    
    /** Rolls unheld dice and scores if aimFor can be scored in the scoreCard. This
     * Function can repeatedly be called until something is scored or there are no rolls left.
     * 
     * @pre This is to be called after firstRoll() is called on the cpu per turn
     * @param aimFor is the category the cpu attempts to achieve.
     * @return true if the cpu can make another decision, false if it will not and scored in the scoreCard.
     */
    public boolean makeDecision(ScoreCategory aimFor) {

    	
    	boolean rolled = this.rollDice();
    	
    	// Cpu cannot roll again, check if can score in aim, then score best score otherwise
    	if (rolled == false) {
    		
    		if (this.calculateScoreForCategory(aimFor) != 0) {
    			this.chooseScore(aimFor);
    		}
    		else {
    			
    			ScoreCategory best = this.getBestScore();
    			this.chooseScore(best);
    		}
    		
    		// End of cpu turn
    		return false;
    	}
    	
    	// Cpu can score in aim category, score.
    	if (this.calculateScoreForCategory(aimFor) != 0) {
    		this.chooseScore(aimFor);
    		
    		// End of cpu turn
    		return false;
    	}

    	
    	// Cpu wants to make another turn/roll
    	return true;
    }
    

    /**
     * Select which score to aim for based on the best current score.
     * @return bestCat - a ScoreCategory which has the most points
     */
	public ScoreCategory chooseAimFor(ScoreCategory current, int[] vals) {
		int pairSize = tryGetPairFrom(5, vals).size();
		ScoreCategory aimFor;
		
		if (current == ScoreCategory.ONES || current == ScoreCategory.TWOS 
				|| current == ScoreCategory.THREES || current == ScoreCategory.FOURS
				|| current == ScoreCategory.FIVES || current == ScoreCategory.SIXES) {
			aimFor = current;
		}
		else if (pairSize >= 4 && !this.isScored(ScoreCategory.YAHTZEE)) {
			aimFor = ScoreCategory.YAHTZEE;
		}
		else if (pairSize == 3 && !this.isScored(ScoreCategory.FULL_HOUSE)) {
			aimFor = ScoreCategory.FULL_HOUSE;
		}
		else if (pairSize == 3 && !this.isScored(ScoreCategory.FOUR_OF_A_KIND)) {
			aimFor = ScoreCategory.FOUR_OF_A_KIND;
		}
		else if (pairSize == 2 && !this.isScored(ScoreCategory.THREE_OF_A_KIND)) {
			aimFor = ScoreCategory.THREE_OF_A_KIND;
		}
		else {
			aimFor = getBestScore();
		}
		
		
		return aimFor;
	}
	
    /**
     * Get the current best score in potential scoring categories.
     * @return bestCat - a ScoreCategory which has the most points
     */
	public ScoreCategory getBestScore() {
		ScoreCategory bestCat = null;
		int bestVal = -1;
		
		for (ScoreCategory category : ScoreCategory.values()) {
			
			if(!this.isScored(category)) {
				int current = this.calculateScoreForCategory(category);
				
				if (current >= bestVal) {
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
	public List<Integer> getDiceKeepForNext(ScoreCategory aimFor, int[] vals) {
		
		List<Integer> selectPos = new ArrayList<Integer>();
		
		switch (aimFor) {
			case ONES:
	        	selectPos = getOfValue(1, vals);
	        	
	        	break;

	        case TWOS:
	        	selectPos = getOfValue(2, vals);
	        	
	        	break;
	            
	        case THREES:
	        	selectPos = getOfValue(3, vals);
	        	
	        	break;
	            
	        case FOURS:
	        	selectPos = getOfValue(4, vals);
	        	
	        	break;
	            
	        case FIVES:
	        	selectPos = getOfValue(5, vals);
	        	
	        	break;
	            
	        case SIXES:
	        	selectPos = getOfValue(6, vals);
	        	
	        	break;
	            
	        case THREE_OF_A_KIND:
	        	selectPos = tryGetPairFrom(3, vals);
	        	
	        	break;
	        	
	        case FOUR_OF_A_KIND:
	        	selectPos = tryGetPairFrom(4, vals);
	        	
	        	break;
	        	
	        case FULL_HOUSE:
	        	selectPos = tryGetPairFrom(3,vals);
	        	
	        	break;
	        	
	        case SMALL_STRAIGHT:
	        	break;
	        	
	        case LARGE_STRAIGHT:
	        	break;
	        	
	        case YAHTZEE:
	        	selectPos = tryGetPairFrom(5, vals);
	        	
	        	break;
	        	
	        case CHANCE:
	        	
	        	break;

		}
		
		return selectPos;
	}
	
	
    /**
     * Continuously iterate down from a start pair size, trying
     * to get the indices of the pairs.
     * @param start - pair size to start from
     * @return - List<Integer> representing the indices in the dice array that are a pair.
     */
	private List<Integer> tryGetPairFrom(int start, int[] vals){
		
		List<Integer> startPos = new ArrayList<Integer>();
		
		int current = start;
		while (current >= 2 && startPos.size() == 0) {
			
			startPos = this.getOfKind(current, vals);
			current--;
		}
		if (startPos.size() == 0) {
			startPos = this.getSingleHighest(vals);
		}
		
		
		return startPos;
		
	}
	
    /**
     * Gets positions of a pair of size.
     * @param pairAmount - Size of pair to find
     * @return - List<Integer> representing the indices in the dice array that are a pair.
     */
	private List<Integer> getOfKind(int pairAmount, int[] vals) {
		List<Integer> selectPos = new ArrayList<Integer>();
		
		// Count all dice amounts
		int count[] = new int[7];
		for (int val : vals) {
			count[val]++;
		}
		
		// Identify pair that fits pair amount
		int pairVal = 0;
		
		// For each of the dice values,
		for (int i = 6; i > 0; i--) {
			
			if (count[i] >= pairAmount) {
				pairVal = i;
				break;
			}
		}
		// No pair found
		if (pairVal == 0) {
			return new ArrayList<Integer>();
		}
		
		int collected = 0;
		// Otherwise, Collect positions of pair values
		for (int i = 0; i < vals.length && collected < pairAmount; i++) {
			if (vals[i] == pairVal) {
				collected++;
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
	private List<Integer> getOfValue(int value, int[] vals) {

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
	private List<Integer> getSingleHighest(int[] vals) {
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
