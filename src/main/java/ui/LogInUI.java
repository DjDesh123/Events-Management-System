package ui;

import events.EventController;
import events.EventDatabase;
import joinEvents.JoinEventDatabase;
import notifications.NotificationController;
import user.User;
import user.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LogInUI extends JFrame {

    private final UserController userController;
    private final EventDatabase eventDatabase = new EventDatabase();
    private final JoinEventDatabase joinEventDatabase = new JoinEventDatabase();
    private final NotificationController notificationController = new NotificationController();
    private final EventController eventController = new EventController();

    public LogInUI(UserController userController) {
        this.userController = userController;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Login");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(35, 50, 35, 50));

        JLabel title = new JLabel("Welcome", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // Input fields
        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(10));

        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passField.setBorder(BorderFactory.createTitledBorder("Password"));
        panel.add(passField);
        panel.add(Box.createVerticalStrut(10));

        // Forgot Password link
        JLabel forgotPassword = new JLabel("<HTML><U>Forgot Password?</U></HTML>");
        forgotPassword.setForeground(Color.BLUE);
        forgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPassword.setAlignmentX(Component.RIGHT_ALIGNMENT);
        forgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ForgotPasswordUI(userController);
                dispose();
            }
        });
        panel.add(forgotPassword);
        panel.add(Box.createVerticalStrut(15));

        // Login button
        JButton enterBtn = new JButton("Login");
        enterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            var user = userController.logIn(email, pass);

            if (user != null) {
                JOptionPane.showMessageDialog(null, "Login Successful!");

                if (user.getAccountType() == User.accountType.STUDENT) {
                    new StudentDashboardUI(user, eventDatabase, joinEventDatabase,eventController,notificationController);
                } else if (user.getAccountType() == User.accountType.ORGANISER) {
                    EventController controller = new EventController();
                    new OrganiserDashboardUI(user, eventDatabase, joinEventDatabase,eventController,notificationController);
                }

                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid email or password!");
            }
        });
        panel.add(enterBtn);

        // Sign up link container
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signupPanel.setBackground(panel.getBackground());
        JLabel signupLink = new JLabel("<HTML><U>Sign up</U></HTML>");
        signupLink.setForeground(Color.BLUE);
        signupLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new SignUpUI();
                dispose();
            }
        });
        signupPanel.add(signupLink);

        panel.add(Box.createVerticalStrut(10));
        panel.add(signupPanel);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LogInUI(new UserController());
    }
}
