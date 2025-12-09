package ui;

import events.Event;
import events.EventDatabase;
import joinEvents.JoinEventController;
import joinEvents.JoinEventDatabase;
import user.User;

import javax.swing.*;
import java.awt.*;

public class ViewEventDetailsUI extends JFrame {

    public ViewEventDetailsUI(User currentUser, Event event, EventDatabase eventDatabase, JoinEventDatabase joinEventDatabase) {

        JoinEventController joinController = new JoinEventController(eventDatabase, joinEventDatabase);

        setTitle("Event Details");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel(event.getEventTitle(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(title);
        box.add(Box.createVerticalStrut(20));

        add(label("Location: " + event.getLocation()), box);
        add(label("Start: " + event.getStartDate() + "  " + event.getStartTime()), box);
        add(label("End: " + event.getEndDate() + "  " + event.getEndTime()), box);
        add(label("Capacity: " + event.getMaxAttendees()), box);
        add(label("Description: " + event.getDescription()), box);

        box.add(Box.createVerticalStrut(30));

        JPanel btnPanel = new JPanel(new FlowLayout());

        JButton joinBtn = new JButton("+ Join");
        JButton leaveBtn = new JButton("- Leave");

        boolean alreadyJoined = joinEventDatabase.getJoinEvents().values().stream()
                .anyMatch(j -> j.getEventId() == event.getEventId() &&
                        j.getUserId() == currentUser.getUserId());

        joinBtn.setEnabled(!alreadyJoined);
        leaveBtn.setEnabled(alreadyJoined);

        joinBtn.addActionListener(e -> {
            if (joinController.joinEvent(event.getEventId())) {
                JOptionPane.showMessageDialog(this, "Joined ✔");
                joinBtn.setEnabled(false);
                leaveBtn.setEnabled(true);
            }
        });

        leaveBtn.addActionListener(e -> {
            if (joinController.leaveEvent(event.getEventId())) {
                JOptionPane.showMessageDialog(this, "Left Event ❌");
                joinBtn.setEnabled(true);
                leaveBtn.setEnabled(false);
            }
        });

        btnPanel.add(joinBtn);
        btnPanel.add(leaveBtn);
        box.add(btnPanel);

        add(box, BorderLayout.CENTER);
        setVisible(true);
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Arial", Font.PLAIN, 16));
        l.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return l;
    }

    private void add(JComponent c, JPanel p) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(c);
    }
}
