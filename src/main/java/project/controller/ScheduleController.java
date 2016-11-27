package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.*;
import project.service.ScheduleService;
import project.service.SearchService;
import project.validator.ItemValidator;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by halld on 02-Nov-16.
 */

@Controller
public class ScheduleController {

    // Services for schedules, finding users and validation
    private ScheduleService scheduleService;
    private SearchService searchService;
    private ItemValidator itemValidator = new ItemValidator();

    // Constructor
    @Autowired
    public ScheduleController(ScheduleService scheduleService, SearchService searchService){
        this.scheduleService = scheduleService;
        this.searchService = searchService;
    }

    // Get schedule filtered by selectedFilter
    @RequestMapping(value = "/scheduleByFilter")
    public String viewGetScheduleByFilters(Model model, @RequestParam("selectedFilter") String filter, HttpSession session){
        // Check if user is logged in
        if (session.getAttribute("loggedInUser") == null ) {
            return "redirect:/";
        }

        // Find logged in user info
        String tmpUsername = session.getAttribute("loggedInUser").toString();
        User tmpUser = searchService.findByName(tmpUsername);
        int userId = tmpUser.getUserId();
        List<User> friendList = tmpUser.getFriends();
        List<Group> groupList = tmpUser.getGroups();

        // Get current week no and year
        int yearNow = LocalDateTime.now().getYear();
        int weekNow = scheduleService.findWeekNo(LocalDateTime.now());

        // Generate timeslot and filter lists
        List <String> TimeSlots = scheduleService.getTimeSlots();
        List <String> Filters = scheduleService.createfilterList();

        // Find scheduleItems that have selectedFilter
        List<ScheduleItem> scheduleByFilters = scheduleService.scheduleItemsFilters(userId, weekNow, yearNow, filter);

        // Add attributes to model
        model.addAttribute("filters",Filters);
        model.addAttribute("timeSlots",TimeSlots);
        model.addAttribute("scheduleItem",new ScheduleItem());
        model.addAttribute("friends", friendList);
        model.addAttribute("groups", groupList);

        // If no filter is selected, redirect to base view
        if(filter.equals("1")) {
            return "redirect:/home";
        }
        model.addAttribute("scheduleItems", scheduleByFilters);

        return "Home";
    }

    // Post method for editing items, not fully implemented
    /*@RequestMapping(value = "/schedule/edit/{itemId}", method = RequestMethod.POST)
    public String editSchedulePost(@ModelAttribute("scheduleItemEdit") ScheduleItem scheduleItem, Model model,
                                   @PathVariable("itemId") int itemId, String title, int userId, LocalDateTime startTime,
                                   LocalDateTime endTime, int weekNo, int yearNo, String location, String color,
                                   String description, List<User> taggedUsers, List<String> filters){

        model.addAttribute("scheduleItemEdit", scheduleService.editScheduleItem(itemId, title, userId, startTime,
                endTime, weekNo, yearNo, location, color, description, taggedUsers, filters));
        return "";
    }*/

    // Method for deleting schedule items
    @RequestMapping(value="/deleteItem")
    public String deleteItemPost(@RequestParam("itemId") int itemId, Model model) {
        scheduleService.removeItem(itemId);
        return "redirect:/home";
    }

    // Method for creating a group
    @RequestMapping(value="/createGroup")
    public String createGroup(@RequestParam("grpName") String grpName, Model model, HttpSession session) {
        // Get logged in user and info
        String LoggedInUser = session.getAttribute("loggedInUser").toString();
        User user = scheduleService.findUserByUsername(LoggedInUser);
        List<User> members = new ArrayList<>();
        members.add(user);
        List<User> friendList = user.getFriends();
        List<Group> groupList = user.getGroups();

        // FInd current week no and year
        int yearNow = LocalDateTime.now().getYear();
        int weekNow = scheduleService.findWeekNo(LocalDateTime.now());

        // Generate timeslot and filter lists
        List <String> Filters = scheduleService.createfilterList();
        List <String> TimeSlots = scheduleService.getTimeSlots();

        // Add attributes to model
        model.addAttribute("friends", friendList);
        model.addAttribute("groups", groupList);
        model.addAttribute("filters",Filters);
        model.addAttribute("timeSlots",TimeSlots);
        model.addAttribute("scheduleItem",new ScheduleItem());

        // Add user's schedule items for current week and year
        model.addAttribute("scheduleItems",scheduleService.scheduleItems(user.getUserId(),weekNow,yearNow));

        // If a group with the same name already exists, reload page with error
        if(scheduleService.createGroup(grpName, members)) {
            model.addAttribute("groupFail", false);
            return "redirect:/home";
        }

        model.addAttribute("groupFail", true);
        return "Home";
    }

    // Method for deleting a group
    @RequestMapping(value="/deleteGroup")
    public String deleteGroup(@RequestParam("grpId") int grpId, Model model) {
        scheduleService.deleteGroup(grpId);
        return "redirect:/home";
    }

    // Method for removing a user from the logged in user's friend list
    @RequestMapping(value="/deleteFriendship")
    public String deleteFriendship(@RequestParam("friendId") int friendId, Model model, HttpSession session) {
        // Get logged in user
        String LoggedInUser = session.getAttribute("loggedInUser").toString();
        User user = scheduleService.findUserByUsername(LoggedInUser);

        // Delete the friendship
        scheduleService.deleteFriendship(user.getUserId(), friendId);
        return "redirect:/home";
    }

