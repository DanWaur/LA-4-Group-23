package model;
/*
 * Juan Rogel Acedo (jarogelacedo)
 * Name (netid)
 * Name (netid)
 * Name (netid)
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scorecard {
    private Map<ScoreCategory, Integer> scores;


    private boolean yahtzeeScored; // for yahtzee bonus eligibility
    private int yahtzeeBonusCount; // for additional yahtzee bonuses

    public Scorecard() {
        scores = new HashMap<>();

        // initialize all scores to null (no score)
        for (ScoreCategory sc : ScoreCategory.values()) {
            scores.put(sc, null);
        }

        yahtzeeScored = false;
        yahtzeeBonusCount = 0;
    }

    /**
     * place a score in the given category
     * @param category - the category to score
     * @param diceValues - an array of dice values
     * @return - true if the score was registered, false if it was already scored
     */
    public boolean score(ScoreCategory category, int[] diceValues) {
        if (scores.get(category) != null) {
            return false; // already scored that category
        }

        int score = calculateScoreForCategory(category, diceValues);

        // check for yahtzee bonus before checking if current roll is a yahtzee
        if (yahtzeeScored && checkOfAKind(diceValues, 5)){
            yahtzeeBonusCount++;
        }

        if (category == ScoreCategory.YAHTZEE && score == 50) {
            yahtzeeScored = true;
        }

        scores.put(category, score);
        return true; // score was registered successfully
    }

    /**
     * calculates the total score of this scorecard, taking into account bonuses
     * @return the total score of this scorecard
     */
    public int getTotalScore() {
        int total = 0;

         for (ScoreCategory sc : ScoreCategory.values()) {
            // check bonus codition when we get to the first lower category
            if (sc == ScoreCategory.THREE_OF_A_KIND && total >= 63) {
                total += 35;
            }

            if (scores.get(sc) != null){
                total += scores.get(sc);
            }
        }

        // apply yahtzee bonuses
        for (int i = 0; i < yahtzeeBonusCount; i++){
            total += 100;
        }

        return total;
    }

    /**
     * @return - true if the Scorecard is complete, false otherwis
     */
    public boolean isComplete() {
        return !scores.containsValue(null);
    }


    

    /**
     * Calculates the score for a given category
     * @param category - the category to calculate the score for
     * @param diceValues - an array of dice values
     * @return - the score for the given category
     */
    public int calculateScoreForCategory(ScoreCategory category, int[] diceValues) {
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
            case THREE_OF_A_KIND:
                if (checkOfAKind(diceValues, 3)) {
                    return sumOfAllDice(diceValues);
                }
                else{ return 0;}
            case FOUR_OF_A_KIND:
                if (checkOfAKind(diceValues, 4)) {
                    return sumOfAllDice(diceValues);
                }
                else{ return 0;}
            case FULL_HOUSE:
                if (checkFullHouse(diceValues)) {
                    return 25;
                }
                else{ return 0;}
            case SMALL_STRAIGHT:
                if (hasConsecutiveNumbers(diceValues, 4)) {
                    return 30;
                }
                else { return 0; }
            case LARGE_STRAIGHT:
                if (hasConsecutiveNumbers(diceValues, 5)) {
                    return 40;
                }
                else { return 0; }
            case YAHTZEE:
                if (checkOfAKind(diceValues, 5)) {
                    return 50;
                }
                else { return 0; }
            case CHANCE:
                return sumOfAllDice(diceValues);

            default: return 0;
        }
    }
    
    // HELPER METHODS BEGIN HERE

    /**
     * Sums the dice values according to the toSum parameter
     * @param diceValues - an array of dice values
     * @param toSum - the value to sum
     * @return - the sum of the dice values according to the toSum parameter
     */
    private int sumOfDice(int[] diceValues, int toSum) {
        int sum = 0;
        for (int val : diceValues) {
            if (val == toSum) {
                sum += val;
            }
        }
        return sum;
    }

    /**
     * checks if the diceValues array contains k equal values
     * @param diceValues - an array of dice values
     * @param k - how many equal values to check for
     * @return - true if the diceValues array contains k equal values, false otherwise
     */
    private boolean checkOfAKind(int[] diceValues, int k) {
        int[] frequencies = new int[7]; // so we can index the array with the dice values
        for (int val : diceValues) {
            frequencies[val]++;
        }

        // check if the frequencies array contains k equal values
        for (int element : frequencies) {
            if (element == k) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sums the dice values in the array
     * @param diceValues - an array of dice values
     * @return - the sum of the dice values in the array
     */
    private int sumOfAllDice(int[] diceValues) {
        int sum = 0;
        for (int val : diceValues){
            sum += val;
        }
        return sum;
    }

    /**
     * Checks if the diceValues array makes up a full house (three of one kind, two of another kind)
     * @param diceValues - an array of dice values
     * @return - true if the diceValues array contains a full house, false otherwise
     */
    private boolean checkFullHouse(int[] diceValues) {
        int[] frequencies = new int[7]; // so we can index the array with the dice values
        for (int val : diceValues) {
            frequencies[val]++;
        }

        boolean hasThree =false;
        boolean hasTwo = false;
        for (int element : frequencies) {
            if (element == 3) {
                hasThree = true;
            }
            if (element == 2) {
                hasTwo = true;
            }
        }
        return hasThree && hasTwo;
    }

    /**
     * checks if there are k consecutive numbers in the array, not necessarily in order
     * @param diceValues the array of dice values
     * @param k the number of consecutive numbers to check for
     * @return true if there are k consecutive numbers in the array, not necessarily in order
     */
    private boolean hasConsecutiveNumbers(int[] diceValues, int k) {
        ArrayList<Integer> sortedVals = new ArrayList<>();
        for (int val : diceValues) {
            sortedVals.add(val);
        }

        Collections.sort(sortedVals);
        int consecutive = 1;

        // check if there are k consecutive numbers
        for (int i = 1; i < sortedVals.size(); i++) {
            if (sortedVals.get(i) - sortedVals.get(i-1) == 1) {
                consecutive++;
            }
            else {
                consecutive = 1;
            }
            if (consecutive == k) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns the score for a given category, null if it hasn't been scored
     * @param category - the category to retrieve the score for
     * @return the score for the given category, or null if it hasn't been scored
     * // NOTE: no need to return the whole map, just the score for the given category passed in
     */
    public Integer getScoreForCategory(ScoreCategory category) {
        return scores.get(category);
    }

    /**
     * @return - a list of all the categories that have not been scored
     */
    public List<ScoreCategory> getAvailableCategories() {
        ArrayList<ScoreCategory> availableCategories = new ArrayList<>();
        for (ScoreCategory sc : ScoreCategory.values()) {
            if (scores.get(sc) == null) {
                availableCategories.add(sc);
            }
        }
        return availableCategories;
    }
}