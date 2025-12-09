package ui;

import events.EventController;
import events.EventDatabase;
import joinEvents.JoinEventDatabase;
import notifications.NotificationController;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CreateEventUI extends JFrame {

    private final User currentUser;
    private final EventController eventController;
    private final EventDatabase eventDatabase;
    private final JoinEventDatabase joinEventDatabase;
    private final NotificationController notificationController;

    public CreateEventUI(User currentUser, EventDatabase eventDatabase,
                         JoinEventDatabase joinEventDatabase, EventController eventController,
                         NotificationController notificationController) {
        this.currentUser = currentUser;
        this.eventDatabase = eventDatabase;
        this.eventController = eventController;
        this.joinEventDatabase = joinEventDatabase;
        this.notificationController = notificationController;

        setTitle("Create Event");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(sidebar(), BorderLayout.WEST);
        add(mainContent(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel sidebar() {
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(180, getHeight()));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(245, 245, 245));

        JButton homeBtn = sideButton("Home");
        homeBtn.addActionListener(e -> {
            new OrganiserDashboardUI(currentUser, eventDatabase, joinEventDatabase, eventController, notificationController);
            dispose();
        });

        JButton searchBtn = sideButton("Search");
        searchBtn.addActionListener(e -> {
            new SearchUI(currentUser, eventDatabase, joinEventDatabase, eventController, notificationController);
            dispose();
        });

        JButton createBtn = sideButton("Create Event");
        JButton settingsBtn = sideButton("Settings");

        side.add(homeBtn);
        side.add(searchBtn);
        side.add(createBtn);
        side.add(settingsBtn);
        side.add(Box.createVerticalGlue());

        return side;
    }

    private JButton sideButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(230, 230, 230));
        btn.setFocusPainted(false);
        return btn;
    }

    private JPanel mainContent() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(Color.WHITE);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(250, 250, 250));
        box.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("Create Event", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(title);
        box.add(Box.createVerticalStrut(25));

        JTextField titleField = wideInput("Event Title (Required)");
        JTextField locationField = wideInput("Location (Required)");
        box.add(titleField); box.add(Box.createVerticalStrut(15)); box.add(locationField); box.add(Box.createVerticalStrut(15));

        JPanel startDatePanel = datePanel(); JPanel endDatePanel = datePanel();
        box.add(new JLabel("Start Date")); box.add(startDatePanel); box.add(Box.createVerticalStrut(10));
        box.add(new JLabel("End Date")); box.add(endDatePanel); box.add(Box.createVerticalStrut(15));

        JPanel startTimePanel = timePanel(); JPanel endTimePanel = timePanel();
        box.add(new JLabel("Start Time")); box.add(startTimePanel); box.add(Box.createVerticalStrut(10));
        box.add(new JLabel("End Time")); box.add(endTimePanel); box.add(Box.createVerticalStrut(15));

        JTextField capacityField = wideInput("Capacity (Required)");
        JTextField descriptionField = wideInput("Description (Required)");
        box.add(capacityField); box.add(Box.createVerticalStrut(10)); box.add(descriptionField); box.add(Box.createVerticalStrut(25));

        JButton submitBtn = new JButton("Create Event");
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.addActionListener(e -> handleSubmit(titleField, locationField, startDatePanel, endDatePanel,
                startTimePanel, endTimePanel, capacityField, descriptionField));
        box.add(submitBtn);

        wrap.add(box);
        return wrap;
    }

    private void handleSubmit(JTextField titleField, JTextField locationField,
                              JPanel startDatePanel, JPanel endDatePanel,
                              JPanel startTimePanel, JPanel endTimePanel,
                              JTextField capacityField, JTextField descriptionField) {
        try {
            LocalDate startDate = parseDatePanel(startDatePanel);
            LocalDate endDate = parseDatePanel(endDatePanel);
            LocalTime startTime = parseTimePanel(startTimePanel);
            LocalTime endTime = parseTimePanel(endTimePanel);
            int capacity = Integer.parseInt(capacityField.getText());

            String error = eventController.createEvent(
                    currentUser,
                    titleField.getText(),
                    locationField.getText(),
                    startDate,
                    endDate,
                    startTime,
                    endTime,
                    capacity,
                    List.of(),
                    descriptionField.getText()
            );

            if (error == null) {
                JOptionPane.showMessageDialog(this, "Event Created Successfully!");
                new OrganiserDashboardUI(currentUser, eventDatabase, joinEventDatabase, eventController, notificationController);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, error, "Input Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "All date, time, and capacity fields must be numbers.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date or time entered.", "Date/Time Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField wideInput(String label) {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(600, 35));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        f.setBorder(BorderFactory.createTitledBorder(label));
        return f;
    }

    private JTextField shortInput(String placeholder) {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(80, 35));
        f.setBorder(BorderFactory.createTitledBorder(placeholder));
        return f;
    }

    private JPanel datePanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.add(shortInput("DD")); p.add(shortInput("MM")); p.add(shortInput("YYYY"));
        return p;
    }

    private JPanel timePanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.add(shortInput("HH")); p.add(shortInput("MM"));
        return p;
    }

    private LocalDate parseDatePanel(JPanel panel) {
        int day = Integer.parseInt(((JTextField) panel.getComponent(0)).getText());
        int month = Integer.parseInt(((JTextField) panel.getComponent(1)).getText());
        int year = Integer.parseInt(((JTextField) panel.getComponent(2)).getText());
        return LocalDate.of(year, month, day);
    }

    private LocalTime parseTimePanel(JPanel panel) {
        int hour = Integer.parseInt(((JTextField) panel.getComponent(0)).getText());
        int minute = Integer.parseInt(((JTextField) panel.getComponent(1)).getText());
        return LocalTime.of(hour, minute);
    }
}