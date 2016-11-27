package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.persistence.entities.User;
import project.persistence.repositories.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Svava on 16.11.16.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private Repository userRepository = new Repository();

    // Loads a user and authorities by username
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUsersByName(username);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("user"));
        if (user.getUsername() == null) {
            return new org.springframework.security.core.userdetails.User("NotFound","NotFound", grantedAuthorities);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
