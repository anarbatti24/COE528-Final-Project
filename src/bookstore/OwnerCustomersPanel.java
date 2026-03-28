package bookstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Owner customers screen.
 * Top: table with Username, Password, Points columns.
 * Middle: Username field, Password field, Add button.
 * Bottom: Delete button, Back button.
 */
public class OwnerCustomersPanel extends JPanel {

    private BookStoreGUI parentGUI;
    private DefaultTableModel tableModel;
    private JTable customerTable;
    private JTextField usernameField;
    private JTextField passwordField;

    public OwnerCustomersPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Top: Customer table ---
        String[] columns = {"Username", "Password", "Points"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // --- Middle: Username, Password, Add ---
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(12);
        addPanel.add(usernameField);
        addPanel.add(new JLabel("Password:"));
        passwordField = new JTextField(12);
        addPanel.add(passwordField);
        JButton addButton = new JButton("Add");
        addPanel.add(addButton);
        add(addPanel, BorderLayout.NORTH);

        // --- Bottom: Delete, Back ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");
        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Actions ---

        addButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.");
                return;
            }

            // New customers start with 0 points
            Customer c = new Customer(username, password, 0);
            Owner.addCustomer(c);
            tableModel.addRow(new Object[]{username, password, 0});
            usernameField.setText("");
            passwordField.setText("");
        });

        deleteButton.addActionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row >= 0) {
                Customer toDelete = BookStoreApplication.customers.get(row);
                Owner.deleteCustomer(toDelete);
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Select a customer to delete.");
            }
        });

        backButton.addActionListener(e -> {
            parentGUI.showScreen("OWNER_START");
        });
    }

    /**
     * Refreshes the table from the customers ArrayList.
     * Called every time we navigate to this screen.
     */
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Customer c : BookStoreApplication.customers) {
            tableModel.addRow(new Object[]{c.getUsername(), c.getPassword(), c.getPoints()});
        }
    }
}
