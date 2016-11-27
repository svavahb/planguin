package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import project.persistence.entities.*;
import project.persistence.repositories.Repository;

/**
 * Created by halld on 02-Nov-16.
 */

@Service
public class LoginService {

    Repository repository;

    // Encryption helper object
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginService() {
        this.repository = new Repository();
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    // Create a user
    public User createUser(String username, String password, String photo, String school) {
        User user = new User();
        // Encrypt password
        password = bCryptPasswordEncoder.encode(password);
        int id = repository.createUser(password, photo, username, school);
        user.setUserId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setPhoto(photo);
        user.setSchool(school);
        return user;
    }

    // Check if username is taken
    public boolean usernameExists(String username) {
        int id = repository.getUserByUsername(username);
        if (id == -1) return false;
        else return true;
    }
}
