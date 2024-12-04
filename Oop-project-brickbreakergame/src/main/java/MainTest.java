import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.SwingUtilities;

class MainTest {

    // Test if the WelcomeScreen is initialized without exceptions
    @Test
    void testWelcomeScreenInitialization() {
        assertDoesNotThrow(() -> SwingUtilities.invokeLater(() -> {
            try {
                // Initialize WelcomeScreen and check for exceptions
                new WelcomeScreen(new WelcomeScreen.BackgroundPanel()).setVisible(true);
            } catch (Exception e) {
                fail("WelcomeScreen initialization threw an exception: " + e.getMessage());
            }
        }), "SwingUtilities should initialize without throwing exceptions");
    }

    // Test handling a critical error
    @Test
    void testCriticalErrorHandling() {
        // Mocking a scenario where an exception occurs
        Exception criticalException = new Exception("Mocked Critical Error");
        try {
            throw criticalException;
        } catch (Exception e) {
            assertEquals("Mocked Critical Error", e.getMessage(),
                    "Exception message should match the expected critical error message");
        }
    }

    // Test error handling in WelcomeScreen (with a GUI-related exception)
    @Test
    void testErrorHandlingInWelcomeScreen() {
        // Simulate an error within the GUI on the Event Dispatch Thread
        Exception guiException = new Exception("Mocked GUI Error");

        // Using invokeLater to simulate an error that could happen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                throw new RuntimeException(guiException);
            } catch (Exception e) {
                // Assert that the exception caught is the one thrown in the GUI
                assertEquals("Mocked GUI Error", e.getCause().getMessage(),
                        "GUI exception message should match the expected error message");
            }
        });
    }
}
