package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.Group;
import project.persistence.entities.User;
import project.service.CompareService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by halld on 02-Nov-16.
 */

@Controller
public class CompareController {

    CompareService compareService;

    @Autowired
    public CompareController(CompareService compareService){
        this.compareService = compareService;
    }

    // GET main Compare page
    @RequestMapping(value="/compare", method = RequestMethod.GET)
    public String viewGetComparison(Model model, HttpSession session){
        // Check if user is logged in
        if (session.getAttribute("loggedInUser") == null ) {
            return "redirect:/";
        }

        // Get logged in user and necessary info
        String loggedInUserName = session.getAttribute("loggedInUser").toString();
        User loggedInUser = compareService.findUserByName(loggedInUserName);
        List<User> friends = loggedInUser.getFriends();
        List<Group> groups = loggedInUser.getGroups();

        // Generate list of timeslots for schedule
        List <String> timeSlots = compareService.createTimeSlots();

        // Add necessary attributes to model
        model.addAttribute("comparedSchedule", new ArrayList<String>());
        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("friendList", friends);
        model.addAttribute("groupList", groups);
        model.addAttribute("comparing", false);
        return "Compare";
    }

    // Show comparison of schedules between logged in user and selected friend
    @RequestMapping(value="/compareFriends")
    public String compareSchedulePost(Model model, @RequestParam("selectedFriend") int friendId, HttpSession session){
        // CHeck if user is logged in
        if (session.getAttribute("loggedInUser") == null ) {
            return "redirect:/";
        }

        // Get logged in user and necessary info
        String loggedInUserName = session.getAttribute("loggedInUser").toString();
        User loggedInUser = compareService.findUserByName(loggedInUserName);
        List<User> friends = loggedInUser.getFriends();
        List<Group> groups = loggedInUser.getGroups();

        // Generate list of timeslots for schedule
        List <String> timeSlots = compareService.createTimeSlots();

        // Find current week no and year
        int yearNow = LocalDateTime.now().getYear();
        int weekNow = compareService.findWeekNo(LocalDateTime.now());

        // Get comparison list of sheduleItems
        List<String> schedule = compareService.compareSchedules(loggedInUser.getUserId(), friendId, weekNow, yearNow);

        // Add necessary atrributes to model
        model.addAttribute("comparedSchedule", schedule);
        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("friendList", friends);
        model.addAttribute("groupList", groups);
        model.addAttribute("comparing", true);
        return "Compare";
    }

    // Get comparison of schedules in group
    @RequestMapping(value="/compareGroup", method = RequestMethod.POST)
    public String compareGroupSchedulePost(Model model, @RequestParam("selectedGroup") int grpId, HttpSession session){
        // CHeck if user is logged in
        if (session.getAttribute("loggedInUser") == null ) {
            return "redirect:/";
        }

        // Get logged in user and info
        String loggedInUserName = session.getAttribute("loggedInUser").toString();
        User loggedInUser = compareService.findUserByName(loggedInUserName);
        List<User> friends = loggedInUser.getFriends();
        List<Group> groups = loggedInUser.getGroups();

        // Generate list of timeslots
        List <String> timeSlots = compareService.createTimeSlots();

        // FInd current week no and year
        int yearNow = LocalDateTime.now().getYear();
        int weekNow = compareService.findWeekNo(LocalDateTime.now());

        // Get comparison list of scheduleItems
        List<String> groupSchedule = compareService.compareScheduleGroup(grpId, weekNow, yearNow);

        // Add necessary attributes to model
        model.addAttribute("comparedSchedule", groupSchedule);
        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("friendList", friends);
        model.addAttribute("groupList", groups);
        model.addAttribute("comparing", true);
        return "Compare";
    }


}
