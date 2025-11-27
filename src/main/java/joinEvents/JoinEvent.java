package joinEvents;

public class JoinEvent {
    private int joinid;
    private int userId;
    private int eventId;


    // constructor
    public JoinEvent(int joinid, int userid, int eventId) {
        this.joinid = joinid;
        this.userId = userid;
        this.eventId = eventId;
    }


    public int getJoinid() {
        return joinid;
    }

    public int getUserId() {
        return userId;
    }

    public int getEventId() {
        return eventId;
    }



}

