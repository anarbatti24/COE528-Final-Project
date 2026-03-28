package bookstore;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Single JFrame that holds all screens using CardLayout.
 * Spec requires one window only — screens swap in the same window.
 * Saves data to files when the window is closed.
 */
public class BookStoreGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private LoginPanel loginPanel;
    private OwnerStartPanel ownerStartPanel;
    private OwnerBooksPanel ownerBooksPanel;
    private OwnerCustomersPanel ownerCustomersPanel;
    private CustomerStartPanel customerStartPanel;
    private CustomerCostPanel customerCostPanel;

    public BookStoreGUI() {
        setTitle("Bookstore App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        // CardLayout stacks panels and shows one at a time
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create all screen panels — each gets a reference to this frame
        // so they can call showScreen() to navigate
        loginPanel = new LoginPanel(this);
        ownerStartPanel = new OwnerStartPanel(this);
        ownerBooksPanel = new OwnerBooksPanel(this);
        ownerCustomersPanel = new OwnerCustomersPanel(this);
        customerStartPanel = new CustomerStartPanel(this);
        customerCostPanel = new CustomerCostPanel(this);

        // Register panels with string keys for switching
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(ownerStartPanel, "OWNER_START");
        mainPanel.add(ownerBooksPanel, "OWNER_BOOKS");
        mainPanel.add(ownerCustomersPanel, "OWNER_CUSTOMERS");
        mainPanel.add(customerStartPanel, "CUSTOMER_START");
        mainPanel.add(customerCostPanel, "CUSTOMER_COST");

        add(mainPanel);

        // Save data when [x] is clicked (spec requirement)
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                BookStoreApplication.saveData();
            }
        });
    }

    /** Switches the visible screen. Called by every panel's button handlers. */
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    // Getters so panels can access each other for data passing
    public CustomerStartPanel getCustomerStartPanel() { return customerStartPanel; }
    public CustomerCostPanel getCustomerCostPanel() { return customerCostPanel; }
    public OwnerBooksPanel getOwnerBooksPanel() { return ownerBooksPanel; }
    public OwnerCustomersPanel getOwnerCustomersPanel() { return ownerCustomersPanel; }
}
