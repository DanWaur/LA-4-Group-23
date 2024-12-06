package model;

/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (netid)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YahtzeeGame {

    private static final int MAX_ROUNDS = 13;
    private final List<Player> players;
    private int currentPlayerIndex;
    private int currentRound;

    /**
     * creates a new YahtzeeGame with the specified number of players
     * @param numPlayers - the number of players to create
     * @param cpu - true if the game will be played against CPU, false if 2-4 player game
     * @pre numPlayers >= 2 && numPlayers <= 4
     */
    public YahtzeeGame(int numPlayers, boolean cpu) {
        players = new ArrayList<>();
        if (cpu) {
            players.add(new Player("Player 1"));
            players.add(new Cpu());
        }
        else{
            String playerName;
            for (int i = 0; i < numPlayers; i++) {
                playerName = "Player " + (i + 1);
                players.add(new Player(playerName));
            }
        } 
        currentPlayerIndex = 0;
        currentRound = 1;
    }

    /**
     * Rolls the dice for the current player if they have rolls remaining.
     * For human players only, the CPU player will handle rolls internally.
     * @return true if the dice were rolled successfully, false if no rolls are left.
     */
    public boolean rollDice() {
        return getCurrentPlayer().rollDice();
    }

    /**
     * Toggles the hold state of specific dice for the current player.
     * For human players only, the CPU player will handle toggles internally.
     * @param diceIndices - a list of indices representing the dice to toggle.
     */
    public boolean toggleDice(List<Integer> diceIndices) {
        Player currPlayer = getCurrentPlayer();
        if (!currPlayer.hasRolled()) {
            return false;
        }

        currPlayer.toggleDice(diceIndices);
        return true;
    }

    /**
     * Scores a category for the current player and advances the game state.
     * @param scoreChoice - the category to score.
     * @pre scoreChoice can be null for the CPU player, will be ignored
     * @return true if the score was recorded successfully, false if the category was already scored.
     */
    public boolean chooseScore(ScoreCategory scoreChoice) {
        if (isCurrentPlayerCPU()) {
            Cpu cpu = getCPU();
            scoreChoice = cpu.firstRoll();
            boolean result = cpu.makeDecision(scoreChoice);
        	while (result) {
        		result = cpu.makeDecision(scoreChoice);
        	}
            advanceTurn(); // move to the next player (or round)
            return true; // scored successfully
        }

        Player currentPlayer = getCurrentPlayer(); 
        if (!currentPlayer.hasRolled()) {
            return false; // dice unrolled
        }
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
     * helper method to get the CPU player only used when it's the CPU's turn
     * @return the CPU player
     */
    private Cpu getCPU() {
        return (Cpu) players.get(currentPlayerIndex);
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
     * Retrieves the Players in this game and their scores, sorted by score in descending order.
     * @return a map of the players' scores mapped to their names.
     */
    public Map<String, Integer> getPlayerScores() {
        Map<String, Integer> playerScores = new HashMap<>();

        // sort in descending order by scores
        List<Player> playersCopy = new ArrayList<>(players);
        Collections.sort(playersCopy, Collections.reverseOrder());

        // map scores to names
        for (Player player : playersCopy) {
            playerScores.put(player.getName(), player.getTotalScore());
        }

        return playerScores;
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
     * @pre playerIndex >= 0 && playerIndex < players.size()
     * @return the score of the specified player.
     */
    public int getPlayerScore(int playerIndex) {
        return players.get(playerIndex).getTotalScore();
    }
    
    /**
     * checks if the current player is the CPU
     * @return true if the current player is the CPU, false otherwise
     */
    public boolean isCurrentPlayerCPU() {
        return players.get(currentPlayerIndex).getClass() == Cpu.class;
    }
}

