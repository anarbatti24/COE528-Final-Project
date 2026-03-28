package bookstore;

import javax.swing.*;
import java.awt.*;

/**
 * Owner start screen. Three buttons: Books, Customers, Logout.
 */
public class OwnerStartPanel extends JPanel {

    private BookStoreGUI parentGUI;

    public OwnerStartPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton booksButton = new JButton("Books");
        gbc.gridy = 0;
        add(booksButton, gbc);

        JButton customersButton = new JButton("Customers");
        gbc.gridy = 1;
        add(customersButton, gbc);

        JButton logoutButton = new JButton("Logout");
        gbc.gridy = 2;
        add(logoutButton, gbc);

        // Actions
        booksButton.addActionListener(e -> {
            parentGUI.getOwnerBooksPanel().refreshTable();
            parentGUI.showScreen("OWNER_BOOKS");
        });

        customersButton.addActionListener(e -> {
            parentGUI.getOwnerCustomersPanel().refreshTable();
            parentGUI.showScreen("OWNER_CUSTOMERS");
        });

        logoutButton.addActionListener(e -> {
            parentGUI.showScreen("LOGIN");
        });
    }
}
