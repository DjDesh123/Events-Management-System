package user;

public class User{
    // all attributes needed to make a user
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String salt;

    // to improve security and be more efficient than just a string
    public enum accountType{
        STUDENT,
        ORGANISER
    }

    private accountType accountType;


    public User(int userId,String firstName, String lastName, String email, String password, String salt, accountType accountType) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.accountType=accountType;
    }


    // all the getters for each attribute

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public accountType getAccountType() {
        return accountType;
    }


    // SETTER

    public void setPassword(String password) {
        this.password =  password;
    }
}
