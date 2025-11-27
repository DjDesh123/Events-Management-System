package events;

import user.User;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private int eventid;
    private String eventTitle;
    private String location;
    public int startDate;
    public int endDate;
    public int timeframe;
    private List<User> attendee;
    private String description;

    public Event(int  eventid, String eventTitle, String location, int startDate, int endDate, int timeframe) {
        this.eventid = eventid;
        this.eventTitle = eventTitle;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeframe = timeframe;
        this.attendee = new ArrayList<>();
        this.description = "";
    }


    //getters

    public int getEventid() {
        return eventid;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getLocation() {
        return location;
    }

    public int getStartDate() {
        return startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public int getTimeframe() {
        return timeframe;
    }

    public List<User> getAttendee() {
        return attendee;
    }

    public String getDescription() {
        return description;
    }


}
