package bootstrap;

import events.*;
import joinEvents.*;
import notifications.*;
import user.*;
import settings.SettingsController;

public class Bootstrap {

    private UserController userController;
    private EventController eventController;
    private JoinEventController joinEventController;
    private NotificationController notificationController;
    private SettingsController settingsController;

    public Bootstrap() {
        // Initialize controllers (they can call their own DB storage internally)
        userController = new UserController();
        eventController = new EventController();

        // For JoinEventController which will pass the two database that it needed
        EventDatabase eventDatabase = new EventDatabase();
        JoinEventDatabase joinEventDatabase = new JoinEventDatabase();

        joinEventController = new JoinEventController(eventDatabase, joinEventDatabase);
        notificationController = new NotificationController();
        settingsController = new SettingsController();

        System.out.println("Bootstrap complete. Controllers ready and data loaded if it existed.");
    }

    public UserController getUserController() { return userController; }
    public EventController getEventController() { return eventController; }
    public JoinEventController getJoinEventController() { return joinEventController; }
    public NotificationController getNotificationController() { return notificationController; }
    public SettingsController getSettingsController() { return settingsController; }
}
