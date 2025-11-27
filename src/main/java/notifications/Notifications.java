package notifications;

public class Notifications {
    private int userid;
    private int eventid;
    private int creatorid;
    private String message;


    public Notifications(int userid, int eventid, int creatorid, String message) {
        this.userid = userid;
        this.eventid = eventid;
        this.creatorid = creatorid;
        this.message = message;
    }


    public int getUserid() {
        return userid;
    }

    public int getEventid() {
        return eventid;
    }

    public int getCreatorid() {
        return creatorid;
    }

    public String getMessage() {
        return message;
    }
}
