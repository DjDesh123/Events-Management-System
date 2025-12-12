package bootstrap;

import user.*;
import events.*;
import joinEvents.*;
import notifications.*;
import ui.LogInUI;

public class Bootstrap {

    private static UserController userController;
    private EventController eventController;
    private JoinEventController joinEventController;
    private NotificationController notificationController;

    public Bootstrap() {
        // Initialize controllers
        userController = new UserController();
        eventController = new EventController();

        // Databases for JoinEventController
        EventDatabase eventDatabase = new EventDatabase();
        JoinEventDatabase joinEventDatabase = new JoinEventDatabase();

        joinEventController = new JoinEventController(eventDatabase, joinEventDatabase);
        notificationController = new NotificationController();

        System.out.println("Bootstrap complete. Controllers ready and data loaded if it existed.");

        // Auto-launch login UI
        launchLogin();
    }

    private void launchLogin() {
        // Ensure DB tables exist (optional but recommended)
        UserDatabaseStorage.init();

        // Launch the login screen
        javax.swing.SwingUtilities.invokeLater(() -> new LogInUI(userController));
    }

    // Main method to start the app
    public static void main(String[] args) {
        new Bootstrap();
    }
}
