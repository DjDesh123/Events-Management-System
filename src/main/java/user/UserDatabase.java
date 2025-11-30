package user;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDatabase {
    private Map<Integer,User> userMap = new HashMap<>();
    private AtomicInteger idCounter = new AtomicInteger(0);



    public int generateNewId() {
        return idCounter.incrementAndGet();
    }

    //adds the users to the local database
    public void add(User user){
        if (user.getUserId() > idCounter.get()) {
            idCounter.set(user.getUserId());
        }
        userMap.put(user.getUserId(),user);
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

    // helper class
    public User getByEmail(String email) {
        for (User user : userMap.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

}
