package ui;

import user.User;
import user.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LogInUI extends JFrame {

    private UserController userController = new UserController();

    public LogInUI() {
        setTitle("Eventify Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(225, 225, 225)); // matches your image

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ----- TITLE BOX -----
        JPanel titleBox = new JPanel();
        titleBox.setPreferredSize(new Dimension(250, 60));
        titleBox.setBackground(Color.WHITE);
        titleBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JLabel title = new JLabel("EVENTIFY", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        titleBox.setLayout(new BorderLayout());
        titleBox.add(title, BorderLayout.CENTER);

        gbc.gridy = 0;
        main.add(titleBox, gbc);

        // ----- ENTRY FIELD MAKER -----
        JTextField emailField = createInputField("Email");
        JPasswordField passwordField = createPasswordField("Password");

        gbc.gridy++;
        main.add(emailField, gbc);

        gbc.gridy++;
        main.add(passwordField, gbc);

        // ----- FORGOT PASSWORD HYPERLINK -----
        JLabel forgot = createHyperlink("forgot password?");
        gbc.gridy++;
        main.add(forgot, gbc);

        forgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Forgot Password clicked.");
            }
        });

        // ----- SIGN IN BUTTON -----
        JButton signIn = new JButton("Enter");
        signIn.setPreferredSize(new Dimension(90, 30));
        signIn.setBackground(new Color(50, 50, 50));
        signIn.setForeground(Color.WHITE);
        signIn.setFocusPainted(false);
        signIn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        gbc.gridy++;
        main.add(signIn, gbc);

        // *** CONNECT TO BACKEND HERE ***
        signIn.addActionListener(e -> {

            String email = emailField.getText().trim();
            String password = String.valueOf(passwordField.getPassword());

            // Ignore placeholders
            if (email.equals("Email")) email = "";
            if (password.equals("Password")) password = "";

            User user = userController.logIn(email, password);

            if (user != null) {
                System.out.println("SUCCESS: Logged in → " + user.getFirstName());
            } else {
                System.out.println("ERROR: Invalid login.");
            }
        });

        // ----- SIGN UP HYPERLINK -----
        JLabel signUp = createHyperlink("Sign up");
        gbc.gridy++;
        main.add(signUp, gbc);

        signUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                    }
                }); {
                    // Close the current login window
                    LogInUI.this.dispose();
                    new SignUpUI();

                }
            }
        });

        add(main);
        setVisible(true);
    }

    // ---------------- HELPER: Create textbox with placeholder ----------------
    private JTextField createInputField(String placeholder) {
        JTextField field = new JTextField(15);
        field.setPreferredSize(new Dimension(180, 32));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        return field;
    }

    // ---------------- HELPER: Password placeholder ----------------
    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(15);
        field.setPreferredSize(new Dimension(180, 32));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0);
        field.setText(placeholder);
        field.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                String text = new String(field.getPassword());
                if (text.equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('•');
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                String text = new String(field.getPassword());
                if (text.isEmpty()) {
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });

        return field;
    }

    // ---------------- HELPER: Hyperlink label ----------------
    private JLabel createHyperlink(String text) {
        JLabel link = new JLabel("<html><a href='#'>" + text + "</a></html>");
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.setHorizontalAlignment(SwingConstants.CENTER);
        return link;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LogInUI::new);
    }
}
