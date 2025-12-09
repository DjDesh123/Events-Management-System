package ui;

import notifications.NotificationController;
import notifications.Notifications;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationsUI extends JFrame {

    private final NotificationController controller = new NotificationController();
    private final User user;

    public NotificationsUI(User user) {
        this.user = user;

        setTitle("Notifications");
        setSize(500,500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        loadNotifications();

        setVisible(true);
    }

    private void loadNotifications() {
        // Correctly use getUserId() to fetch notifications
        List<Notifications> list = controller.getNotificationsForUser(user.getUserId());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        if(list.isEmpty()){
            JLabel none = new JLabel("No notifications");
            none.setFont(new Font("Arial",Font.ITALIC,16));
            none.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(none);
        } else {
            for(Notifications n:list){
                JPanel box = new JPanel(new BorderLayout());
                box.setBorder(BorderFactory.createLineBorder(Color.gray));
                box.add(new JLabel("Message: "+n.getMessage()),BorderLayout.CENTER);

                JButton del = new JButton("Dismiss");
                del.addActionListener(e -> {
                    controller.deleteNotification(n.getNotId());
                    dispose();
                    new NotificationsUI(user);
                });

                box.add(del,BorderLayout.EAST);
                panel.add(box);
            }
        }

        JScrollPane scroll = new JScrollPane(panel);
        add(scroll,BorderLayout.CENTER);
    }
}