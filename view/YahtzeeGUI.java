package view;

import controller.YahtzeeController;
import model.Player;
import model.ScoreCategory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class YahtzeeGUI {

    private static YahtzeeController controller;
    private static JTable scorecardTable;
    private static DefaultTableModel tableModel;
    private static JPanel dicePanel;
    private static JButton rollButton;
    private boolean hasCPU;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main frame
            JFrame frame = createMainFrame();
    
            // Initialize player settings
            YahtzeeGUI yahtzeeGUI = new YahtzeeGUI();
            int numPlayers = yahtzeeGUI.initializePlayers(frame);
    
            // Initialize the controller with the chosen settings
            boolean isCPU = numPlayers == 2 && yahtzeeGUI.hasCPU; // If CPU is enabled, this is true
            controller = new YahtzeeController(numPlayers, isCPU);
    
            // Create and display the GUI
            frame.add(createScorecardPanel(), BorderLayout.WEST);
            frame.add(createDicePanel(), BorderLayout.CENTER);
            frame.add(createCommandPanel(), BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }
    

    private static JFrame createMainFrame() {
        JFrame frame = new JFrame("Yahtzee");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Yahtzee", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        return frame;
    }

    private static JPanel createScorecardPanel() {
        String[] headers = createScorecardHeaders();
        Object[][] data = createScorecardData();
    
        DefaultTableModel tableModel = new DefaultTableModel(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
    
        JTable scorecardTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(scorecardTable);
    
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
    
        return panel;
    }
    
    

    private static JPanel createDicePanel() {
        dicePanel = new JPanel(new GridLayout(1, 5, 10, 10));
        dicePanel.setBackground(Color.LIGHT_GRAY);
        return dicePanel;
    }

    private static JPanel createCommandPanel() {
        JPanel panel = new JPanel();

        // Roll Dice Button
        rollButton = new JButton("Roll Dice");
        rollButton.addActionListener(e -> handleRollDice());
        panel.add(rollButton);

        // Choose Score Button
        JButton chooseScoreButton = new JButton("Choose Score");
        chooseScoreButton.addActionListener(e -> handleChooseScore());
        panel.add(chooseScoreButton);

        return panel;
    }

    private static void handleRollDice() {
        if (controller.rollDice()) {
            updateDicePanel();
        } else {
            JOptionPane.showMessageDialog(null, "No rolls left! Please choose a score.");
        }
    }

    private static String[] createScorecardHeaders() {
        int playerCount = controller.getPlayers().size(); // Fetch the number of players dynamically
        String[] headers = new String[playerCount + 1];
        headers[0] = "Category"; // First column header
        for (int i = 1; i <= playerCount; i++) {
            headers[i] = controller.getPlayers().get(i - 1).getName(); // Player names from the controller
        }
        return headers;
    }
    

    private static Object[][] createScorecardData() {
        int playerCount = controller.getPlayers().size(); // Fetch the number of players dynamically
        int categoryCount = ScoreCategory.values().length;
        Object[][] data = new Object[categoryCount + 1][playerCount + 1]; // +1 for totals/header
    
        // Fill category names in the first column
        for (int i = 0; i < categoryCount; i++) {
            data[i][0] = ScoreCategory.values()[i].name(); // Category name
        }
        data[categoryCount][0] = "Total"; // Total row header
    
        // Fill scores for each player
        for (int col = 1; col <= playerCount; col++) {
            String playerName = controller.getPlayers().get(col - 1).getName();
            for (int row = 0; row < categoryCount; row++) {
                ScoreCategory category = ScoreCategory.values()[row];
                Integer score = controller.getCategoryScoreForPlayer(playerName, category);
                data[row][col] = (score != null) ? score : "-"; // Score or placeholder
            }
            // Fill total score
            data[categoryCount][col] = controller.getPlayers().get(col - 1).getTotalScore();
        }
    
        return data;
    }

    
    
    

    
    

    private static void handleChooseScore() {
        String currentPlayerName = controller.getCurrentPlayerName(); // Get the current player's name
        List<String> categories = controller.getSelectableCategories(currentPlayerName); // Fetch selectable categories
    
        if (categories.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All categories scored!");
            return;
        }
    
        String[] categoryArray = categories.toArray(new String[0]); // Convert list to array for input dialog
        String selected = (String) JOptionPane.showInputDialog(
                null,
                "Choose a scoring category:",
                "Score Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                categoryArray,
                categoryArray[0]
        );
    
        if (selected == null) {
            // User pressed cancel
            return;
        }
    
        // Extract the selected category name (e.g., "ONES (5)" -> "ONES")
        String selectedCategoryName = selected.split(" ")[0];
        ScoreCategory category = ScoreCategory.valueOf(selectedCategoryName);
    
        // Score the selected category
        if (controller.scoreCategory(currentPlayerName, category)) {
            JOptionPane.showMessageDialog(null, "Score saved!");
            updateScores(); // Update the scores table
            updateDicePanel(); // Update the dice panel
        } else {
            JOptionPane.showMessageDialog(null, "Category already scored!");
        }
    }
    

    

    private static void updateDicePanel() {
        dicePanel.removeAll();
        // Populate dicePanel with updated dice data
        dicePanel.revalidate();
        dicePanel.repaint();
    }

    private static void updateScores() {
        Map<String, Integer> playerScores = controller.getPlayerScores();
        List<Player> players = controller.getPlayers();
    
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int columnIndex = i + 1; // Player's column index in the table
            String playerName = player.getName();
            Integer totalScore = playerScores.get(playerName);
    
            // Populate scores for each category
            for (ScoreCategory category : ScoreCategory.values()) {
                int rowIndex = category.ordinal() + 1; // Row index for the category
                Integer score = controller.getCategoryScoreForPlayer(playerName, category);
                tableModel.setValueAt(score != null ? score : "-", rowIndex, columnIndex);
            }
    
            // Update total score
            int totalRowIndex = ScoreCategory.values().length + 1;
            tableModel.setValueAt(totalScore, totalRowIndex, columnIndex);
        }
    }
    
    

    // Initialize players list
    private int initializePlayers(JFrame frame) {
        // prompt for cpu first, no need to prompt number of players if it'll be irrelevant
        boolean cpuBool = promptForBool(frame, "Will this game have a CPU player?");
        this.hasCPU = cpuBool;
        if (this.hasCPU) {
            return 2; // we know if it's a CPU game, we can return that there's 2 players
        }


        int numPlayers = promptForNumber(frame, "Select the Number of Players:", 2, 4);
        if (numPlayers == -1) throw new IllegalStateException("Game initialization canceled."); 
        
        return numPlayers;
    }

    private boolean promptForBool(JFrame frame, String message) {
        int input = JOptionPane.showOptionDialog(
            frame, 
            message, 
            "Add CPU player?", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            null, 
            1);
        return input == 0; // default boolean returned should be false (for no)
    }

    private int promptForNumber(JFrame frame, String message, int min, int max) {
        // Create an array of options based on the range from min to max
        int range = max - min + 1;  // Calculate the total number of options
        String[] options = new String[range];  // Initialize the options array with the size of the range
    
        // Fill the options array with numbers from min to max
        for (int i = 0; i < range; i++) {
            options[i] = String.valueOf(min + i); // Convert each number to a String
        }
    
        // Show the input dialog with the options
        String input = (String) JOptionPane.showInputDialog(
            frame,                
            message,             
            "Select Number",      
            JOptionPane.QUESTION_MESSAGE,  
            null,                
            options,              
            options[0]           
        );
    
        // If input is null (i.e., user canceled), return -1; otherwise, return the selected number
        if (input == null) {
            return -1;  // User canceled
        } else {
            return Integer.parseInt(input);  // Parse and return the selected number
        }
    }
    

}


