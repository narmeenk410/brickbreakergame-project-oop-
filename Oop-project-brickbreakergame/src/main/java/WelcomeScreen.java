import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import static java.util.Objects.requireNonNull;

public class WelcomeScreen<T extends JPanel> extends JFrame {
    private T customPanel;

    public WelcomeScreen(T panel) {
        this.customPanel = panel;

        setTitle("Welcome to Brick Breaker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window on the screen

        // Add the custom panel
        add(customPanel, BorderLayout.CENTER);

        // Create the start button and align it at the bottom
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        customPanel.setLayout(new BorderLayout());
        customPanel.add(startButton, BorderLayout.SOUTH);

        // Action listener for the start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new GameWindow();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Pass an instance of the BackgroundPanel to the generic WelcomeScreen
                new WelcomeScreen<>(new BackgroundPanel()).setVisible(true);
            }
        });
    }

    // Define BackgroundPanel as a static inner class
    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            // Load background image from resources
            URL imageURL = WelcomeScreen.class.getResource("brick-pic.jpg");

            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                backgroundImage = icon.getImage();
            } else {
                // Handle the case where the resource is not found
                System.err.println("Background image not found.");
                // Optionally load a default image or handle the error appropriately
                backgroundImage = null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

