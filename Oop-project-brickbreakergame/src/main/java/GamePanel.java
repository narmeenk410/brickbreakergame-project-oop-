import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private boolean play = true;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int level = 1;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private MapGenerator map;

    public GamePanel() {
        map = new MapGenerator(4, 8); // Initialize the map with 4 rows and 8 columns
        addKeyListener(this); // Listen to key events
        setFocusable(true); // Make the panel focusable to capture key events
        setFocusTraversalKeysEnabled(false); // Disable default key actions
        timer = new Timer(delay, this); // Timer to control game loop speed
        timer.start(); // Start the timer
    }

    public void paint(Graphics g) {
        // Set background color for the game panel
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592); // Draw the game background

        // Draw the map (bricks)
        map.draw((Graphics2D) g);

        // Draw the borders
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 3, 592); // Left border
        g.fillRect(0, 0, 692, 3); // Top border
        g.fillRect(691, 0, 3, 592); // Right border

        // Draw the paddle
        g.setColor(Color.WHITE);
        g.fillRect(playerX, 550, 100, 12); // Paddle at the bottom

        // Draw the ball
        g.setColor(Color.RED);
        g.fillOval(ballposX, ballposY, 20, 20); // Ball

        // Draw the score and level
        g.setColor(Color.WHITE);
        g.setFont(new Font("MV Boli", Font.BOLD, 25));
        g.drawString("Score: " + score, 520, 30);
        g.drawString("Level: " + level, 400, 30); // Display current level

        // If all bricks are destroyed, display "You Won"
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.WHITE);
            g.setFont(new Font("MV Boli", Font.BOLD, 30));
            g.drawString("You Won, Score: " + score, 190, 300);
            g.setFont(new Font("MV Boli", Font.BOLD, 20));
            g.drawString("Press Enter to Restart.", 230, 350);
        }

        // If ball goes below the paddle, display "Game Over"
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.WHITE);
            g.setFont(new Font("MV Boli", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);
            g.setFont(new Font("MV Boli", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose(); // Dispose the graphics object to free resources
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        timer.start(); // Start the timer for continuous updates

        if (play) {
            // Ball-Paddle Interaction
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir; // Reverse ball's vertical direction on paddle hit
            }

            // Ball-Brick Interaction
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;

                        // Rectangle for brick collision detection
                        Rectangle rect = new Rectangle(brickX, brickY, map.brickWidth, map.brickHeight);
                        if (new Rectangle(ballposX, ballposY, 20, 20).intersects(rect)) {
                            map.setBrickValue(0, i, j); // Set brick value to 0 (indicating it's destroyed)
                            totalBricks--; // Decrease the total number of bricks
                            score += 5; // Increase score for brick hit

                            // Handle ball direction after brick collision
                            if (ballposX + 19 <= rect.x || ballposX + 1 >= rect.x + rect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                        }
                    }
                }
            }

            // Ball movement
            ballposX += ballXdir;
            ballposY += ballYdir;

            // Ball boundary interactions (left, right, top walls)
            if (ballposX < 0 || ballposX > 670) ballXdir = -ballXdir;
            if (ballposY < 0) ballYdir = -ballYdir;

            // Check level completion
            if (totalBricks <= 0) {
                play = false;
                if (level < 3) {  // Transition to next level
                    level++;
                    setupNextLevel();
                } else {
                    displayWinMessage();
                }
            }
        }

        repaint();
    }

    // Method to setup next level
    private void setupNextLevel() {
        ballposX = 120;
        ballposY = 350;
        ballXdir = -1;
        ballYdir = -2;
        totalBricks = 28 + (level * 5); // Increase bricks each level
        map = new MapGenerator(4 + level, 8); // More rows/columns for higher levels
        timer.setDelay(delay - (level * 2)); // Increase speed
        play = true;
        repaint();
    }

    // Display final win message
    private void displayWinMessage() {
        ballXdir = 0;
        ballYdir = 0;
        // Display "You Won" screen here...
    }
    @Override
    public void keyTyped(KeyEvent arg0) {}

    @Override
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
            // Move paddle to the right
            if (playerX >= 600) {
                playerX = 600; // Prevent paddle from going out of bounds
            } else {
                moveRight();
            }
        }
        if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
            // Move paddle to the left
            if (playerX <= 10) {
                playerX = 10; // Prevent paddle from going out of bounds
            } else {
                moveLeft();
            }
        }

        if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
            // Restart the game if 'Enter' is pressed after Game Over or Level Completion
            if (!play) {
                level = 1;
                score = 0;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                totalBricks = 21; // Reset the total number of bricks
                map = new MapGenerator(4, 8); // Reset the map size
                delay = 8; // Reset delay (speed)
                timer.setDelay(delay);
                play = true;
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {}

    public void moveRight() {
        // Move paddle to the right by 50 pixels
        playerX += 50;
    }

    public void moveLeft() {
        // Move paddle to the left by 50 pixels
        playerX -= 50;

    }
}