    // Post method for inserting an item into the logged in user's schedule
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    //@PostMapping(value = "/home")
    public String insertItemPost(@ModelAttribute("scheduleItem") ScheduleItem scheduleItem,
                                 BindingResult bindingResult,Model model, HttpSession session) {
        String newDate="";

        // Find current week no and year
        int yearNow = LocalDateTime.now().getYear();
        int weekNow = scheduleService.findWeekNo(LocalDateTime.now());

        // Get logged in user and info
        String tmpUsername = session.getAttribute("loggedInUser").toString();
        User tmpUser = searchService.findByName(tmpUsername);
        int userid = tmpUser.getUserId();
        List<User> friendList = tmpUser.getFriends();
        List<Group> groupList = tmpUser.getGroups();


        // Generate timeslot and filter lists
        List <String> TimeSlots = scheduleService.getTimeSlots();
        List <String> Filters = scheduleService.createfilterList();

        // Add attributes to model
        model.addAttribute("timeSlots",TimeSlots);
        model.addAttribute("friends", friendList);
        model.addAttribute("groups", groupList);
        model.addAttribute("filters",Filters);

        // Validate user input if the date is empty
        if (scheduleItem.getdate() == ""){
            bindingResult.rejectValue("date","You must choose a date");
            itemValidator.validateItem(scheduleItem, bindingResult, tmpUsername);

            if (bindingResult.hasErrors()) {
                model.addAttribute("scheduleItems",scheduleService.scheduleItems(userid,weekNow,yearNow));
                model.addAttribute("hasErrors", true);
                model.addAttribute("errors", bindingResult.getAllErrors());
                return "Home";
            }
        }
        // else change the date string into a new date string, for further parsing
        else {newDate = scheduleService.changeStringDateToRigthDate(scheduleItem.getdate());}

        // Change format of start and end time strings
        String newSTime = scheduleService.changeformatOfTime(scheduleItem.getStartstring());
        String newETime = scheduleService.changeformatOfTime(scheduleItem.getEndstring());

        // Create whole datetime strings for the start and end dates
        String startTimeforItem = newDate +" "+ newSTime;
        String endTimeforItem = newDate +" "+newETime;

        // Format the strings to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startdateTime = LocalDateTime.parse(startTimeforItem,formatter);
        LocalDateTime enddateTime = LocalDateTime.parse(endTimeforItem,formatter);
        scheduleItem.setStartTime(startdateTime);
        scheduleItem.setEndTime(enddateTime);

        // Validate the rest of the input
        itemValidator.validateItem(scheduleItem, bindingResult, tmpUsername);

        // If there are errors, reload page with errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("scheduleItems",scheduleService.scheduleItems(userid,weekNow,yearNow));
            model.addAttribute("hasErrors", true);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "Home";
        }

        // Find week no and year of the new item
        int year = scheduleService.findYear(scheduleItem.getdate());
        int weekNo = scheduleService.findWeekNo(startdateTime);

        // Create the new item
         scheduleService.createItem(scheduleItem.getTitle(), userid, startdateTime, enddateTime,
                scheduleItem.getTaggedUsers(), weekNo, year, scheduleItem.getLocation(),
                scheduleItem.getColor(),scheduleItem.getDescription(), scheduleItem.getFilter());

        // Clean variables so the input values are empty, for next insertion
        ScheduleItem scheduleitem = new ScheduleItem();
        model.addAttribute("scheduleItem",scheduleitem);

        // Add to model attributes for logged in user, current week no and year
        model.addAttribute("scheduleItems",scheduleService.scheduleItems(userid,weekNow,yearNow));

        return "Home";
    }

    // Get base home page
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model, HttpSession session) {
        // Check if user is logged in
        boolean isLoggedIn;
        if (session.getAttribute("loggedInUser") == null ) {
            return "redirect:/";
        }
        else isLoggedIn = true;

        // Get logged in user and info
        String LoggedInUser = session.getAttribute("loggedInUser").toString();
        User user = scheduleService.findUserByUsername(LoggedInUser);
        int userid= user.getUserId();
        List<User> friendList = user.getFriends();
        List<Group> groupList = user.getGroups();

        // Find the current week no and year
        int yearNow = LocalDateTime.now().getYear();
        int weekNow = scheduleService.findWeekNo(LocalDateTime.now());

        // Generate filter and timeslot lists
        List <String> TimeSlots = scheduleService.getTimeSlots();
        List <String> Filters = scheduleService.createfilterList();

        // Add attributes to model
        model.addAttribute("filters",Filters);
        model.addAttribute("friends", friendList);
        model.addAttribute("groups", groupList);
        model.addAttribute("timeSlots",TimeSlots);
        model.addAttribute("loggedInUser",LoggedInUser);
        model.addAttribute("loggedInStatus",isLoggedIn);
        model.addAttribute("scheduleItem", new ScheduleItem());
        model.addAttribute("scheduleItems",scheduleService.scheduleItems(userid,weekNow,yearNow));
        model.addAttribute("groupFail", false);

        return "Home";
    }

}
