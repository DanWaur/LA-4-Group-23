package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YahtzeeGame {

    private static final int MAX_ROUNDS = 13;
    private final List<Player> players;
    private int currentPlayerIndex;
    private int currentRound;

    /**
     * creates a new YahtzeeGame with the specified number of players
     * @param numPlayers - the number of players to create
     * @pre numPlayers >= 2 && numPlayers <= 4
     */
    public YahtzeeGame(int numPlayers) {
        players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
            // TODO decide how CPU players should be initialized
        }
        currentPlayerIndex = 0;
        currentRound = 1;
    }

    /**
     * Rolls the dice for the current player if they have rolls remaining.
     * @return true if the dice were rolled successfully, false if no rolls are left.
     */
    public boolean rollDice() {
        return getCurrentPlayer().rollDice();
    }

    /**
     * Toggles the hold state of specific dice for the current player.
     * @param diceIndices - a list of indices representing the dice to toggle.
     */
    public void toggleDice(List<Integer> diceIndices) {
        getCurrentPlayer().toggleDice(diceIndices);
    }

    /**
     * Scores a category for the current player and advances the game state.
     * @param scoreChoice - the category to score.
     * @return true if the score was recorded successfully, false if the category was already scored.
     */
    public boolean chooseScore(ScoreCategory scoreChoice) {
        Player currentPlayer = getCurrentPlayer(); 
        if (currentPlayer.chooseScore(scoreChoice)) {
            advanceTurn(); // move to the next player (or round)
            return true; // scored successfully
        }
        return false; // category already scored
    }


    /**
     * helper method to get the current player
     * @return the current player
     */
    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * helper method to advance the current turn to the next player, possibly next round
     * @post currentPlayerIndex will be incremented by 1 or reset to 0 if it reaches the last player
     * @post currentRound will be incremented by 1 if it reaches the last player
     */
    private void advanceTurn() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0; // loop back to the first player
            currentRound++; // advance to the next round
        }
        if (currentRound <= MAX_ROUNDS) {
            getCurrentPlayer().resetTurn(); // Reset dice and rolls for the next player
        }
    }

    /**
     * Checks if the game has ended.
     * @return true if all rounds have been completed, false otherwise.
     */
    public boolean isGameOver() {
        return currentRound > MAX_ROUNDS;
    }

    /**
     * Retrieves the winner(s) of the game based on the total scores.
     * @return a list of players with the highest scores.
     */
    public List<Player> getWinners() {
        // could still have only one winner, but ties are possible so we use a list
        List<Player> winners = new ArrayList<>();
        if (!isGameOver()) {
            return winners; // game hasn't ended yet, empty list
        }
        List<Integer> finalScores = new ArrayList<>();
        for (Player player : players) {
            finalScores.add(player.getTotalScore());
        }

        int highestScore = Collections.max(finalScores);
        for (Player player : players) {
            if (player.getTotalScore() == highestScore) {
                winners.add(player);
            }
        }
        return winners;
    }

    /**
     * @return the current round of the game.
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Returns the score of the specified player.
     * @param playerIndex - the index of the player to retrieve the score for.
     * @return the score of the specified player.
     */
    public int getPlayerScore(int playerIndex) {
        return players.get(playerIndex).getTotalScore();
    }
}

