package bookstore;

import javax.swing.*;
import java.awt.*;

/**
 * Owner start screen — shown after the owner logs in successfully.
 *
 * Spec requirement:
 * "When the owner enters her username and password, and clicks button [login],
 * a new screen replaces the login-screen. We call this new screen
 * owner-start-screen. The owner-start-screen has three buttons:
 * [Books], [Customers], [Logout]."
 *
 * Navigation:
 * - [Books]     -> OwnerBooksPanel     (owner-books-screen) — refreshes the book table first
 * - [Customers] -> OwnerCustomersPanel (owner-customers-screen) — refreshes the customer table first
 * - [Logout]    -> LoginPanel          (login-screen)
 */
public class OwnerStartPanel extends JPanel {

    private BookStoreGUI parentGUI;  // reference to the main frame for screen switching

    /**
     * Constructs the owner start panel with three navigation buttons.
     *
     * @param parent the main BookStoreGUI frame
     */
    public OwnerStartPanel(BookStoreGUI parent) {
        this.parentGUI = parent;

        // Stack buttons vertically using BoxLayout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(80, 150, 80, 150));

        // --- Books button ---
        JButton booksButton = new JButton("Books");
        booksButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(booksButton);

        add(Box.createVerticalStrut(15)); // spacer

        // --- Customers button ---
        JButton customersButton = new JButton("Customers");
        customersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(customersButton);

        add(Box.createVerticalStrut(15)); // spacer

        // --- Logout button ---
        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logoutButton);

        // --- Button actions ---

        // Books: refresh the table to reflect any changes, then switch screen
        booksButton.addActionListener(e -> {
            parentGUI.getOwnerBooksPanel().refreshTable();
            parentGUI.showScreen("OWNER_BOOKS");
        });

        // Customers: refresh the table to reflect any changes, then switch screen
        customersButton.addActionListener(e -> {
            parentGUI.getOwnerCustomersPanel().refreshTable();
            parentGUI.showScreen("OWNER_CUSTOMERS");
        });

        // Logout: return to the login screen (spec: "When the owner clicks
        // the button [Logout], she should be taken back to the login-screen.")
        logoutButton.addActionListener(e -> {
            parentGUI.showScreen("LOGIN");
        });
    }
}
