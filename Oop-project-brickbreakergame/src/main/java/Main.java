import javax.swing.SwingUtilities;
public class Main {
    public static void main(String[] args) {
        try {
            // Launch the welcome screen
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        // Pass an instance of BackgroundPanel to the generic WelcomeScreen
                        new WelcomeScreen<>(new WelcomeScreen.BackgroundPanel()).setVisible(true);
                    } catch (Exception e) {
                        System.err.println("Error while displaying the welcome screen: " + e.getMessage());
                        e.printStackTrace(); // Log stack trace for debugging
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Critical error occurred during initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



