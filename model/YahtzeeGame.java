/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (dreynaldo)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

package model;
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
     * Scores a category for the current player
     * @param scoreChoice - the category to score.
     * @pre scoreChoice can be null for the CPU player, will be ignored
     * @return true if the score was recorded successfully, false if the category was already scored.
     */
    public boolean chooseScore(ScoreCategory scoreChoice) {

        Player currentPlayer = getCurrentPlayer(); 
        if (!currentPlayer.hasRolled()) {
            return false; // dice unrolled
        }
        if (currentPlayer.chooseScore(scoreChoice)) {
            return true; // scored successfully
        }
        return false; // category already scored
    }
    
    
    public ScoreCategory getCpuAim() {
    	if (!isCurrentPlayerCPU()) {
    		return null;
    	}
    	
    	Cpu cpu = (Cpu) getCurrentPlayer();
    	
    	if (!cpu.hasRolled()) {
    		return cpu.firstRoll();
    	}
    	else {
    		return null;
    	}
    }
    
    public ScoreCategory iterateCpuChoices(ScoreCategory aim) {
    	if (!isCurrentPlayerCPU()) {
    		return null;
    	}
    	
    	Cpu cpu = (Cpu) getCurrentPlayer();


    	ScoreCategory result = cpu.makeDecision(aim);
    	
    	
    	return result;
    	
    }


    /**
     * helper method to get the current player
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

//    /**
//     * helper method to get the CPU player only used when it's the CPU's turn
//     * @return the CPU player
//     */
//    private Cpu getCPU() {
//        return (Cpu) players.get(currentPlayerIndex);
//    }

    /**
     * helper method to advance the current turn to the next player, possibly next round
     * @post currentPlayerIndex will be incremented by 1 or reset to 0 if it reaches the last player
     * @post currentRound will be incremented by 1 if it reaches the last player
     */

    //  double check this, made this public
    public void advanceTurn() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0; // loop back to the first player
            currentRound++; // advance to next round
            
        }
        getCurrentPlayer().resetTurn();
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

    public List<Player> getPlayers() {
        return players;
    }

    public Integer getCategoryScoreForPlayer(String playerName, ScoreCategory category) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player.getScoreForCategory(category); // Use Player's method to get the category score
            }
        }
        return null; // Return null if the player or category is not found
    }

    public List<String> getSelectableCategories(String playerName) {
        Player player = null;
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                player = p;
                break;
            }
        }
    
        if (player == null) {
            throw new IllegalArgumentException("Player not found: " + playerName);
        }
    
        List<String> selectableCategories = new ArrayList<>();
        for (ScoreCategory category : ScoreCategory.values()) {
            if (!player.isScored(category)) { // Check if the category is already scored
                int potentialScore = player.calculateScoreForCategory(category);
                selectableCategories.add(category.name() + " (" + potentialScore + ")");
            }
        }
        return selectableCategories;
    }
    
    public boolean scoreCategoryForPlayer(String playerName, ScoreCategory category) {
        Player player = null;
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                player = p;
                break;
            }
        }
    
        if (player == null) {
            throw new IllegalArgumentException("Player not found: " + playerName);
        }
    
        // return player.scoreCategory(category); // Use the existing method to score the category
        return player.chooseScore(category);
    }
    
    


    public String getCurrentPlayerName() {
        return getCurrentPlayer().getName();
    }


    private Player getPlayerByName(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return null; // Player not found
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    
    
    public boolean hasPlayerRolled(String playerName) {
        Player player = getPlayerByName(playerName);
        return player != null && player.hasRolled(); // Ensure player exists and has rolled
    }


    public Map<ScoreCategory, Integer> calculatePotentialScoresForPlayer(String playerName) {
        Player player = getPlayerByName(playerName);
        if (player == null) {
            throw new IllegalArgumentException("Player not found: " + playerName);
        }
    
        Map<ScoreCategory, Integer> potentialScores = new HashMap<>();
        for (ScoreCategory category : ScoreCategory.values()) {
            if (!player.isScored(category)) {
                int score = player.calculateScoreForCategory(category);
                potentialScores.put(category, score);
            } else {
                potentialScores.put(category, player.getScoreForCategory(category));
            }
        }
        return potentialScores;
    }
    
    
    
    public List<DiceValue> getCurrentPlayerDiceFaces() {
        return getCurrentPlayer().getDiceFaces(); // Delegate to the current player's method
    }
    
    public List<Boolean> getCurrentPlayerDiceHolds() {
        return getCurrentPlayer().getDiceHoldStates(); // Delegate to the current player's method
    }
    

    
    
}


