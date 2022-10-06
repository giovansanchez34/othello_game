package OthelloUI;

import gamelogic.OthelloGameState;
import gamelogic.Tile;
import gamelogic.Pair;
import gamelogic.OthelloTileState;
import gamelogic.OthelloException;
import othello_players.HumanPlayer;
import othello_players.OthelloPlayer;
import othello_players.OthelloPlayerFactory;
import othello_players.OthelloAI;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



/**
 * A Tab that allows users to play a game of othello on the GUI, whether it will be against an AI, another local user,
 * or watching two AI's play against each other.
 */
public class OthelloGamePanel extends JPanel
{
    // font that will be used to displays the text on labels;
    private static final Font BOLD_LABEL_FONT = new Font("American Typewriter", Font.BOLD, 15);
    // the size of the board widget when displayed onto the GUI
    private static final int BOARD_WIDGET_WIDTH = 600;
    private static final int BOARD_WIDGET_HEIGHT = 600;
    // the color of the othello board
    private static final Color BOARD_COLOR = new Color(0, 71, 49);
    // the thickness of the boarder of the board
    private static final int BOARDER_THICKNESS = 4;
    // the diameter of the othello token
    private static final int TOKEN_DIAMETER = 50;
    // the time to let the game loop thread to "have a break" between each call
    private static final int GAME_LOOP_BREATHING_RATE = 150;


    // labels to display each of the players score
    private JLabel whitePlayerScoreLabel;
    private JLabel blackPlayerScoreLabel;
    // a label that displays who turn it is to make a move
    private JLabel playerTurnLabel;
    // a button that will be used to close this panel
    private JButton closeButton;
    // a panel that displays the current game state
    private BoardWidget othelloBoardPanel;
    // holds the underlying logic of how Othello works
    private OthelloGameState gameState;
    private OthelloPlayer whitePlayer;
    private OthelloPlayer blackPlayer;
    // will run the logic of the game in the background of gui
    private Thread gameLoop;
    // determines whether this game panel is still running
    private boolean running;


    /**
     * A static factory method that creates a new panel that will be used as a tab in the othello GUI in which it will
     * allows players to play an othello game.
     */
    public static OthelloGamePanel createGameTab(String blackType, String whiteType)
    {
        return new OthelloGamePanel(blackType, whiteType);
    }


    /**
     * Adds the closing action to the button that will close the game tab/panel.
     */
    public void addClosingAction(ActionListener closingListener)
    {
        closeButton.addActionListener(closingListener);
    }


    /**
     * Shuts down this othello game - ensures that the thread closes so it doesn't consume cpu after this game panel
     * is closed.
     */
    public void shutdown()
    {
        running = false;
    }


    /**
     * Constructs a new tab on the GUI where users can play a game of othello.
     */
    private OthelloGamePanel(String blackType, String whiteType)
    {
        this.setLayout(null);
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        this.setUpComponents(blackType, whiteType);
        this.addMiscIdentifiers(whiteType, blackType);
        this.addComponents();
        this.setUpGameLoop();
        // Unnecessary to have a thread running if both players are not AI's.
        running = (blackPlayer instanceof OthelloAI || whitePlayer instanceof OthelloAI);
        gameLoop.start();
    }


    /**
     * Updates the player turn label -- either updates to whose turn it is currently or the winner of the game if its over.
     */
    private void updateTurn()
    {
        if (gameState.isGameOver())
        {
            playerTurnLabel.setText(winner());
        }
        else
        {
            String playerTurn = (gameState.isBlackTurn()? "Black's " : "White's ");
            playerTurnLabel.setText(playerTurn + "turn -- Place a disc!");
        }
    }


    /**
     * Updates both the labels that display the players' scores.
     */
    private void updateScore()
    {
        whitePlayerScoreLabel.setText(String.format("%2d", gameState.whiteScore()));
        blackPlayerScoreLabel.setText(String.format("%2d", gameState.blackScore()));
    }

