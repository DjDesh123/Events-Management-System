package ui;

import events.Event;
import events.EventController;
import events.EventDatabase;
import joinEvents.JoinEventDatabase;
import user.User;
import notifications.NotificationController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDashboardUI extends JFrame {

    private final User currentUser;
    private final EventDatabase eventDatabase;
    private final JoinEventDatabase joinEventDatabase;
    private final EventController eventController;
    private final NotificationController notificationController;

    private List<Event> upcomingEvents;
    private List<Event> registeredEvents;

    private int upcomingIndex = 0;
    private int registeredIndex = 0;

    public StudentDashboardUI(User currentUser,
                              EventDatabase eventDatabase,
                              JoinEventDatabase joinEventDatabase,
                              EventController eventController,
                              NotificationController notificationController) {
        this.currentUser = currentUser;
        this.eventDatabase = eventDatabase;
        this.joinEventDatabase = joinEventDatabase;
        this.eventController = eventController;
        this.notificationController = notificationController;

        loadEvents();

        setTitle("Student Dashboard");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(sidebar(), BorderLayout.WEST);
        add(mainContent(), BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadEvents() {
        upcomingEvents = eventDatabase.getAllEvents().values().stream()
                .filter(e -> e.getEndDate().isAfter(LocalDate.now()) || e.getEndDate().isEqual(LocalDate.now()))
                .collect(Collectors.toList());

        registeredEvents = joinEventDatabase.getJoinEvents().values().stream()
                .filter(j -> j.getUserId() == currentUser.getUserId())
                .map(j -> eventDatabase.get(j.getEventId()))
                .filter(e -> e != null)
                .collect(Collectors.toList());
    }

    private JPanel sidebar() {
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(180, getHeight()));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(245,245,245));

        JButton homeBtn = sideButton("Home");
        homeBtn.addActionListener(e -> reloadDashboard());

        JButton searchBtn = sideButton("Search");
        searchBtn.addActionListener(e -> {
            new SearchUI(currentUser, eventDatabase, joinEventDatabase, eventController, notificationController);
            dispose();
        });

        JButton notificationBtn = sideButton("Notifications");
        notificationBtn.addActionListener(e -> new NotificationsUI(currentUser));

        JButton settingsBtn = sideButton("Settings");
        settingsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,"Settings coming soon!"));

        side.add(homeBtn);
        side.add(searchBtn);
        side.add(notificationBtn);
        side.add(settingsBtn);
        side.add(Box.createVerticalGlue());

        return side;
    }

    private JButton sideButton(String name) {
        JButton btn = new JButton(name);
        btn.setMaximumSize(new Dimension(180,40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(230,230,230));
        btn.setFocusPainted(false);
        return btn;
    }

    private JPanel mainContent() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main,BoxLayout.Y_AXIS));
        main.setBackground(Color.WHITE);

        main.add(eventSection("Upcoming Events", upcomingEvents,true));
        main.add(eventSection("Registered Events",registeredEvents,false));
        return main;
    }

    private JPanel eventSection(String title,List<Event> events,boolean isUpcoming) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        section.setBackground(Color.WHITE);

        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial",Font.BOLD,22));
        section.add(label,BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new GridLayout(1,3,15,15));
        cardPanel.setBackground(Color.WHITE);

        if(events.isEmpty()){
            JLabel noMessage = new JLabel("No events",SwingConstants.CENTER);
            noMessage.setFont(new Font("Arial",Font.ITALIC,16));
            cardPanel.add(noMessage);
        } else {
            int start = isUpcoming ? upcomingIndex : registeredIndex;
            for(int i=start;i<Math.min(start+3,events.size());i++){
                cardPanel.add(eventCard(events.get(i)));
            }
        }

        JPanel nav = new JPanel(new BorderLayout());
        JButton left = new JButton("<");
        JButton right = new JButton(">");

        left.addActionListener(e -> {
            if(isUpcoming && upcomingIndex>0) upcomingIndex--;
            else if(!isUpcoming && registeredIndex>0) registeredIndex--;
            reloadDashboard();
        });

        right.addActionListener(e -> {
            if(isUpcoming && upcomingIndex < events.size()-3) upcomingIndex++;
            else if(!isUpcoming && registeredIndex< events.size()-3) registeredIndex++;
            reloadDashboard();
        });

        nav.add(left,BorderLayout.WEST);
        nav.add(cardPanel,BorderLayout.CENTER);
        nav.add(right,BorderLayout.EAST);

        section.add(nav,BorderLayout.CENTER);
        return section;
    }

    private JPanel eventCard(Event e){
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(220,160));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(new Color(250,250,250));

        JLabel title = new JLabel(e.getEventTitle(),SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,15));
        card.add(title,BorderLayout.NORTH);

        JTextArea info = new JTextArea(
                "Start: "+e.getStartDate()+"\n"+
                        "End: "+e.getEndDate()+"\n"+
                        "Max: "+e.getMaxAttendees()
        );
        info.setEditable(false);
        info.setBackground(card.getBackground());
        card.add(info,BorderLayout.CENTER);

        JButton view = new JButton("View");
        view.addActionListener(ev -> new ViewEventDetailsUI(currentUser,e,eventDatabase,joinEventDatabase));
        card.add(view,BorderLayout.SOUTH);

        return card;
    }

    private void reloadDashboard(){
        loadEvents();
        refreshUI();
    }

    private void refreshUI(){
        getContentPane().removeAll();
        add(sidebar(),BorderLayout.WEST);
        add(mainContent(),BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}