package user;

import java.io.Serializable;

public class User implements Serializable {
    // all attributes needed to make a user
    private int userid;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String salt;

    // to improve security and be more efficient than just a string
    private enum accountType{
        STUDENT,
        ORGANISER
    }

    private accountType accountType;


    public User(int userid,String firstName, String lastName, String email, String password, String salt, accountType accountType) {
        this.userid = userid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.accountType=accountType;
    }


    // all the getters for each attribute

    public int getUserid() {
        return userid;
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

}
