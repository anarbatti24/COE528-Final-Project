package bookstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Owner customers screen. Spec layout: table on top, add fields in middle,
 * delete/back buttons at bottom. Owner can add and delete customers.
 * New customers always start with 0 points.
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

        // Top: customer table (CENTER so it expands to fill space)
        String[] columns = {"Username", "Password", "Points"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        customerTable = new JTable(tableModel);
        add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // Middle: Username, Password, Add
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(12);
        addPanel.add(usernameField);
        addPanel.add(new JLabel("Password:"));
        passwordField = new JTextField(12);
        addPanel.add(passwordField);
        JButton addButton = new JButton("Add");
        addPanel.add(addButton);

        // Bottom: Delete, Back
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        // Stack middle + bottom below the table
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(addPanel);
        southPanel.add(buttonPanel);
        add(southPanel, BorderLayout.SOUTH);

        // Add button: create customer with 0 points, add to list and table
        addButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.");
                return;
            }
            Customer c = new Customer(username, password, 0);
            Owner.addCustomer(c);
            tableModel.addRow(new Object[]{username, password, 0});
            usernameField.setText("");
            passwordField.setText("");
        });

        // Delete button: remove selected customer from list and table
        deleteButton.addActionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row >= 0) {
                Owner.deleteCustomer(BookStoreApplication.customers.get(row));
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Select a customer to delete.");
            }
        });

        backButton.addActionListener(e -> parentGUI.showScreen("OWNER_START"));
    }

    /** Rebuilds table from customers list. Called when navigating to this screen. */
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Customer c : BookStoreApplication.customers)
            tableModel.addRow(new Object[]{c.getUsername(), c.getPassword(), c.getPoints()});
    }
}
