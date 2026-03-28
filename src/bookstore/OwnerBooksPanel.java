package bookstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Owner books screen. Spec layout: table on top, add fields in middle,
 * delete/back buttons at bottom. Owner can add and delete books.
 */
public class OwnerBooksPanel extends JPanel {

    private BookStoreGUI parentGUI;
    private DefaultTableModel tableModel;
    private JTable bookTable;
    private JTextField nameField;
    private JTextField priceField;

    public OwnerBooksPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: book table (CENTER so it expands to fill space)
        String[] columns = {"Book Name", "Book Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        bookTable = new JTable(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // Middle: Name, Price, Add
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(new JLabel("Name:"));
        nameField = new JTextField(15);
        addPanel.add(nameField);
        addPanel.add(new JLabel("Price:"));
        priceField = new JTextField(8);
        addPanel.add(priceField);
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

        // Add button: create book, add to inventory and table
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            if (name.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both name and price.");
                return;
            }
            try {
                double price = Double.parseDouble(priceStr);
                Book b = new Book(name, price);
                Owner.addBook(b);
                tableModel.addRow(new Object[]{name, price});
                nameField.setText("");
                priceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Price must be a number.");
            }
        });

        // Delete button: remove selected book from inventory and table
        deleteButton.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row >= 0) {
                Owner.deleteBook(BookStoreApplication.inventory.get(row));
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to delete.");
            }
        });

        backButton.addActionListener(e -> parentGUI.showScreen("OWNER_START"));
    }

    /** Rebuilds table from inventory. Called when navigating to this screen. */
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Book b : BookStoreApplication.inventory)
            tableModel.addRow(new Object[]{b.getName(), b.getPrice()});
    }
}
