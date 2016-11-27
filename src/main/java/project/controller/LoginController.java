package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.User;
import project.service.LoginService;
import project.service.SecurityService;
import project.validator.UserValidator;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by halld on 02-Nov-16.
 */

@Controller
public class LoginController {

    // Main service for log in business logic
    LoginService loginService;

    // Service for security
    @Autowired
    SecurityService securityService = new SecurityService();

    // Service for validation of user input
    UserValidator userValidator = new UserValidator();

    @Autowired
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    // Get login page
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String user(Model model){
        model.addAttribute("LogIn",new User());
        return "LogIn";
    }

    // Get sign up page
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model){
        //String test = "test name";
        model.addAttribute("SignUp", new User());
        model.addAttribute("errors", null);
        model.addAttribute("hasErrors", false);
        return "signUp";
    }

    // Post method for signing up user
    @PostMapping(value="/signup")
    public String signUpPost(@ModelAttribute("SignUp") User SignUp, BindingResult bindingResult, Model model, HttpSession session) {
        // Validate input
        userValidator.validate(SignUp, bindingResult);

        // If errors in validation, reload page with errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "signUp";
        }

        // Create user in database
        User user = loginService.createUser(SignUp.getUsername(),SignUp.getPassword(), SignUp.getPhoto(), SignUp.getSchool());

        // Log in user automatically
        securityService.autologin(user.getUsername(), SignUp.getPassword());
        session.setAttribute("loggedInUser",SignUp.getUsername());

        return "redirect:/home";
    }

    // Post method for logging in user
    @PostMapping(value="/login")
    public String LogInPost(@ModelAttribute("LogIn") User LogIn, Model model, HttpSession session) {
        // Log in user
        boolean isLoggedIn = securityService.autologin(LogIn.getUsername(), LogIn.getPassword());
        if (isLoggedIn) {
            session.setAttribute("loggedInUser",LogIn.getUsername());
        }
        // If login failed, reload page with error
        else {
            model.addAttribute("loginfail", true);
            return "LogIn";
        }

        // Else redirect to home page of logged in user
        return "redirect:/home";
    }

    // Method for logging a user out
    @RequestMapping(value="/logout")
    public String LogOut(Model model, HttpSession session) {
        // Empty the session attribute
        session.removeAttribute("loggedInUser");
        //Redirect to index page
        return "redirect:/";
    }

}