    /**
     * Makes a move at coordinate (ROW, COL) of the board. If the move is invalid at the given coordinate,
     * the game continues running and it will still be the player's turn until they enter a valid move.
     */
    private void makeMove(int row, int col)
    {
        try
        {
            gameState.makeMove(row, col);
            updateScore();
            updateTurn();
        }

        catch (OthelloException ignored) {}
    }


    /**
     * Set up the components that will be displayed onto the othello gui panel - will display the score, the state of
     * the game, the type of players, etc.
     */
    private void setUpComponents(String blackType, String whiteType)
    {
        gameState = new OthelloGameState();
        blackPlayer = OthelloPlayerFactory.createAPlayer(blackType);
        whitePlayer = OthelloPlayerFactory.createAPlayer(whiteType);

        blackPlayerScoreLabel = createPlayerScoreLabel(10);
        whitePlayerScoreLabel = createPlayerScoreLabel(40);
        othelloBoardPanel = new BoardWidget(40, 60);
        playerTurnLabel = createPlayerTurnLabel();
        closeButton = createCloseButton();
    }


    /**
     * Sets up the game loop that will be running in the background so that any othello AI's will be able to play
     * the game.
     */
    private void setUpGameLoop()
    {
        gameLoop = new Thread(()->
        {
            while (!gameState.isGameOver() && running)
            {
                OthelloPlayer player = (gameState.isBlackTurn() ? blackPlayer : whitePlayer);
                Pair<Integer, Integer> move = player.chooseMove(gameState);
                if (player instanceof OthelloAI)
                {
                    makeMove(move.first, move.second);
                }
                try
                {
                    // gives the thread the time to breathe so it doesn't consume too much cpu
                    Thread.sleep(GAME_LOOP_BREATHING_RATE);
                }
                catch (InterruptedException ignored) { }
            }
        });
    }


    /**
     * Adds the components that are essential to the GUI, such as the close button to close the selected othello game
     * panel.
     */
    private void addComponents()
    {
        this.add(closeButton);
        this.add(blackPlayerScoreLabel);
        this.add(whitePlayerScoreLabel);
        this.add(othelloBoardPanel);
        this.add(playerTurnLabel);
    }


    /**
     * Adds miscellaneous labels that will be only used for identifying purposes - displaying the players' names and
     * what color they are playing as.
     */
    private void addMiscIdentifiers(String whiteType, String blackType)
    {
        JLabel whitePlayerName = new JLabel(String.format("%35s", whiteType));
        JLabel blackPlayerName = new JLabel(String.format("%35s", blackType));
        JLabel whitePlayerIdentifier = new JLabel("White: ");
        JLabel blackPlayerIdentifier = new JLabel("Black: ");

        blackPlayerIdentifier.setBounds(10, 10, 60, 15);
        whitePlayerIdentifier.setBounds(10, 40, 60, 15);
        blackPlayerIdentifier.setFont(BOLD_LABEL_FONT);
        whitePlayerIdentifier.setFont(BOLD_LABEL_FONT);

        whitePlayerName.setBounds(100, 40, 500, 15);
        blackPlayerName.setBounds(100, 10, 500, 15);
        whitePlayerName.setFont(BOLD_LABEL_FONT);
        blackPlayerName.setFont(BOLD_LABEL_FONT);

        this.add(whitePlayerName);
        this.add(blackPlayerName);
        this.add(whitePlayerIdentifier);
        this.add(blackPlayerIdentifier);
    }


    /**
     * Creates and returns a JLabel that keeps track of the specified player's score
     */
    private JLabel createPlayerScoreLabel(int yLocation)
    {
        JLabel label = new JLabel(String.format("%2d", 2)); // both players start off with 2 points
        label.setBounds(60, yLocation, 50, 15);
        label.setFont(BOLD_LABEL_FONT);
        return label;
    }


    /**
     * Creates a label that will display who turn it is to make a move.
     */
    private JLabel createPlayerTurnLabel()
    {
        JLabel label = new JLabel("Black's turn -- Place a disc!", JLabel.CENTER); // black always goes first
        label.setForeground(Color.black);
        label.setFont(new Font("Monospaced", Font.BOLD, 17));
        label.setBorder(BorderFactory.createLoweredBevelBorder());
        label.setBounds(10, 670, 300, 20);
        return label;
    }



