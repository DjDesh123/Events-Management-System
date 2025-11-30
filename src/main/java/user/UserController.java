package user;
import user.UserDatabase;
import util.PasswordHashing;

public class UserController {

    private UserDatabase userDatabase;
    private  User loggedInUser;

    public UserController() {
        userDatabase = new UserDatabase();
        UserDatabaseStorage.load();
    }

    public  User getLoggedInUser() {
        return loggedInUser;
    }


    public boolean signUp(int id, String firstName, String lastName, String email, String password, String accountTypeText) {

        // validates basic fields
        if (firstName.isEmpty() || lastName.isEmpty() ||
                email.isEmpty() || password.isEmpty()) {
            return false;
        }

        // generate salt + hashed password
        PasswordHashing ph = new PasswordHashing();
        String salt = ph.getSalt();
        String hashedPassword = ph.hashingPassword(password,salt);

        // convert text to enum
        User.accountType type;
        if (accountTypeText.equalsIgnoreCase("student")) {
            type = User.accountType.STUDENT;
        } else {
            type = User.accountType.ORGANISER;
        }

        // create user object
        User user = new User(id, firstName, lastName, email, hashedPassword, salt, type);

        // stores in local database
        userDatabase.add(user);

        // saves to file
        UserDatabaseStorage.save(userDatabase.getUsers());

        return true;
    }

    public boolean checkPasswordIsValid(String Email, String Password) {

        // Retrieve stored user info
        User user = userDatabase.getByEmail(Email);

        if(user == null){
            System.out.println("Users are not found");
            return false;
        }

        String StoredSalt = user.getSalt();
        String StoredHashedPassword = user.getPassword();

        // Hash input password using stored salt
        PasswordHashing ph = new PasswordHashing();
        String hashedInput = ph.hashingPassword(Password, StoredSalt);

        // Compare stored hash vs the input hash
        if (hashedInput.equals(StoredHashedPassword)) {
            System.out.println("Password Match! User authenticated.");
            return true;
        } else {
            System.out.println("Password Did Not Match! Authentication failed.");
            return false;
        }
    }


    public User logIn(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {return null;}

        User user = userDatabase.getByEmail(email);
        if (user == null) {return null;}


        boolean loginSucessfully = checkPasswordIsValid(email, password);
        if (loginSucessfully) {
            System.out.println("User logged in successfully.");
        }
        else {return null;}

        //keeps track of the session
        User loggedUser = user;
        return user;
    }
}
