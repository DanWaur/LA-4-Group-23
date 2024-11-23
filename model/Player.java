package model;
/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (netid)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

import java.util.ArrayList;
import java.util.List;

public class Player {
    private static final int MAX_ROLLS = 3;
    private static final int NUM_DICE = 5;
    private List<Dice> dice;
    private int rollsLeft;
    private Scorecard scoreCard;

    public Player() {
        dice = new ArrayList<>();
        for (int i = 0; i < NUM_DICE; i++) {
            dice.add(Dice.get(i));
        }
        scoreCard = new Scorecard();
        resetGame();
    }

    /**
     * Rolls all dice that are not held if rolls are remaining.
     * @return true if the dice were rolled successfully, false if no rolls are left.
     */
    public boolean rollDice() {
        if (rollsLeft > 0) {
            for (Dice die : dice) {
                die.roll();
            }
            rollsLeft--;
            return true; // successfull roll
        }
        return false; // no rolls left
    }

    /**
     * Toggles the hold state of the specified dice.
     * @param dicePos - a list of indices representing the dice to toggle.
     */
    public void toggleDice(List<Integer> dicePos) {
        for (int dieIndex : dicePos) {
            // Validate index and toggle only specific dice
            if (dieIndex >= 0 && dieIndex < NUM_DICE) {
                dice.get(dieIndex).toggleHold();
            }
        }
    }

    /**
     * Scores a specified category using the current dice values.
     * Assumes the GUI prevents selecting already-scored categories.
     * @param scoreChoice - the category to score.
     * @return true if scored successfully, false otherwise
     */
    public boolean chooseScore(ScoreCategory scoreChoice) {
        int[] diceValues = getDiceValues();
        return scoreCard.score(scoreChoice, diceValues); // Perform the scoring
    }
    /**
     * Converts the current dice values into an array of integers.
     * @return an array representing the values of the dice (1-6).
     */
    private int[] getDiceValues() {
        int[] values = new int[dice.size()];
        for (int i = 0; i < dice.size(); i++) {
            values[i] = dice.get(i).getFaceVal(); // Convert DiceValue to 1-6
        }
        return values;
    }

    /**
     * Resets the player's game state, including rolls left, dice hold states, and scorecard.
     */
    public void resetGame() {
       // Reset the rolls left for the player
        rollsLeft = MAX_ROLLS;

        // Reset the dice: unhold all dice
        for (Dice die : dice) {
            if (die.isHeld()) {
                die.setHold(false); // Ensure all dice are unheld
            }
        }

        // Reset the scorecard
        scoreCard = new Scorecard();
    }

    /**
     * Resets the player's game state for a new turn, including rolls left and dice hold states
     */
    public void resetTurn() {
        // Reset the rolls left for the player
         rollsLeft = MAX_ROLLS;
 
         // Reset the dice: unhold all dice
         for (Dice die : dice) {
             if (die.isHeld()) {
                 die.setHold(false); // Ensure all dice are unheld
             }
         }
     }

    /**
     * Gets the number of rolls remaining for the player.
     * for GUI purposes to be able to display it.
     * @return the number of rolls left.
     */
    public int getRollsLeft() {
        return rollsLeft;
    }

    /**
     * Retrieves the current faces of all dice.
     * @return a list of DiceValue objects representing the faces of the dice.
     */
    public List<DiceValue> getDiceFaces() {
        List<DiceValue> faces = new ArrayList<>();
        for (Dice die : dice) {
            faces.add(die.getFace());
        }
        return faces;
    }

    /**
     * Retrieves the hold states of all dice.
     * @return a list of booleans where true indicates the die is held.
     */
    public List<Boolean> getDiceHoldStates() {
        List<Boolean> holdStates = new ArrayList<>();
        for (Dice die : dice) {
            holdStates.add(die.isHeld());
        }
        return holdStates;
    }

    /**
     * Retrieves the score for a specified category.
     * @param category - the score category to retrieve.
     * @return the score for the specified category, or null if it hasn't been scored.
     */
    public Integer getScoreForCategory(ScoreCategory category) {
        return scoreCard.getScores().get(category);
    }

    /**
     * Retrieves the total score of the player from the scorecard.
     * @return the total score.
     */
    public int getTotalScore() {
        return scoreCard.getTotalScore();
    }

}