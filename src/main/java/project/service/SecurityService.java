package project.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by Svava on 16.11.16.
 */
@Service
public class SecurityService {

    // Used for checking password
    PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    private UserDetailsService userDetailsService = new UserDetailsServiceImpl();

    // Log in user
    public boolean autologin(String username, String password) {
        // Check if username already exists
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails.getUsername().equals("NotFound")) {
            return false;
        }

        // Check manually whether password is correct
        if (pwEncoder.matches(password, userDetails.getPassword())) {
            return true;
        }
        return false;
    }
}
