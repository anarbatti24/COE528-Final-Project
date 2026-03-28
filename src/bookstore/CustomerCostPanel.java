package bookstore;

import javax.swing.*;
import java.awt.*;

/**
 * Customer cost screen — shown after a purchase transaction.
 *
 * Spec requirement:
 * "When the customer clicks either [Buy] or [Redeem Points and Buy],
 * a new screen replaces the customer-start-screen. We call this new screen
 * customer-cost-screen. This screen has three GUI items from top to bottom:
 *
 * Top item: The message 'Total Cost: TC'. TC is the total transaction cost.
 * There is no tax.
 *
 * Middle item: The message 'Points: P, Status: S'. P is the current number
 * of points. S is the current status (Gold or Silver).
 *
 * Bottom item: A [Logout] button. If the customer clicks [Logout], she should
 * be taken back to the login-screen."
 *
 * The data displayed here is set by CustomerStartPanel right before switching
 * to this screen, via the setData() method.
 */
public class CustomerCostPanel extends JPanel {

    private BookStoreGUI parentGUI;  // reference to the main frame for screen switching
    private JLabel costLabel;        // displays "Total Cost: TC"
    private JLabel pointsLabel;      // displays "Points: P, Status: S"

    /**
     * Constructs the customer cost panel with cost label, points/status label,
     * and logout button.
     *
     * @param parent the main BookStoreGUI frame
     */
    public CustomerCostPanel(BookStoreGUI parent) {
        this.parentGUI = parent;

        // Stack items vertically using BoxLayout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(80, 50, 80, 50));

        // --- Total cost label ---
        // Shows the transaction cost (sum of selected books, minus any redemption)
        costLabel = new JLabel("Total Cost: 0");
        costLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(costLabel);

        add(Box.createVerticalStrut(20)); // spacer

        // --- Points and status label ---
        // Shows the customer's updated point total and Gold/Silver status
        // AFTER the transaction has been processed
        pointsLabel = new JLabel("Points: 0, Status: Silver");
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(pointsLabel);

        add(Box.createVerticalStrut(20)); // spacer

        // --- Logout button ---
        // Spec: "If the customer clicks [Logout], she should be taken back
        // to the login-screen."
        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logoutButton);

        logoutButton.addActionListener(e -> {
            parentGUI.showScreen("LOGIN");
        });
    }

    /**
     * Sets the data to display after a transaction.
     * Called from CustomerStartPanel right before switching to this screen.
     *
     * @param c    the customer who just completed a purchase
     * @param cost the total transaction cost returned by buyBook() or redeemAndBuy()
     *             (for [Redeem Points and Buy], this is the cost AFTER redemption)
     */
    public void setData(Customer c, double cost) {
        costLabel.setText("Total Cost: " + cost);
        pointsLabel.setText("Points: " + c.getPoints() + ", Status: " + c.getStatusName());
    }
}
