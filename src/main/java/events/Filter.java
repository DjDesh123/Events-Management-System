package events;

import java.time.LocalDate;
import java.time.LocalTime;

public class Filter {
    // simple data container for optinal filter criteria
    public String keyword;
    public String location;
    public LocalDate startDate;
    public LocalDate endDate;
    public LocalTime startTime;
    public LocalTime endTime;
}
