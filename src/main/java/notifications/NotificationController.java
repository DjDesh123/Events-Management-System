package notifications;

import events.Event;
import user.User;

import java.util.List;


public class NotificationController {

    private final NotificationDatabase notificationDatabase;

    public NotificationController() {

        //ensure table exists then load any persisted notifications
        NotificationsDatabaseStorage.createTableIfNotExist();
        this.notificationDatabase = new NotificationDatabase(NotificationsDatabaseStorage.load());
    }

    // get list for UI
    public List<Notifications> getNotificationsForUser(int userId) {
        return notificationDatabase.getNotificationsForUser(userId);
    }

    // delete and persist
    public void deleteNotification(int notId) {
        notificationDatabase.delete(notId);
        NotificationsDatabaseStorage.save(notificationDatabase.getNotificationsMap());
    }

    // create full notification
    public Notifications createNotification(int userId, int eventId, int creatorId, String message) {
        Notifications n = notificationDatabase.add(userId, eventId, creatorId, message);
        NotificationsDatabaseStorage.save(notificationDatabase.getNotificationsMap());
        return n;
    }

    //notify all attendees (called by your event code)
    public void notifyAttendees(Event event, String message) {
        for (User u : event.getAttendee()) {
            createNotification(u.getUserId(), event.getEventId(), event.getCreatorId(), message);
        }
    }
}
