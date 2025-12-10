package ui;

import user.UserController;

import javax.swing.*;
import java.awt.*;

public class SettingsUI extends JFrame {

    private final JFrame parentFrame;
    private final UserController userController;
    private JSlider redSlider, greenSlider, blueSlider, fontSizeSlider;

    public SettingsUI(JFrame parent, UserController userController) {
        this.parentFrame = parent;
        this.userController = userController;

        setTitle("Settings");
        setSize(400, 380);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(10));

        // sliders
        add(createSliderPanel("Red", 0, 255));
        add(createSliderPanel("Green", 0, 255));
        add(createSliderPanel("Blue", 0, 255));
        add(createSliderPanel("Font Size", 10, 30));
        add(Box.createVerticalStrut(20));

        // buttons
        JPanel buttons = new JPanel(new FlowLayout());

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> applySettings());

        JButton signOutBtn = new JButton("Sign Out");
        signOutBtn.setBackground(Color.RED);
        signOutBtn.setForeground(Color.WHITE);
        signOutBtn.setOpaque(true);
        signOutBtn.setFocusPainted(false);
        signOutBtn.addActionListener(e -> {
            parentFrame.dispose();
            dispose();
            JOptionPane.showMessageDialog(null, "Signed out.");
            new LogInUI(userController);
        });

        buttons.add(saveBtn);
        buttons.add(signOutBtn);
        add(buttons);

        setVisible(true);
    }

    private JPanel createSliderPanel(String name, int min, int max) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(350, 50));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(name);
        JSlider slider = new JSlider(min, max);

        panel.add(label, BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);

        switch (name) {
            case "Red": redSlider = slider; break;
            case "Green": greenSlider = slider; break;
            case "Blue": blueSlider = slider; break;
            case "Font Size": fontSizeSlider = slider; break;
        }
        return panel;
    }

    private void applySettings() {
        int r = redSlider.getValue();
        int g = greenSlider.getValue();
        int b = blueSlider.getValue();
        int fontSize = fontSizeSlider.getValue();

        Color color = new Color(r, g, b);

        styleComponents(parentFrame.getContentPane(), color, fontSize);

        parentFrame.revalidate();
        parentFrame.repaint();

        JOptionPane.showMessageDialog(this, "Settings applied!");
    }

    private void styleComponents(Component comp, Color color, int fontSize) {
        comp.setFont(comp.getFont().deriveFont((float) fontSize));

        // recolor only background elements
        if (comp instanceof JPanel || comp instanceof JButton) {
            if (!(comp instanceof JButton &&
                    ((JButton) comp).getText().equalsIgnoreCase("Sign Out"))) {
                comp.setBackground(color);
            }
        }

        // Java 11 compatible
        if (comp instanceof Container) {
            Container container = (Container) comp;
            for (Component child : container.getComponents()) {
                styleComponents(child, color, fontSize);
            }
        }
    }
}
