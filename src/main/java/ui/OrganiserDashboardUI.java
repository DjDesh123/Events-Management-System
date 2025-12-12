package ui;

import events.Event;
import events.EventController;
import events.EventDatabase;
import joinEvents.JoinEventDatabase;
import notifications.NotificationController;
import user.User;
import user.UserController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OrganiserDashboardUI extends JFrame {

    private final User currentUser;
    private final EventDatabase eventDatabase;
    private final EventController eventController;
    private final JoinEventDatabase joinEventDatabase;
    private final NotificationController notificationController;
    private final UserController userController;

    // list of events organised by this user
    private List<Event> organisedEvents;
    // current index for pagination
    private int eventIndex = 0;

    // constructor
    public OrganiserDashboardUI(User currentUser,
                                EventDatabase eventDatabase,
                                JoinEventDatabase joinEventDatabase,
                                EventController eventController,
                                NotificationController notificationController,
                                UserController userController) {
        this.currentUser = currentUser;
        this.eventDatabase = eventDatabase;
        this.eventController = eventController;
        this.joinEventDatabase = joinEventDatabase;
        this.notificationController = notificationController;
        this.userController = userController;

        loadEvents();

        setTitle("Organiser Dashboard");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(sidebar(), BorderLayout.WEST);
        add(mainContent(), BorderLayout.CENTER);

        setVisible(true);
    }

    // load organised events from controller
    private void loadEvents() {
        organisedEvents = eventController.getUsersEvents(currentUser).stream()
                .filter(e -> !e.getEndDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    // create sidebar panel with navigation buttons
    private JPanel sidebar() {
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(180, getHeight()));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(245, 245, 245));

        JButton homeBtn = sideButton("Home");
        homeBtn.addActionListener(e -> refreshUI());

        JButton searchBtn = sideButton("Search");
        searchBtn.addActionListener(e -> {
            new SearchUI(currentUser, eventDatabase, joinEventDatabase, eventController, notificationController, userController);
            dispose();
        });

        JButton createBtn = sideButton("Create Event");
        createBtn.addActionListener(e -> new CreateEventUI(
                currentUser,
                eventDatabase,
                joinEventDatabase,
                eventController,
                notificationController,
                userController
        ));

        JButton settingsBtn = sideButton("Settings");
        settingsBtn.addActionListener(e -> new SettingsUI(this,userController));

        side.add(homeBtn);
        side.add(searchBtn);
        side.add(createBtn);
        side.add(settingsBtn);
        side.add(Box.createVerticalGlue());

        return side;
    }

    // helper to create a sidebar button
    private JButton sideButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(230, 230, 230));
        btn.setFocusPainted(false);
        return btn;
    }

    // create main content panel displaying organised events
    private JPanel mainContent() {
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);

        if (organisedEvents.isEmpty()) {
            JLabel noEvents = new JLabel("no events found", SwingConstants.CENTER);
            noEvents.setFont(new Font("Arial", Font.ITALIC, 16));
            main.add(noEvents, BorderLayout.CENTER);
        } else {
            JPanel cardPanel = new JPanel(new GridLayout(1, 3, 15, 15));
            cardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            cardPanel.setBackground(Color.WHITE);

            int start = eventIndex;
            for (int i = start; i < Math.min(start + 3, organisedEvents.size()); i++) {
                cardPanel.add(eventCard(organisedEvents.get(i)));
            }

            JPanel navPanel = new JPanel(new BorderLayout());
            JButton left = new JButton("<");
            JButton right = new JButton(">");

            left.addActionListener(e -> {
                if (eventIndex > 0) eventIndex--;
                refreshUI();
            });

            right.addActionListener(e -> {
                if (eventIndex < organisedEvents.size() - 3) eventIndex++;
                refreshUI();
            });

            navPanel.add(left, BorderLayout.WEST);
            navPanel.add(cardPanel, BorderLayout.CENTER);
            navPanel.add(right, BorderLayout.EAST);

            main.add(navPanel, BorderLayout.CENTER);
        }

        return main;
    }

    // create individual event card with click listener
    private JPanel eventCard(Event e) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(200, 140));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(new Color(250, 250, 250));

        JLabel title = new JLabel(e.getEventTitle(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(title, BorderLayout.CENTER);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new ViewDetailsUI(
                        currentUser,
                        e,
                        eventDatabase,
                        eventController,
                        joinEventDatabase,
                        notificationController
                );
            }
        });

        return card;
    }

    // refresh entire ui
    private void refreshUI() {
        getContentPane().removeAll();
        add(sidebar(), BorderLayout.WEST);
        add(mainContent(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
