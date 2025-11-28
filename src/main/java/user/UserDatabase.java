package user;

import user.User;
import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private Map<Integer,User> userMap = new HashMap<>();

    //adds the users to the local database
    public void add(User user){
        userMap.put(user.getUserid(),user);
    }

    // gets the certain users infomation
    public User get(int userid){
        return userMap.get(userid);
    }

    // deletes the user from the local database
    public void delete(int userId){
        userMap.remove(userId);
    }

    // gets all the users
    public Map<Integer,User> getUsers(){
        return userMap;
    }
}
