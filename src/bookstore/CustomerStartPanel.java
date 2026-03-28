package bookstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Customer start screen — shown after a registered customer logs in.
 *
 * Spec requirement:
 * "When a customer enters her username and password, and clicks [login],
 * a new screen replaces the login-screen. We call this new screen
 * customer-start-screen. The customer-start-screen has three parts:
 *
 * Top part: Shows the message 'Welcome [Name]. You have P points.
 * Your status is S.' P is the number of points. S is Gold or Silver.
 * A customer with points less than 1000 has Silver status.
 * A customer with points 1000 or above has Gold status.
 *
 * Middle part: A table with three columns — Book Name, Book Price, Select.
 * The Select column has a checkbox that can be checked or unchecked.
 *
 * Bottom part: Three buttons — [Buy], [Redeem Points and Buy], [Logout].
 * If the customer clicks [Logout], she is taken back to the login-screen."
 *
 * Purchase flow:
 * 1. Customer checks one or more book checkboxes in the Select column.
 * 2. Customer clicks either [Buy] or [Redeem Points and Buy].
 * 3. The corresponding method on Customer is called (buyBook or redeemAndBuy).
 * 4. The result (total cost) and updated customer data are passed to
 *    CustomerCostPanel, and the screen switches to customer-cost-screen.
 *
 * Layout: BorderLayout with welcome label at NORTH, book table in CENTER,
 * buttons at SOUTH.
 */
public class CustomerStartPanel extends JPanel {

    private BookStoreGUI parentGUI;       // reference to the main frame for screen switching
    private Customer currentCustomer;     // the customer who is currently logged in
    private DefaultTableModel tableModel; // table model backing the JTable
    private JTable bookTable;             // the visual table showing available books
    private JLabel welcomeLabel;          // dynamic label showing name, points, and status

    /**
     * Constructs the customer start panel with the welcome label,
     * book table with checkboxes, and buy/redeem/logout buttons.
     *
     * @param parent the main BookStoreGUI frame
     */
    public CustomerStartPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Top: Welcome label ---
        // This is updated dynamically every time setCustomer() or refresh() is called.
        // It displays the customer's name, current points, and Gold/Silver status.
        welcomeLabel = new JLabel("Welcome");
        add(welcomeLabel, BorderLayout.NORTH);

        // --- Middle: Book table with checkbox column ---
        // Three columns: Book Name, Book Price, Select (checkbox).
        // The custom DefaultTableModel overrides two methods:
        // - getColumnClass(): returns Boolean.class for column 2, which makes Swing
        //   automatically render that column as checkboxes instead of text.
        // - isCellEditable(): returns true only for column 2, so the customer can
        //   click checkboxes but cannot edit book names or prices.
        tableModel = new DefaultTableModel(
                new String[]{"Book Name", "Book Price", "Select"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) return Boolean.class;  // renders as checkbox
                if (column == 1) return Double.class;    // proper numeric sorting
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 2;  // only the Select checkboxes are clickable
            }
        };
        bookTable = new JTable(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // --- Bottom: Buy, Redeem Points and Buy, Logout buttons ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton buyButton = new JButton("Buy");
        JButton redeemButton = new JButton("Redeem Points and Buy");
        JButton logoutButton = new JButton("Logout");
        bottomPanel.add(buyButton);
        bottomPanel.add(redeemButton);
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Buy button action ---
        // Spec: "If the customer wants to buy one or more books, she should click
        // the checkbox(es) and click [Buy]."
        // Calls Customer.buyBook() which calculates cost and earns points WITHOUT
        // redeeming any existing points.
        buyButton.addActionListener(e -> {
            ArrayList<Book> selected = getSelectedBooks();
            if (selected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select at least one book.");
                return;
            }
            // buyBook() returns the total cost and updates points/status internally
            double cost = currentCustomer.buyBook(selected);
            // Pass the results to the cost screen and switch to it
            parentGUI.getCustomerCostPanel().setData(currentCustomer, cost);
            parentGUI.showScreen("CUSTOMER_COST");
        });

        // --- Redeem Points and Buy button action ---
        // Spec: "When the customer clicks [Redeem Points and Buy], the TC should be
        // the transaction cost after the relevant amount of existing points has been
        // redeemed. When possible, ALL accumulated points must be redeemed."
        // Calls Customer.redeemAndBuy() which redeems points first, then calculates
        // the remaining cost and earns new points on that remaining amount.
        redeemButton.addActionListener(e -> {
            ArrayList<Book> selected = getSelectedBooks();
            if (selected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select at least one book.");
                return;
            }
            // redeemAndBuy() returns the cost after redemption and updates points/status
            double cost = currentCustomer.redeemAndBuy(selected);
            // Pass the results to the cost screen and switch to it
            parentGUI.getCustomerCostPanel().setData(currentCustomer, cost);
            parentGUI.showScreen("CUSTOMER_COST");
        });

        // --- Logout button action ---
        // Spec: "If the customer clicks [Logout], she should be taken back to
        // the login-screen."
        logoutButton.addActionListener(e -> {
            parentGUI.showScreen("LOGIN");
        });
    }

    /**
     * Sets the currently logged-in customer and refreshes the screen.
     * Called from LoginPanel right before switching to this screen.
     *
     * This is the key method that connects the login flow to this panel:
     * LoginPanel finds the Customer object, then calls this method to
     * tell CustomerStartPanel which customer is active.
     *
     * @param c the Customer who just logged in
     */
    public void setCustomer(Customer c) {
        this.currentCustomer = c;
        refresh();
    }

    /**
     * Updates the welcome label text and repopulates the book table
     * with fresh data from the inventory.
     *
     * Called every time setCustomer() is called (i.e. on every login).
     * The welcome message is rebuilt using the customer's current username,
     * points, and status name to match the spec format:
     * "Welcome [Name]. You have P points. Your status is S"
     */
    public void refresh() {
        // Update the welcome message with current customer data
        welcomeLabel.setText("Welcome " + currentCustomer.getUsername()
                + ". You have " + currentCustomer.getPoints()
                + " points. Your status is " + currentCustomer.getStatusName());

        // Clear and repopulate the book table from the inventory.
        // Each row gets: book name, book price, and an unchecked checkbox (false).
        tableModel.setRowCount(0);
        for (Book b : BookStoreApplication.inventory) {
            tableModel.addRow(new Object[]{b.getName(), b.getPrice(), false});
        }
    }

    /**
     * Collects all books whose checkbox is checked in the Select column.
     *
     * Loops through every row of the table model, checks the Boolean value
     * in column 2 (the checkbox column), and if true, adds the corresponding
     * Book from BookStoreApplication.inventory to the result list.
     *
     * This works because the table rows and inventory ArrayList are populated
     * in the same order by refresh(), so row index i in the table corresponds
     * to inventory.get(i).
     *
     * @return ArrayList of selected Book objects
     */
    private ArrayList<Book> getSelectedBooks() {
        ArrayList<Book> selected = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean checked = (Boolean) tableModel.getValueAt(i, 2);
            if (checked) {
                selected.add(BookStoreApplication.inventory.get(i));
            }
        }
        return selected;
    }
}
