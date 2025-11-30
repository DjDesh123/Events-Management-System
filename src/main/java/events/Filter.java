package events;

import java.time.LocalDate;
import java.time.LocalTime;

public class Filter {
    public String keyword;
    public String location;
    public LocalDate startDate;
    public LocalDate endDate;
    public LocalTime startTime;
    public LocalTime endTime;

    // chainable setters (optional)
    public Filter setKeyword(String keyword) { this.keyword = keyword; return this; }
    public Filter setLocation(String location) { this.location = location; return this; }
    public Filter setStartDate(LocalDate startDate) { this.startDate = startDate; return this; }
    public Filter setEndDate(LocalDate endDate) { this.endDate = endDate; return this; }
    public Filter setStartTime(LocalTime startTime) { this.startTime = startTime; return this; }
    public Filter setEndTime(LocalTime endTime) { this.endTime = endTime; return this; }
}
