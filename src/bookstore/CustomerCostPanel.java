package bookstore;

import javax.swing.*;
import java.awt.*;

/**
 * Customer cost screen. Shown after a purchase.
 * Top: "Total Cost: TC"
 * Middle: "Points: P, Status: S"
 * Bottom: Logout button
 */
public class CustomerCostPanel extends JPanel {

    private BookStoreGUI parentGUI;
    private JLabel costLabel;
    private JLabel pointsLabel;

    public CustomerCostPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        // Total cost label
        costLabel = new JLabel("Total Cost: 0");
        gbc.gridy = 0;
        add(costLabel, gbc);

        // Points and status label
        pointsLabel = new JLabel("Points: 0, Status: Silver");
        gbc.gridy = 1;
        add(pointsLabel, gbc);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        gbc.gridy = 2;
        add(logoutButton, gbc);

        logoutButton.addActionListener(e -> {
            parentGUI.showScreen("LOGIN");
        });
    }

    /**
     * Sets the data to display after a transaction.
     * Called from CustomerStartPanel right before switching here.
     *
     * @param c    the customer who just bought
     * @param cost the total cost returned by buyBook() or redeemAndBuy()
     */
    public void setData(Customer c, double cost) {
        costLabel.setText("Total Cost: " + cost);
        pointsLabel.setText("Points: " + c.getPoints() + ", Status: " + c.getStatusName());
    }
}
