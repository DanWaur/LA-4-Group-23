package view;

import controller.YahtzeeController;
import model.Player;
import model.ScoreCategory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class YahtzeeGUI {

    public JTable scorecardTable;
    private DefaultTableModel tableModel;
    private JPanel dicePanel;
    private JButton rollButton;
    private YahtzeeController controller;
    private List<Player> players;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            YahtzeeGUI gui = new YahtzeeGUI();
            gui.setupGame();
        });
    }

    private void setupGame() {
        JFrame frame = createMainFrame();

        try {
            // Initialize players
            players = initializePlayers(frame);

            // Initialize the controller and pass YahtzeeGUI
            controller = new YahtzeeController(this);
            controller.initializeGame(frame);

            // Set up the UI layout with scorecard, dice panel, and command panel
            frame.add(createScorecardPanel(controller.getPlayerNames()), BorderLayout.WEST);
            frame.add(createDicePanel(), BorderLayout.CENTER);
            frame.add(createCommandPanel(), BorderLayout.SOUTH);

            frame.setVisible(true);
        } catch (IllegalStateException e) {
            System.exit(0); // Exit if game setup fails
        }
    }

    private JLabel createDiceLabel(String face, boolean isHeld, int index) {
        String diceImagePath = "resources/dice-" + face + ".png"; // Path to dice image
        ImageIcon diceIcon = new ImageIcon(new ImageIcon(diceImagePath).getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
        JLabel diceLabel = new JLabel(diceIcon, SwingConstants.CENTER);
        
        // Add a red border if the die is held (we will make this look better)
        if (isHeld) {
            diceLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));  // Red border for held dice
        } 
        
        // Add mouse listener to toggle the dice hold state
        diceLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
    
                // Toggle the dice state and update the display
                controller.handleToggleDice(index);
            }
        });
        
        return diceLabel;
    }
    

    private JFrame createMainFrame() {
        JFrame frame = new JFrame("Yahtzee");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Yahtzee", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        return frame;
    }

    private JPanel createScorecardPanel(List<String> playerNames) {
        JPanel scorecardPanel = new JPanel(new BorderLayout());
        String[] scorecardColumnHeaders = createScorecardHeaders(playerNames);
        Object[][] scorecardData = createScorecardData();
    
        tableModel = new DefaultTableModel(scorecardData, scorecardColumnHeaders) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing in the scorecard table
            }
        };
        scorecardTable = new JTable(tableModel);
        scorecardPanel.add(new JScrollPane(scorecardTable), BorderLayout.CENTER);
    
        return scorecardPanel;
    }
    

    private String[] createScorecardHeaders(List<String> playerNames) {
        String[] headers = new String[playerNames.size() + 1];
        headers[0] = "Category"; // First column is for the score categories
        for (int i = 0; i < playerNames.size(); i++) {
            headers[i + 1] = playerNames.get(i); // Add player names as column headers
        }
        return headers;
    }

    private Object[][] createScorecardData() {
        Object[][] data = new Object[ScoreCategory.values().length + 1][2];
        data[0][0] = "Total"; // Header row for total score
        for (int i = 0; i < ScoreCategory.values().length; i++) {
            data[i + 1][0] = ScoreCategory.values()[i].name(); // List score categories
        }
        return data;
    }

    private JPanel createDicePanel() {
        dicePanel = new JPanel(new GridLayout(1, 5, 10, 10)); // Panel to hold dice
        dicePanel.setBackground(Color.LIGHT_GRAY);
        return dicePanel;
    }

    private JPanel createCommandPanel() {
        JPanel panel = new JPanel();

        rollButton = new JButton("Roll Dice");
        rollButton.addActionListener(e -> controller.handleRollDice()); // Button to roll dice
        panel.add(rollButton);

        JButton chooseScoreButton = new JButton("Choose Score");
        chooseScoreButton.addActionListener(e -> controller.handleChooseScore()); // Button to choose score
        panel.add(chooseScoreButton);

        return panel;
    }

    public void updateDiceDisplay(Player player) {
        dicePanel.removeAll(); // Clear current dice display
        List<String> diceFaces = player.getDiceFacesAsStrings();
        List<Boolean> diceHolds = player.getDiceHoldStates();
    
        // Update dice display for each die
        for (int i = 0; i < diceFaces.size(); i++) {
            JLabel diceLabel;
            if (diceFaces.get(i) == null) {
                diceLabel = new JLabel(new ImageIcon("resources/placeholder-dice.png"), SwingConstants.CENTER); // Placeholder for unset dice
            } else {
                diceLabel = createDiceLabel(diceFaces.get(i), diceHolds.get(i), i); // Create label for dice
            }
            dicePanel.add(diceLabel);
        }
    
        dicePanel.revalidate(); // Refresh dice panel
        dicePanel.repaint(); // Repaint the panel to show updates
    }

    public void updateScore(int rowIndex, int columnIndex, String value) {
        tableModel.setValueAt(value, rowIndex, columnIndex); // Update the score in the table
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message); // Show a message dialog
    }

    public String showInputDialog(String message, String[] options) {
        return (String) JOptionPane.showInputDialog(
                null,
                message,
                "Input",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        ); // Show input dialog and return selected option
    }

    // Initialize players list
    private List<Player> initializePlayers(JFrame frame) {
        List<Player> players = new ArrayList<>();
        int numPlayers = promptForNumber(frame, "Select the Number of Players:", 2, 4);
        if (numPlayers == -1) throw new IllegalStateException("Game initialization canceled.");

        int numCPUs = promptForNumber(frame, "Select the Number of Computers:", 0, numPlayers);
        if (numCPUs == -1) throw new IllegalStateException("Game initialization canceled.");

        // Add human players
        for (int i = 0; i < numPlayers - numCPUs; i++) {
            players.add(new Player()); // Add human players
        }

        // Add CPU players (if any)
        for (int i = 0; i < numCPUs; i++) {
            players.add(new Player()); // Add CPU players
        }

        return players;
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

    // Method to get the players list (so controller can access it)
    public List<Player> getPlayers() {
        return players;  // Return the players list
    }

    
    

    // This method returns the list of player names from the controller
    public List<String> getPlayerNames() {
        return controller.getPlayerNames();  // Retrieve player names from the controller
    }
}
