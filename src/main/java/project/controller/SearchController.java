package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.Group;
import project.persistence.entities.User;
import project.service.SearchService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by halld on 02-Nov-16.
 */

@Controller
public class SearchController {

    // Class for sending over user info + info on whether the user is the logged in user's friend
    public class UserHolder {
        User user;
        boolean friendship;

        public User getUser() {
            return user;
        }

        public boolean isFriendship() {
            return friendship;
        }
    }

    // Service for search business logic
    SearchService searchService;

    // Constructor
    @Autowired
    public SearchController(SearchService searchService){
        this.searchService = searchService;
    }

    // Get base list of all users
    @RequestMapping(value="/search")
    public String viewGetListOfUsers(Model model, HttpSession session){
        // CHeck if user is logged in
        if (session.getAttribute("loggedInUser") == null ) {
            return "redirect:/";
        }

        // Get logged in user and info
        String loggedInUsername = session.getAttribute("loggedInUser").toString();
        User loggedInUser = searchService.findByName(loggedInUsername);
        List<User> users = searchService.findAll();
        List<UserHolder> userHolders = new ArrayList<>();
        List<Group> groups = loggedInUser.getGroups();

        model.addAttribute("groupList", groups);
        model.addAttribute("loggedInId",loggedInUser.getUserId());

        // Generate user holders for all users
        for(User user:users ) {
            UserHolder uh = new UserHolder();
            uh.user = user;
            uh.friendship = searchService.checkIfFriend(user, loggedInUser);
            userHolders.add(uh);
        }
        model.addAttribute("users", userHolders);

        return "Search";
    }

    // Method for searching for a certain username
    @RequestMapping(value="/search/q")
    public String getSearchByName(@RequestParam("username") String username, Model model, HttpSession session){
        // Check if user is logged in
        if (session.getAttribute("loggedInUser") == null ) {
            return "redirect:/";
        }

        // Get logged in user and info
        String loggedInUsername = session.getAttribute("loggedInUser").toString();
        User loggedInUser = searchService.findByName(loggedInUsername);

        //Find user searched for
        User user = searchService.findByName(username);
        if(user.getUsername() == null) {
            return "Search";
        }

        // Generate necessary variables for model
        boolean friendship = searchService.checkIfFriend(user, loggedInUser);
        List<Group> groups = loggedInUser.getGroups();
        model.addAttribute("groupList", groups);
        model.addAttribute("loggedInId",loggedInUser.getUserId());
        List<UserHolder> users = new ArrayList<>();
        UserHolder userHolderItem  = new UserHolder();
        userHolderItem.user = user;
        userHolderItem.friendship = friendship;
        users.add(userHolderItem);
        model.addAttribute("users", users);

        return "Search";
    }

    // Post method for adding a user as the logged in user's friend
    @RequestMapping(value="/search/addFriend", method = RequestMethod.POST)
    public String addFriendPost(@RequestParam("userId") int userId, HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("loggedInUser") == null ) {
            return "redirect:/";
        }
        // Find logged in user and info
        String loggedInUsername = session.getAttribute("loggedInUser").toString();
        User loggedInUser = searchService.findByName(loggedInUsername);

        // Find user selected for adding as friend
        User user2 = searchService.findByUserId(userId);

        // If they are not already friends, create the friendship
        if(!searchService.checkIfFriend(loggedInUser, user2)) {
            searchService.createFriendship(loggedInUser.getUserId(), userId);
        }
        return "redirect:/search";
    }

    // Add a user to a certain group
    @RequestMapping(value="/search/addToGroup", method = RequestMethod.POST)
    public String addToGroup(@RequestParam("addToGroup") int grpId, @RequestParam("addMember") int userId, HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("loggedInUser") == null ) {
            return "redirect:/";
        }

        // Find the group and the user
        Group group = searchService.findGroup(grpId);
        User user = searchService.findByUserId(userId);

        // Add the user to the group
        searchService.addGroupMemeber(grpId, userId);
        group.addMember(user);
        
        return "redirect:/search";
    }
}
