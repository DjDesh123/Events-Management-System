package notifications;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class NotificationDatabase {
        private Map<Integer, Notifications> notificationsMap = new HashMap<>();
        private AtomicInteger counterId = new AtomicInteger(0);


        public int generateNewId() {
            return counterId.getAndIncrement();
        }

        public void save(Notifications notification) {
            int notId = notification.getNotId();
            if (notId > counterId.get()) {
                counterId.set(notId);
            }
            notificationsMap.put(notId, notification);
        }

        public Notifications add(int userId, int eventId, int creatorId, String message) {
            int id = generateNewId();
            Notifications notifications = new Notifications(id,userId,eventId,creatorId,message);
            notificationsMap.put(id, notifications);
            return notifications;
        }

        public Notifications get(int notId){
            return notificationsMap.get(notId);
        }

        public void delete(int notId){
            notificationsMap.remove(notId);
        }

        public Map<Integer, Notifications> getNotifications() {
            return notificationsMap;
        }

    public java.util.List<Notifications> getNotificationsForUser(int userId) {
        return notificationsMap.values().stream()
                .filter(n -> n.getUserId() == userId)
                .collect(Collectors.toList());
    }
}
