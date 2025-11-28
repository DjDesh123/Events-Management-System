package notifications;

import java.util.HashMap;
import java.util.Map;

public class NotificationDatabase {
        private Map<Integer, Notifications> notificationsMap = new HashMap<>();

        public void add(Notifications notifications){
            notificationsMap.put(notifications.getNotId(), notifications);
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
    }
