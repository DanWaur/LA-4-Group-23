
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YahtzeeGUI {

    public static void main(String[] args) {
    	
    	JFrame frame = new JFrame("Yahtzee");
    	
    	frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        frame.setVisible(true);
        
        // Exit if no input
        if(!playerPrompts(frame)) {
        	System.exit(0);
        }
        
        // Title of Game at top of screen
        JLabel title = new JLabel("YAHTZEE", SwingConstants.CENTER);
        title.setForeground(Color.red);
        title.setFont(new Font("Calibri", Font.BOLD, 28));
        frame.add(title, BorderLayout.NORTH);
    	
    }
    
    
    
    
    /** Prompts user for number of players and computers
     * 
     * @param frame - gui main JFrame
     * @return true if input was given by user, false if input was cancelled or null
     */
    public static boolean playerPrompts(JFrame frame) {
    	String[] playerOptions = {"2","3","4"};
    	String numPlayerInput = (String) JOptionPane.showInputDialog(null, "Select the Number of Players: ", 
    			"Players", JOptionPane.QUESTION_MESSAGE, null, playerOptions, playerOptions[0]);
    	if (numPlayerInput == null) {
    		return false;
    	}
    	int numPlayers = Integer.parseInt(numPlayerInput);
    	
    	String[] computerOptions = {"1","2","3"};
    	String[] comSelectable = Arrays.copyOf(computerOptions, computerOptions.length - (4-numPlayers));
    	String numComputerInput = (String) JOptionPane.showInputDialog(null, "Select the Number of Computers out of the "+numPlayerInput+" players: ", 
    			"Players", JOptionPane.QUESTION_MESSAGE, null, comSelectable, comSelectable[0]);
    	if (numComputerInput == null) {
    		return false;
    	}
    	int numComputers = Integer.parseInt(numComputerInput);
    	
    	return true;
    	
    }
    
    
}
