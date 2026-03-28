package bookstore;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main application window. Single JFrame using CardLayout
 * to swap between screens as required by the spec.
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

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        ownerStartPanel = new OwnerStartPanel(this);
        ownerBooksPanel = new OwnerBooksPanel(this);
        ownerCustomersPanel = new OwnerCustomersPanel(this);
        customerStartPanel = new CustomerStartPanel(this);
        customerCostPanel = new CustomerCostPanel(this);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(ownerStartPanel, "OWNER_START");
        mainPanel.add(ownerBooksPanel, "OWNER_BOOKS");
        mainPanel.add(ownerCustomersPanel, "OWNER_CUSTOMERS");
        mainPanel.add(customerStartPanel, "CUSTOMER_START");
        mainPanel.add(customerCostPanel, "CUSTOMER_COST");

        add(mainPanel);

        // Save data when window is closed (spec requirement)
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                BookStoreApplication.saveData();
            }
        });
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public CustomerStartPanel getCustomerStartPanel() {
        return customerStartPanel;
    }

    public CustomerCostPanel getCustomerCostPanel() {
        return customerCostPanel;
    }

    public OwnerBooksPanel getOwnerBooksPanel() {
        return ownerBooksPanel;
    }

    public OwnerCustomersPanel getOwnerCustomersPanel() {
        return ownerCustomersPanel;
    }
}
