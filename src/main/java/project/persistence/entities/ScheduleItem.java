package project.persistence.entities;
import java.time.*;
import java.util.*;

/**
 * Created by Þórunn on 11/2/2016.
 * Class for containing all info about a schedule item
 */
public class ScheduleItem {
    private String title;
    private int id;
    private int userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<User> taggedUsers = new ArrayList<>();  // Never used in this version
    private int weekNo;
    private int year;
    private String location;
    private String color;
    private String description;
    private List<String> filters = new ArrayList<>();  // Never used in this version
    private String filter;
    private String date;
    // These are used for ease of showing schedule in view:
    private String startstring;
    private String endstring;
    private int timeSpan;
    private int weekDay;

    public String getTitle(){return title;}
    public void setTitle(String title){this.title = title;}

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public int getUserId(){return userId;}
    public void setUserId(int userId){this.userId = userId;}

    public LocalDateTime getStartTime(){return startTime;}
    public void setStartTime(LocalDateTime startTime){this.startTime = startTime;}

    public LocalDateTime getEndTime(){return endTime;}
    public void setEndTime(LocalDateTime endTime){this.endTime = endTime;}

    public void setTaggedUsers(List<User> taggedUsers){this.taggedUsers = taggedUsers;}
    public List<User> getTaggedUsers(){return taggedUsers;}
    public void addTaggeduser(User user){taggedUsers.add(user);}
    public void removeTaggedUser(User user){taggedUsers.remove(user);}

    public int getWeekNo(){return weekNo;}
    public void setWeekNo(int weekNo){this.weekNo = weekNo;}

    public int getYear(){return year;}
    public void setYear(int year){this.year = year;}

    public String getLocation(){return location;}
    public void setLocation(String location ){this.location = location;}

    public String getColor(){return color;}
    public void setColor(String color ){this.color = color;}

    public String getDescription(){return description;}
    public void setDescription(String description ){this.description = description;}

    public String getdate(){return date;}
    public void setDate(String date){this.date = date;}

    public String getStartstring(){return startstring;}
    public void setStartstring(String startstring ){this.startstring= startstring;}

    public String getEndstring(){return endstring;}
    public void setEndstring(String endstring ){this.endstring = endstring;}

    public int getTimeSpan(){return timeSpan;}
    public void setTimeSpan(int timeSpan){this.timeSpan = timeSpan;}

    public int getWeekDay(){return weekDay;}
    public void setWeekDay(int weekDay){this.weekDay = weekDay;}

    public String getFilter(){return filter;}
    public void setFilter(String filter ){this.filter= filter;}


    public void setFilters(List<String> filters){this.filters = filters;}
    public List<String> getFilters(){return filters;}
    public void addFilter(String filter){filters.add(filter);}
    public void removeFilter(String filter){filters.remove(filter);}

    public void changeTime(LocalDateTime start, LocalDateTime end){
        startTime = start;
        endTime = end;
    }

    // Calculates the timespan for this item
    public void calculateTime(){
        int hours =endTime.getHour() -startTime.getHour();
        int minutes = endTime.getMinute() - startTime.getMinute();

        int finalMin = hours*60 + minutes;
        int timespan = finalMin/10;
        this.timeSpan = timespan;
    }

    // Finds the number of the item's weekday. 0 = Monday, 7 = Sunday
    public void findWeekDay(){
        int weekday = startTime.getDayOfWeek().getValue();
        this.weekDay = weekday-1;
    }

}
