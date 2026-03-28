package bookstore;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main application window — the single JFrame that holds all screens.
 *
 * Spec requirement:
 * "The app should be a single-window GUI, that is, ONLY one window should be
 * available to the user of the app. If a user clicks a button to obtain a new
 * screen, the last screen should be replaced by the new screen, in the SAME
 * window (i.e. multiple windows MUST NOT get opened while using the app)."
 *
 * This is achieved using CardLayout, which stacks all screen panels on top of
 * each other and shows only one at a time. Each panel calls showScreen() to
 * switch to a different screen, passing the string key of the target panel.
 *
 * Screen keys:
 * - "LOGIN"            -> LoginPanel          (login screen)
 * - "OWNER_START"      -> OwnerStartPanel     (owner-start-screen)
 * - "OWNER_BOOKS"      -> OwnerBooksPanel     (owner-books-screen)
 * - "OWNER_CUSTOMERS"  -> OwnerCustomersPanel (owner-customers-screen)
 * - "CUSTOMER_START"   -> CustomerStartPanel  (customer-start-screen)
 * - "CUSTOMER_COST"    -> CustomerCostPanel   (customer-cost-screen)
 *
 * Spec requirement for window close:
 * "Whenever a user clicks the [x] button at the top-right of the app window,
 * all the relevant data should be written in books.txt and customers.txt."
 * This is handled by the WindowListener which calls saveData() on close.
 */
public class BookStoreGUI extends JFrame {

    private CardLayout cardLayout;  // layout manager that handles screen switching
    private JPanel mainPanel;       // container panel that holds all screen panels

    // References to each screen panel — needed so panels can access each other
    // (e.g. LoginPanel needs to call customerStartPanel.setCustomer())
    private LoginPanel loginPanel;
    private OwnerStartPanel ownerStartPanel;
    private OwnerBooksPanel ownerBooksPanel;
    private OwnerCustomersPanel ownerCustomersPanel;
    private CustomerStartPanel customerStartPanel;
    private CustomerCostPanel customerCostPanel;

    /**
     * Constructs the main application window.
     * Creates all six screen panels, adds them to the CardLayout,
     * and sets up the window close handler for saving data.
     */
    public BookStoreGUI() {
        setTitle("Bookstore App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);  // center the window on screen

        // CardLayout allows switching between panels using string keys
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create all screen panels, passing 'this' so each panel
        // can call showScreen() and access other panels via getters
        loginPanel = new LoginPanel(this);
        ownerStartPanel = new OwnerStartPanel(this);
        ownerBooksPanel = new OwnerBooksPanel(this);
        ownerCustomersPanel = new OwnerCustomersPanel(this);
        customerStartPanel = new CustomerStartPanel(this);
        customerCostPanel = new CustomerCostPanel(this);

        // Register each panel with a string key for CardLayout switching
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(ownerStartPanel, "OWNER_START");
        mainPanel.add(ownerBooksPanel, "OWNER_BOOKS");
        mainPanel.add(ownerCustomersPanel, "OWNER_CUSTOMERS");
        mainPanel.add(customerStartPanel, "CUSTOMER_START");
        mainPanel.add(customerCostPanel, "CUSTOMER_COST");

        // The first panel added ("LOGIN") is shown by default
        add(mainPanel);

        // Save all data when the user clicks the [x] close button (spec requirement)
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                BookStoreApplication.saveData();
            }
        });
    }

    /**
     * Switches the visible screen to the panel with the given key.
     * Called by every panel's button handlers to navigate between screens.
     *
     * Example: parentGUI.showScreen("OWNER_START") switches to the owner start screen.
     *
     * @param screenName the string key of the panel to show (e.g. "LOGIN", "OWNER_BOOKS")
     */
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    // --- Getters for panels ---
    // These allow panels to access each other for data passing.
    // For example, LoginPanel calls getCustomerStartPanel().setCustomer(c)
    // to pass the logged-in customer before switching to that screen.

    /** @return the CustomerStartPanel instance */
    public CustomerStartPanel getCustomerStartPanel() {
        return customerStartPanel;
    }

    /** @return the CustomerCostPanel instance */
    public CustomerCostPanel getCustomerCostPanel() {
        return customerCostPanel;
    }

    /** @return the OwnerBooksPanel instance */
    public OwnerBooksPanel getOwnerBooksPanel() {
        return ownerBooksPanel;
    }

    /** @return the OwnerCustomersPanel instance */
    public OwnerCustomersPanel getOwnerCustomersPanel() {
        return ownerCustomersPanel;
    }
}
