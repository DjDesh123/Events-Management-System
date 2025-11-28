package notifications;

public class Notifications {
    private int notId;
    private int userId;
    private int eventId;
    private int creatorId;
    private String message;


    public Notifications(int notId,int userId, int eventId, int creatorId, String message) {
        this.notId = notId;
        this.userId = userId;
        this.eventId = eventId;
        this.creatorId = creatorId;
        this.message = message;
    }



    public int getNotId() {return notId;}

    public int getUserId() {
        return userId;
    }

    public int getEventId() {
        return eventId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getMessage() {
        return message;
    }
}
