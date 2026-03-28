package bookstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Owner customers screen — allows the owner to manage registered customers.
 *
 * Spec requirement:
 * "When the owner clicks the button [Customers], she sees a screen having three parts:
 *
 * Top part: A table with three columns — Username, Password, Points. They refer
 * to a customer's username, password, and the points the customer has earned.
 *
 * Middle part: Two textfields (Username and Password) and one button [Add].
 * When the owner enters a customer's username and password and clicks [Add],
 * a new row gets entered in the table. The Points for the newly added row
 * should be 0 (initially the customer has 0 points).
 *
 * Bottom part: A [Delete] button and a [Back] button. When the owner selects
 * a row and clicks [Delete], the customer is removed. If the owner clicks
 * [Back], she is taken back to the owner-start-screen."
 *
 * Data synchronization:
 * - The JTable's DefaultTableModel is the visual representation.
 * - BookStoreApplication.customers is the actual data.
 * - Both are updated in sync on every add/delete operation.
 * - refreshTable() rebuilds the table from the customers ArrayList.
 *
 * Layout: BorderLayout with table in CENTER, add fields at NORTH, buttons at SOUTH.
 */
public class OwnerCustomersPanel extends JPanel {

    private BookStoreGUI parentGUI;       // reference to the main frame for screen switching
    private DefaultTableModel tableModel; // table model backing the JTable
    private JTable customerTable;         // the visual table showing customers
    private JTextField usernameField;     // text field for entering customer username
    private JTextField passwordField;     // text field for entering customer password

    /**
     * Constructs the owner customers panel with the customer table,
     * add fields, and delete/back buttons.
     *
     * @param parent the main BookStoreGUI frame
     */
    public OwnerCustomersPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Top: Customer table ---
        // Three columns as specified: Username, Password, Points.
        // Cells are not editable — the owner can only add or delete customers.
        String[] columns = {"Username", "Password", "Points"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;  // no inline editing allowed
            }
        };
        customerTable = new JTable(tableModel);
        add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // --- Middle (placed at NORTH): Username field, Password field, Add button ---
        // --- Middle: Username field, Password field, Add button ---
        // Spec: "The middle part contains two textfields Username and Password; one button [Add]."
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(12);
        addPanel.add(usernameField);
        addPanel.add(new JLabel("Password:"));
        passwordField = new JTextField(12);
        addPanel.add(passwordField);
        JButton addButton = new JButton("Add");
        addPanel.add(addButton);

        // --- Bottom: Delete and Back buttons ---
        // Spec: "The bottom part contains a [Delete] button and a [Back] button."
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        // Combine middle and bottom into one panel below the table
        // This keeps the table on top (CENTER expands to fill space)
        // and the add fields + buttons below it
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(addPanel);
        southPanel.add(buttonPanel);
        add(southPanel, BorderLayout.SOUTH);

        // --- Add button action ---
        // Creates a new Customer with 0 points (spec: "initially the customer has 0 points"),
        // adds them to the customers ArrayList AND the table.
        addButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.");
                return;
            }

            // New customers start with 0 points and Silver status
            // (Silver is assigned automatically by the Customer constructor when points < 1000)
            Customer c = new Customer(username, password, 0);
            Owner.addCustomer(c);                                      // add to data
            tableModel.addRow(new Object[]{username, password, 0});    // add to visual table
            usernameField.setText("");                                  // clear input fields
            passwordField.setText("");
        });

        // --- Delete button action ---
        // Removes the selected customer from both the customers ArrayList and the table.
        deleteButton.addActionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row >= 0) {
                // Get the Customer object from the list at the same index as the table row
                Customer toDelete = BookStoreApplication.customers.get(row);
                Owner.deleteCustomer(toDelete);  // remove from data
                tableModel.removeRow(row);       // remove from visual table
            } else {
                JOptionPane.showMessageDialog(this, "Select a customer to delete.");
            }
        });

        // --- Back button action ---
        // Returns to the owner-start-screen (spec requirement)
        backButton.addActionListener(e -> {
            parentGUI.showScreen("OWNER_START");
        });
    }

    /**
     * Rebuilds the table from the customers ArrayList.
     * Called every time the user navigates to this screen (from OwnerStartPanel)
     * to ensure the table reflects the current state of the customer list.
     */
    public void refreshTable() {
        tableModel.setRowCount(0);  // clear all existing rows
        for (Customer c : BookStoreApplication.customers) {
            tableModel.addRow(new Object[]{c.getUsername(), c.getPassword(), c.getPoints()});
        }
    }
}