    /**
     * Creates and returns a button that will allow the user to close the tab they are currently on.
     */
    private JButton createCloseButton()
    {
        JButton closeButton = new JButton("CLOSE");
        closeButton.setFont(BOLD_LABEL_FONT);
        closeButton.setBounds(500, 670, 100, 50);
        closeButton.setBackground(Color.LIGHT_GRAY);
        closeButton.setOpaque(true);
        closeButton.setBorder(BorderFactory.createLoweredBevelBorder());
        return closeButton;
    }


    /**
     * Returns the winner of the current othello game.
     */
    private String winner()
    {
        if (gameState.blackScore() > gameState.whiteScore())
        {
            return "BLACK WINS!!!";
        }
        else if (gameState.blackScore() < gameState.whiteScore())
        {
            return "WHITE WINS!!!";
        }
        else
        {
            return "DRAW!!!";
        }
    }


    /**
     * A widget used to display an othello board onto the screen
     */
    private class BoardWidget extends JPanel
    {

        /**
         * Constructs a board widget that will allow players to see the current state of the game. The board widget will need
         * to be placed on another component.
         */
        public BoardWidget(int x, int y)
        {
            this.setBackground(BOARD_COLOR);
            this.setOpaque(true);
            this.setBounds(x, y, BOARD_WIDGET_WIDTH, BOARD_WIDGET_HEIGHT);
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, BOARDER_THICKNESS));
            this.setLayout(new GridLayout(gameState.board().rows(), gameState.board().cols()));
            this.addTiles();
        }


        /**
         * Adds the following tiles from the given game state's board to the board widget so that the tiles' state would
         * be displayed to the user.
         */
        private void addTiles()
        {
            for (Tile tile : gameState.board())
            {
                TileWidget tileWidget = new TileWidget(tile);
                this.add(tileWidget);
            }
        }


        /**
         * Paints the current state of the othello game onto the component.
         */
        @Override
        public void paintComponent(Graphics graphics)
        {
            super.paintComponent(graphics);
            this.repaint();
        }
    }


    /**
     * Used to display the state of the tile on an Othello Board Widget.
     */
    private class TileWidget extends JButton implements ActionListener
    {
        // the tile that is going to be displayed by the widget
        private final Tile tile;

        /**
         * Creates and returns a TileWidget instance that displays the state of the tile in an othello board - the tile
         * can be empty or it contain a black or white token.
         */
        public TileWidget(Tile tile)
        {
            this.tile = tile;
            this.setOpaque(true);
            this.setBackground(BOARD_COLOR);
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.addActionListener(this);
        }

        /**
         * If the tile that is clicked on is a valid move and player is a HumanPlayer, the board will update itself by
         * displaying the tokens that have been flanked, it will update the score, and it will update whose turn it is.
         */
        @Override
        public void actionPerformed(ActionEvent event)
        {
            OthelloPlayer player  = (gameState.isBlackTurn() ? blackPlayer : whitePlayer);
            if (player instanceof HumanPlayer)
            {
                makeMove(tile.row(), tile.col());
            }
        }


        /**
         * Paints the state of the tile onto the component
         */
        @Override
        public void paintComponent(Graphics graphics)
        {
            super.paintComponent(graphics);
            Graphics2D drawer = (Graphics2D) graphics;

            if (tile.state() != OthelloTileState.EMPTY)
            {
                // Paints the state of the tile - could have a token drawn on top of the tile or not
                drawer.setColor(getTokenColor());
                drawer.fillOval(this.getWidth() / 6, this.getHeight() / 6, TOKEN_DIAMETER, TOKEN_DIAMETER);
            }
        }


        /**
         * Returns the Color (black or white) of the token that is on the tile.
         */
        private Color getTokenColor()
        {
            return (tile.state() == OthelloTileState.BLACK? Color.BLACK : Color.WHITE);
        }
    }
}

