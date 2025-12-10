package notifications;

import events.Event;
import user.User;

import java.util.List;

/**
 * Controller that exposes simple methods to UI/business code.
 * It delegates persistence to NotificationDatabase and NotificationsDatabaseStorage.
 */
public class NotificationController {

    private final NotificationDatabase notificationDatabase;

    public NotificationController() {
        // ensure table exists then load any persisted notifications
        NotificationsDatabaseStorage.createTableIfNotExist();
        this.notificationDatabase = new NotificationDatabase(NotificationsDatabaseStorage.load());
    }

    // Get list for UI
    public List<Notifications> getNotificationsForUser(int userId) {
        return notificationDatabase.getNotificationsForUser(userId);
    }

    // Delete and persist
    public void deleteNotification(int notId) {
        notificationDatabase.delete(notId);
        NotificationsDatabaseStorage.save(notificationDatabase.getNotificationsMap());
    }

    // Full-create (keeps previous API)
    public Notifications createNotification(int userId, int eventId, int creatorId, String message) {
        Notifications n = notificationDatabase.add(userId, eventId, creatorId, message);
        NotificationsDatabaseStorage.save(notificationDatabase.getNotificationsMap());
        return n;
    }

    // Convenience create when you only have a user and message
    public Notifications createNotification(int userId, String message) {
        return createNotification(userId, -1, -1, message);
    }

    // notify all attendees (called by your event code)
    public void notifyAttendees(Event event, String message) {
        for (User u : event.getAttendee()) {
            createNotification(u.getUserId(), event.getEventId(), event.getCreatorId(), message);
        }
    }
}
