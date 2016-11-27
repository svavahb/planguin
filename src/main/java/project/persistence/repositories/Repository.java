package project.persistence.repositories;

import org.springframework.cglib.core.Local;
import project.persistence.entities.Group;
import project.persistence.entities.ScheduleItem;
import project.persistence.entities.User;

import java.sql.Timestamp;
import java.time.*;
import java.util.List;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.postgresql.Driver;
import project.persistence.repositories.Mappers.GroupMapper;
import project.persistence.repositories.Mappers.ItemMapper;
import project.persistence.repositories.Mappers.UserMapper;

/**
 * Created by Svava on 03.11.16.
 * Class for database connection and logic
 */
public class Repository implements RepositoryInterface {

    // The template used for executing SQL queries
    private JdbcTemplate jdbcTemplate;

    // Constructor
    public Repository() {
        // Set up datasource and connection
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/planguin");
        dataSource.setUsername("postgres");
        dataSource.setPassword("lalli");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // Get list of all users in database
    @Override
    public List<User> findAllUsers(){
        String SQL = "select * from \"user\";";
        List<User> users = jdbcTemplate.query(SQL, new UserMapper());

        // Get all user's friends
        SQL = "select * from \"user\" where id in (select userid1 from Friendship where userid2=?) or id in (select userid2 from Friendship where userid1=?)";
        for (User u : users) {
            List<User> friends = jdbcTemplate.query(SQL, new Object[]{u.getUserId(), u.getUserId()}, new UserMapper());
            for (User f : friends) {
                if(f.getUsername()!=u.getUsername()) {
                    u.addFriend(f);
                }
            }
        }

        // Get all user's groups
        SQL = "select * from \"group\" where id in (select groupid from members where userid=?)";
        List<Group> groups = jdbcTemplate.query(SQL, new Object[]{users.get(0).getUserId()}, new GroupMapper());
        for (User u : users) {
            for (Group grp : groups) {
                u.addGroup(grp);
            }
        }

        return users;
    }

    // Get a user by username.
    public User findUsersByName(String username){
        String SQL = "select * from \"user\" where username=?;";
        List<User> users = jdbcTemplate.query(SQL, new Object[]{username}, new UserMapper());

        // If no users found, return empty user
        if(users.size()==0) return new User();

        // Find all user's friends
        SQL = "select * from \"user\" where id in (select userid1 from Friendship where userid2=?) or id in (select userid2 from Friendship where userid1=?)";
        for (User u : users) {
            List<User> friends = jdbcTemplate.query(SQL, new Object[]{u.getUserId(), u.getUserId()}, new UserMapper());
            for (User f : friends) {
                if(f.getUsername()!=u.getUsername()) {
                    u.addFriend(f);
                }
            }
        }

        // Find all user's group
        SQL = "select * from \"group\" where id in (select groupid from members where userid=?)";
        List<Group> groups = jdbcTemplate.query(SQL, new Object[]{users.get(0).getUserId()}, new GroupMapper());
        for (User u : users) {
            for (Group grp : groups) {
                u.addGroup(grp);
            }
        }

        if(users.size()==0) return new User();
        // Return onlt the first user found (should only be one, usernames are validated to be unique on sign up
        if(users.size()>=1) return users.get(0);
        return null;
    }

    // Find user by user id
    public User findUserById(int userId) {
        String SQL = "select * from \"user\" where id=?";
        List<User> users = jdbcTemplate.query(SQL, new Object[]{userId}, new UserMapper());

        // If no user found, return null
        if(users.size()==0) return null;

        User user = users.get(0);
        SQL = "select * from \"user\" where id in (select userid1 from Friendship where userid2=?) or id in (select userid2 from Friendship where userid1=?)";

        // Find all user's friends
        for (User u : users) {
            List<User> friends = jdbcTemplate.query(SQL, new Object[]{u.getUserId(), u.getUserId()}, new UserMapper());
            for (User f : friends) {
                if(f.getUsername()!=u.getUsername()) {
                    u.addFriend(f);
                }
            }
        }

        // Find all user's groups
        SQL = "select * from \"group\" where id in (select groupid from members where userid=?)";
        List<Group> groups = jdbcTemplate.query(SQL, new Object[]{users.get(0).getUserId()}, new GroupMapper());
        for (User u : users) {
            for (Group grp : groups) {
                u.addGroup(grp);
            }
        }

        return user;
    }

    // Find all items by userid, week no and year
    public List<ScheduleItem> findItemsByUserWeek(int userId, int weekNo, int year){
        String SQL = "select * from \"scheduleItem\" where userid = ? and \"weekNo\" = ? and year=?;";
        List<ScheduleItem> items = jdbcTemplate.query(SQL, new Object[]{userId, weekNo, year}, new ItemMapper());

        // Find all filters associated with each item
        SQL="select name from Filters where itemId=?";
        for (ScheduleItem i : items) {
            List<String> filters = jdbcTemplate.queryForList(SQL, new Object[]{i.getId()}, String.class);
            for (String f : filters) {
                i.addFilter(f);
            }
        }
        return items;
    }

    // Find all items by user id, week no, year and filter
    public List<ScheduleItem> findItemsByUserWeekFilter(int userId, int weekNo, int year, String filter) {
        String SQL = "select * from \"scheduleItem\" where userid = ? and \"weekNo\" = ? and year=? and id in (select itemid from filters where name=?)";
        List<ScheduleItem> items = jdbcTemplate.query(SQL, new Object[]{userId, weekNo, year, filter}, new ItemMapper());

        // Find all filters associated with each item
        SQL="select name from Filters where itemId=?";
        for (ScheduleItem i : items) {
            List<String> filters = jdbcTemplate.queryForList(SQL, new Object[]{i.getId()}, String.class);
            for (String f : filters) {
                i.addFilter(f);
            }
        }
        return items;
    }

    // Insert a user into the database
    public int createUser(String password,String photo, String username, String school){
        String SQL = "insert into \"user\" (password, photo, username, school) values (?,?,?,?);";
        jdbcTemplate.update(SQL, new Object[]{password, photo, username, school});

        // Find the inserted user's id
        int userid = getUserByUsername(username);
        return userid;
    }

    // Get user id only by username (could be replaced by findUserByUsername!)
    public int getUserByUsername(String username){
        String SQL = "select id from \"user\" where username =?;";
        int userid;
        try {
            userid = jdbcTemplate.queryForObject(SQL, new Object[]{username}, Integer.class);
        }
        catch (Exception e){
            return -1;
        }
        return userid;
    }

    // Edit user info (never used in this version)
    public void editUser(int userId, String username, String password, String photo, String school){
        String SQL = "update \"user\" set username = ?, password = ?, photo = ?, school = ? where id = ?;";
        jdbcTemplate.update(SQL, username, password, photo, school, userId);
    }

    // Insert item into database
    public int createItem(String title, int userId, LocalDateTime startTime, LocalDateTime endTime,
                   int weekNo, int year, String location, String color, String description){
        String SQL="insert into \"scheduleItem\" (title, userid, \"startTime\", \"endTime\", \"weekNo\", year, location, color, description) " +
                "values (?,?,?,?,?,?,?,?,?);";
        jdbcTemplate.update(SQL, title, userId, Timestamp.valueOf(startTime), Timestamp.valueOf(endTime), weekNo, year, location, color, description);

        // Find newly inserted item's id. Causes an error when user accidentally resends info in http request (so two items exist with same info)
        SQL="select id from \"scheduleItem\" where  title = ? and userid= ? and \"startTime\" = ? and \"endTime\" = ? and \"weekNo\" = ? and year = ? and location = ? and color = ? and description = ?";
        int itemid = jdbcTemplate.queryForObject(SQL, new Object[]{title, userId,  Timestamp.valueOf(startTime), Timestamp.valueOf(endTime),weekNo,year,location,color,description}, Integer.class);
        return itemid;
    }

    // Deletes item from database by id
    public void deleteItem(int itemId){
        // Delete from filters first, to avoid foreign key reference errors
        String SQL = "delete from filters where itemid=?";
        jdbcTemplate.update(SQL, itemId);

        SQL="delete from \"scheduleItem\" where id = ?";
        jdbcTemplate.update(SQL, itemId);
    }

    // Edits item in database (never used in this version)
    public void editItem(int itemId, String title, int userId, LocalDateTime startTime, LocalDateTime endTime, int weekNo, int year,
                         String location, String color, String description, List<User> taggedUsers, List<String> filters){
        String SQL="update \"scheduleitem\" set title=?, userid=?, startTime=?, endTime=?, weekNo=?, year=?, location=?, " +
                "color=?, description=? where id=?;";
        jdbcTemplate.update(SQL, title, userId, startTime, endTime, weekNo, year, location, color, description, itemId);

    }

    // Adds filter to an item
    public void addFilterToItem(int userId, int itemId, String filterName) {
        String SQL ="insert into \"filters\" (userId, itemId, name) values (?,?,?);";
        jdbcTemplate.update(SQL, userId, itemId, filterName);
    }

    // Removes filter from an item
    public void removeFilterFromItem(int userId, int itemId, String filtername) {
        String SQL = "delete from \"filters\" where name=? and itemId=? and userId=?";
        jdbcTemplate.update(SQL, filtername, itemId, userId);
    }

    // Find group by id
    public Group findGroup(int grpId) {
        String SQL="select * from \"group\" where id = ?";
        List<Group> groups = jdbcTemplate.query(SQL, new Object[]{grpId}, new GroupMapper());

        if(groups.size()==0) return null;

        Group group = groups.get(0);
        // Get all of group's members
        SQL = "select * from \"user\" where id in (select userid from members where groupid=?)";
        List<User> members = jdbcTemplate.query(SQL, new Object[]{grpId}, new UserMapper());
        for (User u : members) {
            group.addMember(u);
        }
        return group;
    }

    // Find a group id by name
    public int findGroupByName(String grpName) {
        String SQL = "select id from \"group\" where \"name\" = ?";
        List<Integer> grpId = jdbcTemplate.queryForList(SQL, new Object[]{grpName}, Integer.class);
        // If none found, return -1
        if(grpId.size()==0) return -1;
        else return grpId.get(0);
    }

    // Insert a group into database
    public int createGroup(String grpName, List<User> members){
        String SQL="insert into \"group\" (name) values (?)";
        jdbcTemplate.update(SQL, grpName);

        int grpId = findGroupByName(grpName);

        SQL="insert into Members (groupid, userid) values (?,?)";
        for (User u : members) {
            jdbcTemplate.update(SQL, new Object[]{grpId, u.getUserId()});
        }
        return grpId;
    }

    // Delete a group from database
    public void deleteGroup(int grpId){
        // Delete members first to avoid foreign key errors
        String SQL = "delete from members where groupid=?";
        jdbcTemplate.update(SQL, new Object[]{grpId});

        SQL = "delete from \"group\" where id=?";
        jdbcTemplate.update(SQL, grpId);
    }

    // Edit a group's name (never used in this version)
    public void editGroupName(int grpId, String grpName){
        String SQL = "update \"group\" set name=? where id = ?";
        jdbcTemplate.update(SQL, new Object[]{grpName, grpId});
    }

    // Adds a user to a group
    public void addGroupMember(int groupId, int userId){
        String SQL="insert into \"members\" (groupid, userid) values (?,?)";
        jdbcTemplate.update(SQL, groupId, userId);
    }

    // Removes a user from a group (never used in this version)
    public void removeGroupMember(int groupId, int userId){
        String SQL="delete from \"members\" where groupId=? and userId=? ";
        jdbcTemplate.update(SQL, userId);
    }

    // Inserts a friendship into database
    public void createFriendship(int userId1, int userId2){
        String SQL="insert into \"friendship\" (userid1, userid2) values (?,?)";
        jdbcTemplate.update(SQL, userId1, userId2);
    }

    // Deletes a friendship from database
    public void deleteFriendship(int userid1, int userid2){
        String SQL="delete from \"friendship\" where ( userid1=? and userid2=? ) or (userid2=? and userid1=?)";
        jdbcTemplate.update(SQL,userid1,userid2,userid1, userid2);
    }

    // Inserts a filter into database
    public void createFilter(String filterName, int userId, int itemId){
        String SQL="insert into \"filters\" (name, userId, itemId) values (?,?,?)";
        jdbcTemplate.update(SQL, filterName, userId, itemId);
    }

    // Deletes a filter from database
    public void deleteFilter(int filterId){
        String SQL="delete from \"filters\" where id=?";
        jdbcTemplate.update(SQL, filterId);
    }
}
