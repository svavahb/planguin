package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.persistence.entities.User;
import project.service.SearchService;

import java.util.List;

/**
 * Created by halld on 02-Nov-16.
 */

@Controller
public class SearchController {

    SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService){
        this.searchService = searchService;
    }

    @RequestMapping(value="/search/all", method = RequestMethod.GET)
    public String viewGetListOfUsers(Model model){

        List<User> users = searchService.findAll();
        model.addAttribute(users);

        return "Search";
    }

    @RequestMapping(value="/search/{username}", method = RequestMethod.GET)
    public String getSearchByName(@PathVariable("username") String username, Model model){

        System.out.println(username);
        User user = searchService.findByName(username);
        System.out.println("í controller: "+user.getUsername());
        model.addAttribute("User", user);

        return "Search";
    }

    @RequestMapping(value="/search/{userId1}/{userId2}", method = RequestMethod.POST)
    public String addFriendPost(@ModelAttribute("friendship") Boolean friendship,
                                Model model, @PathVariable("userId1") int userId1, @PathVariable("userId2") int userId2){

        model.addAttribute("friendship", searchService.createFriendship(userId1,userId2));

        return "/";
    }
}
