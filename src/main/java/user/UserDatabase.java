package user;

import user.User;
import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private Map<Integer,User> users = new HashMap<>();

    //adds the users to the local database
    public void add(User user){
        users.put(user.getUserid(),user);
    }

    // gets the certain users infomation
    public User get(int userid){
        return users.get(userid);
    }

    // deletes the user from the local database
    public void delete(int userId){
        users.remove(userId);
    }

    // gets all the users
    public Map<Integer,User> getUsers(){
        return users;
    }
}
