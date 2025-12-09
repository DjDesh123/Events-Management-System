package notifications;

import events.Event;
import java.util.List;

public class NotificationController {

    private final NotificationDatabase notificationDatabase = new NotificationDatabase();

    public List<Notifications> getNotificationsForUser(int userId){
        return notificationDatabase.getNotificationsForUser(userId);
    }

    public void deleteNotification(int notId){
        notificationDatabase.delete(notId);
    }

    public Notifications createNotification(int userId, int eventId, int creatorId, String message){
        return notificationDatabase.add(userId, eventId, creatorId, message);
    }

    // Send notifications to all attendees of an event
    public void notifyAttendees(Event event, String message) {
        for (user.User u : event.getAttendee()) {
            createNotification(u.getUserId(), event.getEventId(), event.getCreatorId(), message);
        }
    }
}
