package notifications;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class NotificationDatabase {

    private final Map<Integer, Notifications> notificationsMap;
    private final AtomicInteger counterId;

    // Accept an initial map
    public NotificationDatabase(Map<Integer, Notifications> initial) {
        if (initial == null) initial = new HashMap<>();
        this.notificationsMap = new HashMap<>(initial);

        // set counter to max existing id + 1
        int max = notificationsMap.keySet().stream().mapToInt(i -> i).max().orElse(-1);
        this.counterId = new AtomicInteger(max + 1);
    }

    private int generateNewId() {
        return counterId.getAndIncrement();
    }

    //save one notification
    public void save(Notifications notification) {
        notificationsMap.put(notification.getNotId(), notification);
    }

    // create a new notification record
    public Notifications add(int userId, int eventId, int creatorId, String message) {
        int id = generateNewId();
        Notifications n = new Notifications(id, userId, eventId, creatorId, message);
        notificationsMap.put(id, n);
        return n;
    }

    //get one notification
    public Notifications get(int notId) {
        return notificationsMap.get(notId);
    }

    // delete one notification
    public void delete(int notId) {
        notificationsMap.remove(notId);
    }

    //expose the full map
    public Map<Integer, Notifications> getNotificationsMap() {
        return Collections.unmodifiableMap(notificationsMap);
    }

    // Get all notifications belonging to a user
    public List<Notifications> getNotificationsForUser(int userId) {
        return notificationsMap.values().stream()
                .filter(n -> n.getUserId() == userId)
                .sorted(Comparator.comparingInt(Notifications::getNotId)) // optional order
                .collect(Collectors.toList());
    }
}
