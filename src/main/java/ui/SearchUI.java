package ui;

import events.Event;
import events.EventController;
import events.EventDatabase;
import joinEvents.JoinEventDatabase;
import notifications.NotificationController;
import user.User;
import user.UserController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class SearchUI extends JFrame {

    private final User currentUser;
    private final EventDatabase eventDatabase;
    private final JoinEventDatabase joinEventDatabase;
    private final EventController eventController;
    private final NotificationController notificationController;
    private final UserController userController;

    private JPanel eventsPanel; // Panel to hold event cards
    private JTextField searchField;
    private JComboBox<String> filterBox;

    // Constructor sets up UI
    public SearchUI(User currentUser,
                    EventDatabase eventDatabase,
                    JoinEventDatabase joinEventDatabase,
                    EventController eventController,
                    NotificationController notificationController, UserController userController) {

        this.currentUser = currentUser;
        this.eventDatabase = eventDatabase;
        this.joinEventDatabase = joinEventDatabase;
        this.eventController = eventController;
        this.notificationController = notificationController;
        this.userController = userController;

        setTitle("Search Events");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST); // add navigation
        add(mainPanel(), BorderLayout.CENTER);  // add main search panel

        updateEventList(); // show events initially
        setVisible(true);
    }

    // Main panel  and events
    private JPanel mainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { updateEventList(); }
            @Override public void removeUpdate(DocumentEvent e) { updateEventList(); }
            @Override public void changedUpdate(DocumentEvent e) { updateEventList(); }
        });

        filterBox = new JComboBox<>(new String[]{"All", "Upcoming", "Past"});
        filterBox.addActionListener(e -> updateEventList());

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Filter:"));
        topPanel.add(filterBox);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    // Sidebar buttons
    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(180, getHeight()));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(245, 245, 245));

        JButton homeBtn = sideButton("Home");
        homeBtn.addActionListener(e -> {
            if (currentUser.getAccountType() == User.accountType.STUDENT)
                new StudentDashboardUI(currentUser, eventDatabase, joinEventDatabase, eventController, notificationController,userController);
            else
                new OrganiserDashboardUI(currentUser, eventDatabase, joinEventDatabase, eventController, notificationController,userController);
            dispose();
        });

        JButton searchBtn = sideButton("Search");
        searchBtn.setEnabled(false);

        JButton settingsBtn = sideButton("Settings");
        settingsBtn.addActionListener(e ->
                new SettingsUI(this,userController));

        side.add(homeBtn);
        side.add(searchBtn);
        side.add(settingsBtn);

        if (currentUser.getAccountType() == User.accountType.ORGANISER) {
            JButton createBtn = sideButton("Create Event");
            createBtn.addActionListener(e ->
                    new CreateEventUI(currentUser, eventDatabase, joinEventDatabase, eventController, notificationController,userController));
            side.add(createBtn);
        }

        side.add(Box.createVerticalGlue());
        return side;
    }

    //helper for sidebar buttons
    private JButton sideButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(230, 230, 230));
        btn.setFocusPainted(false);
        return btn;
    }

    // refresh event list based on search/filter
    private void updateEventList() {
        String query = searchField.getText().trim().toLowerCase();
        String filter = (String) filterBox.getSelectedItem();

        List<Event> filtered = eventDatabase.getAllEvents().values().stream()
                .filter(e -> e.getEventTitle().toLowerCase().contains(query))
                .filter(e -> {
                    if ("Upcoming".equals(filter))
                        return !e.getEndDate().isBefore(java.time.LocalDate.now());
                    else if ("Past".equals(filter))
                        return e.getEndDate().isBefore(java.time.LocalDate.now());
                    return true;
                })
                .collect(Collectors.toList());

        eventsPanel.removeAll();

        if (filtered.isEmpty()) {
            JLabel noEvents = new JLabel("No events found");
            noEvents.setFont(new Font("Arial", Font.ITALIC, 16));
            noEvents.setAlignmentX(Component.CENTER_ALIGNMENT);
            eventsPanel.add(Box.createVerticalStrut(20));
            eventsPanel.add(noEvents);
        } else {
            for (Event e : filtered) {
                eventsPanel.add(eventCard(e));
                eventsPanel.add(Box.createVerticalStrut(10));
            }
        }

        eventsPanel.revalidate();
        eventsPanel.repaint();
    }

    //create card for single event
    private JPanel eventCard(Event e) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(new Color(250, 250, 250));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel title = new JLabel(e.getEventTitle());
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton viewBtn = new JButton("View");
        viewBtn.addActionListener(a ->
                new ViewEventDetailsUI(currentUser, e, eventDatabase, joinEventDatabase));

        card.add(title, BorderLayout.CENTER);
        card.add(viewBtn, BorderLayout.EAST);

        return card;
    }
}
