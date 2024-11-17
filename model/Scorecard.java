import java.util.HashMap;
import java.util.Map;

public class Scorecard {
    private Map<ScoreCategory, Integer> scores;


    private boolean yahtzeeScored; // for yahtzee bonus eligibility
    private int yahtzeeBonusCount; // for additional yahtzee bonuses
    
    public Scorecard() {
        scores = new HashMap<ScoreCategory, Integer>();

        // initialize all scores to null (no score)
        for (ScoreCategory sc : ScoreCategory.values()) {
            scores.put(sc, null);
        }

        yahtzeeScored = false;
        yahtzeeBonusCount = 0;
    }

    /*
     * @param category - the category to score
     * @param diceValues - an array of dice values
     * @return - true if the score was registered, false if it was already scored
     */
    public boolean score(ScoreCategory category, int[] diceValues) {
        if (scores.get(category) != null) {
            return false; // already scored that category
        }

        int score = calculateScoreForCategory(category, diceValues);

        // yahtzee check for bonus
        if (category == ScoreCategory.YAHTZEE && score == 50) {
            if (yahtzeeScored) {
                yahtzeeBonusCount++;
            } 
            else {
                yahtzeeScored = true;
            }
        }
        scores.put(category, score);
        return true;
    }

    // TODO: implement this method to calculate the total score
    public int getTotalScore() {
        return 0; // placeholder
    }


    // HELPER METHODS BEGIN HERE

    // TODO: implement this method to calculate the score for a given category
    private int calculateScoreForCategory(ScoreCategory category, int[] diceValues) {
        switch (category) {
            case ONES:
                return sumOfDice(diceValues, 1);
            case TWOS:
                return sumOfDice(diceValues, 2);
            case THREES:
                return sumOfDice(diceValues, 3);
            case FOURS:
                return sumOfDice(diceValues, 4);
            case FIVES:
                return sumOfDice(diceValues, 5);
            case SIXES:
                return sumOfDice(diceValues, 6);
            // TODO: implement the rest of the more complicated cases for lower categories


            default: return 0;
    }
    
}

    /*
     * @param diceValues - an array of dice values
     * @param toSum - the value to sum
     * @return - the sum of the dice values according to the toSum parameter
     */
    private int sumOfDice(int[] diceValues, int toSum) {
        int sum = 0;
        for (int val : diceValues) {
            if (val == toSum) {
                sum++;
            }
        }
        return sum;
    }
}