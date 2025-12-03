package ui;

import user.UserController;
import user.UserDatabase;

import javax.swing.*;
import java.awt.*;

public class SignUpUI extends JFrame {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    private JToggleButton studentToggle;
    private JToggleButton organizerToggle;

    private final UserController userController = new UserController();
    private final UserDatabase userDatabase = new UserDatabase();

    public SignUpUI() {
        setTitle("Sign Up");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(220, 220, 220));

        // Title
        JLabel title = new JLabel("EVENTIFY", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(75, 40, 300, 60);
        title.setOpaque(true);
        title.setBackground(Color.WHITE);
        add(title);

        // Toggle Buttons
        studentToggle = new JToggleButton("Student");
        organizerToggle = new JToggleButton("Organizer");

        ButtonGroup group = new ButtonGroup();
        group.add(studentToggle);
        group.add(organizerToggle);

        studentToggle.setSelected(true);

        studentToggle.setBounds(120, 120, 90, 30);
        organizerToggle.setBounds(230, 120, 90, 30);

        add(studentToggle);
        add(organizerToggle);

        // Input fields
        firstNameField = createInputField("First name", 180);
        lastNameField = createInputField("Last name", 230);
        emailField = createInputField("Email", 280);

        passwordField = new JPasswordField();
        passwordField.setBounds(110, 330, 230, 35);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        add(passwordField);

        // Enter Button
        JButton enterButton = new JButton("Enter");
        enterButton.setBounds(160, 390, 130, 40);
        enterButton.setBackground(Color.BLACK);
        enterButton.setForeground(Color.WHITE);
        enterButton.addActionListener(e -> handleSignUp());
        add(enterButton);

        // Sign in hyperlink
        JLabel signInLink = new JLabel("<HTML><U>Sign in</U></HTML>");
        signInLink.setForeground(Color.BLUE);
        signInLink.setBounds(200, 450, 100, 25);
        signInLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        signInLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("SIGN IN LINK CLICKED (switch to login window here)");
            }
        });

        add(signInLink);

        setVisible(true);
    }

    private JTextField createInputField(String placeholder, int y) {
        JTextField field = new JTextField(placeholder);
        field.setBounds(110, y, 230, 35);
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setHorizontalAlignment(JTextField.CENTER);

        // remove placeholder text on click
        field.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }
        });

        add(field);
        return field;
    }

    private void handleSignUp() {
        String fname = firstNameField.getText().trim();
        String lname = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        String accountType = studentToggle.isSelected() ? "student" : "organizer";

        int id = userDatabase.generateNewId();

        boolean success = userController.signUp(id, fname, lname, email, pass, accountType);

        if (success) {
            System.out.println("USER CREATED SUCCESSFULLY");
            JOptionPane.showMessageDialog(this, "Account created!");
        } else {
            System.out.println("FAILED TO CREATE USER");
            JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
        }
    }

    public static void main(String[] args) {
        new SignUpUI();
    }
}
