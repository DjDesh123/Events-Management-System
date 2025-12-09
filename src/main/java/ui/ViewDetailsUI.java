package ui;

import events.*;
import events.Event;
import joinEvents.JoinEventDatabase;
import notifications.NotificationController;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ViewDetailsUI extends JFrame {

    private final User currentUser;
    private final Event event;
    private final EventDatabase eventDatabase;
    private final EventController eventController;
    private final JoinEventDatabase joinEventDatabase;
    private final NotificationController notificationController;

    public ViewDetailsUI(User currentUser, Event event, EventDatabase eventDatabase,
                         EventController eventController, JoinEventDatabase joinEventDatabase,
                         NotificationController notificationController) {
        this.currentUser = currentUser;
        this.event = event;
        this.eventDatabase = eventDatabase;
        this.eventController = eventController;
        this.joinEventDatabase = joinEventDatabase;
        this.notificationController = notificationController;

        setTitle("View Event Details");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(mainContent(), BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel mainContent() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(Color.WHITE);
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(250, 250, 250));
        box.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // TITLE
        JLabel titleLabel = new JLabel("View / Edit Event", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(titleLabel);
        box.add(Box.createVerticalStrut(25));

        JTextField titleField = wideInput("Event Title"); titleField.setText(event.getEventTitle());
        JTextField locationField = wideInput("Location"); locationField.setText(event.getLocation());
        box.add(titleField); box.add(Box.createVerticalStrut(15)); box.add(locationField);
        box.add(Box.createVerticalStrut(15));

        // Dates
        JPanel startDatePanel = datePanel(event.getStartDate());
        JPanel endDatePanel = datePanel(event.getEndDate());
        box.add(new JLabel("Start Date")); box.add(startDatePanel);
        box.add(Box.createVerticalStrut(10));
        box.add(new JLabel("End Date")); box.add(endDatePanel);
        box.add(Box.createVerticalStrut(15));

        // Times
        JPanel startTimePanel = timePanel(event.getStartTime());
        JPanel endTimePanel = timePanel(event.getEndTime());
        box.add(new JLabel("Start Time")); box.add(startTimePanel);
        box.add(Box.createVerticalStrut(10));
        box.add(new JLabel("End Time")); box.add(endTimePanel);
        box.add(Box.createVerticalStrut(15));

        JTextField capacityField = wideInput("Capacity"); capacityField.setText(String.valueOf(event.getMaxAttendees()));
        JTextField descriptionField = wideInput("Description"); descriptionField.setText(event.getDescription());
        box.add(capacityField); box.add(Box.createVerticalStrut(10)); box.add(descriptionField);
        box.add(Box.createVerticalStrut(25));

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> saveEvent(titleField, locationField, startDatePanel, endDatePanel,
                startTimePanel, endTimePanel, capacityField, descriptionField));

        JButton deleteBtn = new JButton("Delete Event");
        deleteBtn.setForeground(Color.RED);
        deleteBtn.addActionListener(e -> deleteEvent());

        buttonsPanel.add(saveBtn); buttonsPanel.add(deleteBtn);
        box.add(buttonsPanel);

        wrap.add(box);
        return wrap;
    }

    private void saveEvent(JTextField titleField, JTextField locationField,
                           JPanel startDatePanel, JPanel endDatePanel,
                           JPanel startTimePanel, JPanel endTimePanel,
                           JTextField capacityField, JTextField descriptionField) {
        try {
            LocalDate startDate = LocalDate.of(
                    Integer.parseInt(((JTextField)startDatePanel.getComponent(0)).getText()),
                    Integer.parseInt(((JTextField)startDatePanel.getComponent(1)).getText()),
                    Integer.parseInt(((JTextField)startDatePanel.getComponent(2)).getText())
            );
            LocalDate endDate = LocalDate.of(
                    Integer.parseInt(((JTextField)endDatePanel.getComponent(0)).getText()),
                    Integer.parseInt(((JTextField)endDatePanel.getComponent(1)).getText()),
                    Integer.parseInt(((JTextField)endDatePanel.getComponent(2)).getText())
            );

            LocalTime startTime = LocalTime.of(
                    Integer.parseInt(((JTextField)startTimePanel.getComponent(0)).getText()),
                    Integer.parseInt(((JTextField)startTimePanel.getComponent(1)).getText())
            );

            LocalTime endTime = LocalTime.of(
                    Integer.parseInt(((JTextField)endTimePanel.getComponent(0)).getText()),
                    Integer.parseInt(((JTextField)endTimePanel.getComponent(1)).getText())
            );

            int capacity = Integer.parseInt(capacityField.getText());

            boolean success = eventController.editEvent(event.getEventId(),
                    titleField.getText(), locationField.getText(),
                    startDate, endDate, startTime, endTime,
                    capacity, descriptionField.getText());

            if (success) {
                // Notify attendees
                notificationController.notifyAttendees(event, "Event " + event.getEventTitle() + " was updated!");
                JOptionPane.showMessageDialog(this, "Event updated successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid data or capacity less than current attendees.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input format.",
                    "Format Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEvent() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this event?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            eventController.deleteEvent(event.getEventId());
            notificationController.notifyAttendees(event, "Event " + event.getEventTitle() + " was deleted!");
            JOptionPane.showMessageDialog(this, "Event deleted!");
            dispose();
        }
    }

    private JTextField wideInput(String label) {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(600, 35));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        f.setBorder(BorderFactory.createTitledBorder(label));
        return f;
    }

    private JPanel datePanel(LocalDate date) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,0));
        JTextField day = new JTextField(String.valueOf(date.getDayOfMonth()), 2);
        JTextField month = new JTextField(String.valueOf(date.getMonthValue()), 2);
        JTextField year = new JTextField(String.valueOf(date.getYear()), 4);
        p.add(day); p.add(month); p.add(year);
        return p;
    }

    private JPanel timePanel(LocalTime time) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,0));
        JTextField hour = new JTextField(String.valueOf(time.getHour()), 2);
        JTextField minute = new JTextField(String.valueOf(time.getMinute()), 2);
        p.add(hour); p.add(minute);
        return p;
    }
}
