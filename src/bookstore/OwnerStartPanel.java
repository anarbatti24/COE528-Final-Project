package bookstore;

import javax.swing.*;
import java.awt.*;

/**
 * Owner start screen. Three buttons: Books, Customers, Logout.
 * Navigates to the corresponding screen or back to login.
 */
public class OwnerStartPanel extends JPanel {

    private BookStoreGUI parentGUI;

    public OwnerStartPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(80, 150, 80, 150));

        JButton booksButton = new JButton("Books");
        booksButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(booksButton);

        add(Box.createVerticalStrut(15));

        JButton customersButton = new JButton("Customers");
        customersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(customersButton);

        add(Box.createVerticalStrut(15));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logoutButton);

        booksButton.addActionListener(e -> {
            parentGUI.getOwnerBooksPanel().refreshTable();
            parentGUI.showScreen("OWNER_BOOKS");
        });

        customersButton.addActionListener(e -> {
            parentGUI.getOwnerCustomersPanel().refreshTable();
            parentGUI.showScreen("OWNER_CUSTOMERS");
        });

        logoutButton.addActionListener(e -> parentGUI.showScreen("LOGIN"));
    }
}
