package OthelloUI;

import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.BorderFactory;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * A GUI that displays an interactive version of Othello to allow users
 * to play the game.
 */
public class OthelloGUI
{
    // the size of the frame
    private static final int FRAME_HEIGHT = 800;
    private static final int FRAME_WIDTH = 700;
    // font that will be used to displays the text on labels;
    private static final Font PLAIN_LABEL_FONT = new Font("American Typewriter", Font.PLAIN, 15);

    // the frame that will display the GUI
    private JFrame frame;
    // this frame will contain tabs - a tab to the main menu and othello games that the user played
    // while the GUI is active
    private JTabbedPane tabs;
    // tracks how many games that user has started since this GUI has launched.
    private int totalGames;

    // lists the players that are available to play
    private JComboBox blackPlayerOptions;
    private JComboBox whitePlayerOptions;



    /**
     * Constructs an othello gui that the user can interact with
     */
    public OthelloGUI()
    {
        totalGames = 0;
        frame = new JFrame("Othello");
        tabs = new JTabbedPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);

        // a panel that will contain the components that will be displayed on the Menu.
        JPanel menuPanel = setUpMainMenuPanel();
        tabs.addTab("Menu", menuPanel);
        frame.add(tabs);
        frame.setVisible(true);
    }


    /**
     * Sets up and returns the Main menu panel where users will be able to select their players and start a game of othello.
     */
    private JPanel setUpMainMenuPanel()
    {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBorder(BorderFactory.createRaisedBevelBorder());

        setUpMenuLabel(menuPanel);
        setUpInstructionsLabel(menuPanel);
        setUpPlayerLabels(menuPanel);
        setUpPlayerOptions(menuPanel);
        setUpNewGameButton(menuPanel);
        return menuPanel;
    }


    /**
     * Sets up the label that will help the user to play a game of othello
     */
    private void setUpInstructionsLabel(JPanel menuPanel)
    {
        JLabel label = new JLabel("Select who plays as black and white!", JLabel.LEFT);
        label.setBounds(5, 100, 800, 50);
        label.setFont(PLAIN_LABEL_FONT);
        menuPanel.add(label);
    }


    /**
     * Sets up the main menu label that will be used to display it onto the GUI
     */
    private void setUpMenuLabel(JPanel menuPanel)
    {
        JLabel titleLabel = new JLabel("Main Menu", JLabel.CENTER);
        titleLabel.setBounds(0, 0, FRAME_WIDTH, 100);
        titleLabel.setFont(new Font("American Typewriter", Font.BOLD, 30));
        menuPanel.add(titleLabel);
    }


    /**
     * Sets up the labels that will point to the user which player they are customizing.
     */
    private void setUpPlayerLabels(JPanel menuPanel)
    {
        JLabel blackPlayerLabel = new JLabel("Black Player: ", JLabel.CENTER);
        JLabel whitePlayerLabel = new JLabel("White Player: ", JLabel.CENTER);
        blackPlayerLabel.setBounds(10, 200, 110, 50);
        whitePlayerLabel.setBounds(10, 250, 110, 50);
        blackPlayerLabel.setFont(PLAIN_LABEL_FONT);
        whitePlayerLabel.setFont(PLAIN_LABEL_FONT);
        menuPanel.add(blackPlayerLabel);
        menuPanel.add(whitePlayerLabel);
    }


    /**
     * Sets up the combo boxes for the user to select the type of players she/he wants to play with.
     */
    private void setUpPlayerOptions(JPanel menuPanel)
    {
        String[] playerTypeNames = {"BASIC AI", "HUMAN PLAYER", "RANDOM AI", "STATIC AI", "DYNAMIC AI",
                "DEPTH CHANGER AI"};
        whitePlayerOptions = new JComboBox(playerTypeNames);
        blackPlayerOptions = new JComboBox(playerTypeNames);
        blackPlayerOptions.setBounds(120, 200, 500, 50);
        whitePlayerOptions.setBounds(120, 250, 500, 50);

        menuPanel.add(blackPlayerOptions);
        menuPanel.add(whitePlayerOptions);
        setUpPlayerCounterLabel(menuPanel, playerTypeNames.length);
    }


    /**
     * Sets up a label that displays the number of different players the user can play with.
     */
    private void setUpPlayerCounterLabel(JPanel menuPanel, int totalPlayers)
    {
        JLabel playersCounter = new JLabel("# of player options: " + totalPlayers, JLabel.LEFT);
        playersCounter.setBounds(10, 120, 200, 100);
        playersCounter.setFont(PLAIN_LABEL_FONT);
        menuPanel.add(playersCounter);
    }


    /**
     * Sets up the button that will be used to start up a new game of othello.
     */
    private void setUpNewGameButton(JPanel menuPanel)
    {
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(PLAIN_LABEL_FONT);
        newGameButton.setBounds(275, 330, 150, 50);
        newGameButton.setBackground(Color.LIGHT_GRAY);
        newGameButton.setOpaque(true);
        newGameButton.setBorder(BorderFactory.createLoweredBevelBorder());
        newGameButton.addActionListener(new NewGameListener());
        menuPanel.add(newGameButton);
    }


    /**
     * An ActionListener object that adds a tab to the GUI and sets up the new game tab with the proper types of players
     * that the user has selected from the menu.
     */
    private class NewGameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            ++totalGames;
            String whitePlayer = (String) whitePlayerOptions.getSelectedItem();
            String blackPlayer = (String) blackPlayerOptions.getSelectedItem();

            OthelloGamePanel gamePanel = OthelloGamePanel.createGameTab(blackPlayer, whitePlayer);
            gamePanel.addClosingAction(new CloseGameListener());
            tabs.addTab("Game " + totalGames, gamePanel);
            tabs.setSelectedIndex(tabs.getTabCount() - 1);
        }
    }


    /**
     * An ActionListener object that notifies the GUI to close the tab that user has specified by clicking the close
     * game button - the user will be able to close down every tab except for the main menu.
     */
    private class CloseGameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            int tabIndex = tabs.getSelectedIndex();
            OthelloGamePanel panel = (OthelloGamePanel) tabs.getComponentAt(tabIndex);
            panel.shutdown();
            tabs.remove(tabIndex);
        }
    }
}

