package joinEvents;


public class JoinEvent {
    private int joinId;
    private int userId;
    private int eventId;


    // constructor
    public JoinEvent(int joinId, int userId, int eventId) {
        this.joinId = joinId;
        this.userId = userId;
        this.eventId = eventId;
    }


    public int getJoinId() {
        return joinId;
    }

    public int getUserId() {
        return userId;
    }

    public int getEventId() {
        return eventId;
    }



}

