package bookstore;

import javax.swing.*;
import java.awt.*;

/**
 * Customer cost screen. Shown after a purchase.
 * Displays total cost, updated points/status, and a Logout button.
 */
public class CustomerCostPanel extends JPanel {

    private BookStoreGUI parentGUI;
    private JLabel costLabel;
    private JLabel pointsLabel;

    public CustomerCostPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(80, 50, 80, 50));

        costLabel = new JLabel("Total Cost: 0");
        costLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(costLabel);

        add(Box.createVerticalStrut(20));

        pointsLabel = new JLabel("Points: 0, Status: Silver");
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(pointsLabel);

        add(Box.createVerticalStrut(20));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logoutButton);

        logoutButton.addActionListener(e -> parentGUI.showScreen("LOGIN"));
    }

    /** Sets the transaction results. Called from CustomerStartPanel before switching here. */
    public void setData(Customer c, double cost) {
        costLabel.setText("Total Cost: " + cost);
        pointsLabel.setText("Points: " + c.getPoints() + ", Status: " + c.getStatusName());
    }
}
