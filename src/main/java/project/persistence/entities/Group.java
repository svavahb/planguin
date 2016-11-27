package project.persistence.entities;
import java.util.*;

/**
 * Created by Þórunn on 11/2/2016.
 * Class for containing info about a group
 */
public class Group {

    private int grpId;
    private List<User> members = new ArrayList<>();
    private String grpName;

    public int getGrpId(){return grpId;}
    public void setGrpId(int grpId){this.grpId = grpId;}

    public String getGrpName(){return grpName;}
    public void setGrpName(String grpName){this.grpName = grpName;}

    public List<User> getMembers(){return members;}
    public void addMember(User user){members.add(user);}
    public void removeMember(User user){members.remove(user);}

    public void changeName(String newname){
        grpName = newname;
    }







}
