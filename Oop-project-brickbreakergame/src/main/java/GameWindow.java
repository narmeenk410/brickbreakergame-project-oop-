import javax.swing.*;
public class GameWindow extends JFrame {
    public GameWindow() {
        // Setup the JFrame for the actual game
        setTitle("Brick Breaker Game");
        setSize(700, 630);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        setVisible(true);
    }
}