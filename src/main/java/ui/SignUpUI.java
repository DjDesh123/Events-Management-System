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
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel title = new JLabel("EVENTIFY", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // Account type toggles
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        studentToggle = new JToggleButton("Student");
        organizerToggle = new JToggleButton("Organizer");
        ButtonGroup group = new ButtonGroup();
        group.add(studentToggle);
        group.add(organizerToggle);
        studentToggle.setSelected(true);
        togglePanel.add(studentToggle);
        togglePanel.add(organizerToggle);
        panel.add(togglePanel);
        panel.add(Box.createVerticalStrut(15));

        // Input fields
        firstNameField = createInputField("First Name");
        lastNameField = createInputField("Last Name");
        emailField = createInputField("Email");
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        panel.add(firstNameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lastNameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(20));

        // Panel to hold button + link vertically, centered
        JPanel buttonLinkPanel = new JPanel();
        buttonLinkPanel.setLayout(new BoxLayout(buttonLinkPanel, BoxLayout.Y_AXIS));
        buttonLinkPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton enterButton = new JButton("Sign Up");
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterButton.addActionListener(e -> handleSignUp());
        buttonLinkPanel.add(enterButton);

        buttonLinkPanel.add(Box.createVerticalStrut(10)); // space between button and link

        JLabel loginLink = new JLabel("<HTML><U>Login</U></HTML>");
        loginLink.setForeground(Color.BLUE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new LogInUI(userController);
                dispose();
            }
        });

        buttonLinkPanel.add(loginLink);

        panel.add(buttonLinkPanel); // add the button+link panel to main panel

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JTextField createInputField(String title) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setBorder(BorderFactory.createTitledBorder(title));
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
            JOptionPane.showMessageDialog(this, "Account created!");
            new LogInUI(userController); // redirect to login
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
        }
    }

    public static void main(String[] args) {
        new SignUpUI();
    }
}
