package ui;

import user.UserController;
import user.User;

import javax.swing.*;
import java.awt.*;

public class ForgotPasswordUI extends JFrame {

    private final UserController userController;
    private String storedEmail;
    private User account;

    public ForgotPasswordUI(UserController userController) {
        this.userController = userController;

        setTitle("Reset Password");
        setSize(400, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        CardLayout cl = (CardLayout) getContentPane().getLayout();

        // email screen
        JPanel emailPanel = new JPanel(new GridBagLayout());
        emailPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel t1 = new JLabel("Forgot Password?", SwingConstants.CENTER);
        t1.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridy = 0;
        emailPanel.add(t1, gbc);

        JTextField emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Enter Account Email"));
        gbc.gridy = 1;
        emailPanel.add(emailField, gbc);

        JButton next = new JButton("Next");
        gbc.gridy = 2;
        emailPanel.add(next, gbc);

        // reset password screen
        JPanel resetPanel = new JPanel(new GridBagLayout());
        resetPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel t2 = new JLabel("Create New Password", SwingConstants.CENTER);
        t2.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridy = 0;
        resetPanel.add(t2, gbc);

        JPasswordField pass1 = new JPasswordField();
        pass1.setBorder(BorderFactory.createTitledBorder("New Password"));
        gbc.gridy = 1;
        resetPanel.add(pass1, gbc);

        JPasswordField pass2 = new JPasswordField();
        pass2.setBorder(BorderFactory.createTitledBorder("Confirm Password"));
        gbc.gridy = 2;
        resetPanel.add(pass2, gbc);

        JButton save = new JButton("Save");
        gbc.gridy = 3;
        resetPanel.add(save, gbc);

        JLabel back = new JLabel("<HTML><U>Back to Login</U></HTML>");
        back.setForeground(Color.BLUE);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        resetPanel.add(back, gbc);

        // Add Panels
        add(emailPanel, "email");
        add(resetPanel, "reset");

        //button logic

        // check if user exists
        next.addActionListener(e -> {
            String email = emailField.getText().trim();

            account = userController.getUserByEmail(email);

            if (account == null) {
                JOptionPane.showMessageDialog(this, "No account found with that email.");
                return;
            }

            storedEmail = email;
            cl.show(getContentPane(), "reset");
        });

        // update password
        save.addActionListener(e -> {
            String p1 = new String(pass1.getPassword());
            String p2 = new String(pass2.getPassword());

            if (p1.isEmpty() || p2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
                return;
            }

            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            boolean success = userController.forgotPassword(storedEmail, p1);

            if (success) {
                JOptionPane.showMessageDialog(this, "Password updated!");
                new LogInUI(userController); // pass the same controller
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Unexpected error — try again.");
            }
        });

        back.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new LogInUI(userController); // pass the same controller
                dispose();
            }
        });

        setVisible(true);
    }
}
