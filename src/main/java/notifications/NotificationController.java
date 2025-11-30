package notifications;

import user.UserController;
import java.util.List;


public class NotificationController {

    private NotificationDatabase notificationDatabase= new NotificationDatabase();

    public List<Notifications> getNotification(){
        int userId= UserController.getLoggedInUser().getUserId();
        return notificationDatabase.getNotificationsForUser(userId);

    }

    public void deleteNotification(int notId){
        notificationDatabase.delete(notId);
    }

    public Notifications createNotification(int userId, int eventId, int creatorId, String message) {
        return notificationDatabase.add(userId, eventId, creatorId, message);
    }
}
