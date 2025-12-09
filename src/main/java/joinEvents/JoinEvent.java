package joinEvents;

public class JoinEvent {

    private int joinId;
    private int userId;
    private int eventId;

    public JoinEvent(int joinId, int userId, int eventId) {
        this.joinId = joinId;
        this.userId = userId;
        this.eventId = eventId;
    }

    public int getJoinId() { return joinId; }
    public int getUserId() { return userId; }
    public int getEventId() { return eventId; }

    public void setJoinId(int joinId) { this.joinId = joinId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
}
