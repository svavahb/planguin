package project.service;

import org.springframework.stereotype.Service;
import project.persistence.repositories.Repository;
import project.persistence.entities.*;

import java.util.List;


/**
 * Created by halld on 02-Nov-16.
 */

@Service
public class SearchService {
    Repository repository;

    public SearchService() {

        this.repository = new Repository();
    }

    // Get list of all users
    public List<User> findAll(){
        return repository.findAllUsers();
    }

    // Find a user by name
    public User findByName(String username){
        return repository.findUsersByName(username);
    }

    // Find a user by id
    public User findByUserId(int userId) { return repository.findUserById((userId)); }

    // Create a friendship
    public boolean createFriendship(int userId1, int userId2){
        repository.createFriendship(userId1,userId2);

        User user1 = repository.findUserById(userId1);
        User user2 = repository.findUserById(userId2);

        user1.addFriend(user2);
        user2.addFriend(user1);

        return true;
    }

    // Check if user1 and user2 are friends
    public boolean checkIfFriend(User user1, User user2) {
        for (User u : user1.getFriends()) {
            if (u.getUserId() == user2.getUserId()) {
                return true;
            }
        }
        for (User u : user2.getFriends()) {
            if (u.getUserId() == user1.getUserId()) {
                return true;
            }
        }
        return false;
    }

    // Find a group by id
    public Group findGroup(int grpId) {
        return repository.findGroup(grpId);
    }

    // Add a user to a group
    public void addGroupMemeber(int groupId, int userId) {
        repository.addGroupMember(groupId, userId);
    }
}
