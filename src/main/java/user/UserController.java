package user;

import util.PasswordHashing;
import java.util.Map;

public class UserController {

    private UserDatabase userDatabase;
    private static User loggedInUser;

    public UserController() {
        // Create a new in-memory database
        userDatabase = new UserDatabase();

        // Load users from storage and populate database
        Map<Integer, User> loadedUsers = UserDatabaseStorage.load();
        if (loadedUsers != null) {
            for (User u : loadedUsers.values()) {
                userDatabase.add(u);
            }
        }

        System.out.println("Users loaded: " + userDatabase.getUsers().size());
        for (User u : userDatabase.getUsers().values()) {
            System.out.println("Loaded user: " + u.getEmail());
        }
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public boolean signUp(int id, String firstName, String lastName, String email, String password, String accountTypeText) {
        System.out.println("=== SIGNUP ATTEMPT ===");
        System.out.println("Email: '" + email + "'");
        System.out.println("Password: '" + password + "'");

        // Validate basic fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Signup failed: Missing fields.");
            return false;
        }

        // Generate salt + hashed password
        PasswordHashing ph = new PasswordHashing();
        String salt = ph.getSalt();
        String hashedPassword = ph.hashingPassword(password, salt);

        // Convert text to enum
        User.accountType type = accountTypeText.equalsIgnoreCase("student") ?
                User.accountType.STUDENT : User.accountType.ORGANISER;

        // Generate ID if 0 or negative
        if (id <= 0) {
            id = userDatabase.generateNewId();
        }

        // Create user object
        User user = new User(id, firstName, lastName, email, hashedPassword, salt, type);

        // Add to database and save
        userDatabase.add(user);
        UserDatabaseStorage.save(userDatabase.getUsers());

        System.out.println("Signup successful: User '" + firstName + "' added with ID " + id);
        return true;
    }

    public boolean checkPasswordIsValid(String email, String password) {
        System.out.println("=== CHECK PASSWORD ===");
        System.out.println("Input email: '" + email + "'");
        System.out.println("Input password: '" + password + "'");

        // Retrieve stored user info
        User user = userDatabase.getByEmail(email);
        if (user == null) {
            System.out.println("Users are not found for email: '" + email + "'");
            return false;
        }

        System.out.println("User found: " + user.getFirstName() + " " + user.getLastName());
        System.out.println("Stored hashed password: " + user.getPassword());
        System.out.println("Stored salt: " + user.getSalt());

        // Hash input password using stored salt
        PasswordHashing ph = new PasswordHashing();
        String hashedInput = ph.hashingPassword(password, user.getSalt());
        System.out.println("Hashed input password: " + hashedInput);

        // Compare stored hash vs input hash
        if (hashedInput.equals(user.getPassword())) {
            System.out.println("Password Match! User authenticated.");
            return true;
        } else {
            System.out.println("Password Did Not Match! Authentication failed.");
            return false;
        }
    }

    public User logIn(String email, String password) {
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Input email: '" + email + "'");
        System.out.println("Input password: '" + password + "'");

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Login failed: Email or password is empty.");
            return null;
        }

        boolean loginSuccessfully = checkPasswordIsValid(email, password);
        System.out.println("Password check result: " + loginSuccessfully);

        if (!loginSuccessfully) {
            System.out.println("Login failed: Incorrect email or password.");
            return null;
        }

        // Set session
        loggedInUser = userDatabase.getByEmail(email);
        System.out.println("User logged in successfully: " + loggedInUser.getFirstName());
        return loggedInUser;
    }


    public boolean forgotPassword(String email, String newPassword) {
        // Get user from database
        User account = userDatabase.getByEmail(email);
        if (account == null) {
            System.out.println("Forgot password failed: User not found.");
            return false;
        }

        // Generate new salt + hash
        PasswordHashing ph = new PasswordHashing();
        String newSalt = ph.getSalt();
        String hashedPassword = ph.hashingPassword(newPassword, newSalt);

        // Update user object
        account.setPassword(hashedPassword);
        account.setSalt(newSalt);

        // Update database storage
        UserDatabaseStorage.save(userDatabase.getUsers());

        System.out.println("Password updated for user: " + email);
        return true;
    }
    // In UserController
    public User getUserByEmail(String email) {
        return userDatabase.getByEmail(email);
    }

}
