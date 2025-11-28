package events;

import user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    private int eventId;
    private String eventTitle;
    private String location;
    public int startDate;
    public int endDate;
    public int timeframe;
    private List<User> attendee;
    private String description;

    public Event(int  eventId, String eventTitle, String location, int startDate, int endDate, int timeframe) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeframe = timeframe;
        this.attendee = new ArrayList<>();
        this.description = "";
    }


    //getters
    public int getEventId() {
        return eventId;
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

    //setters

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public void setTimeframe(int timeframe) {
        this.timeframe = timeframe;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
