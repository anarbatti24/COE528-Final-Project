package bookstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Owner books screen.
 * Top: table with Book Name, Book Price columns.
 * Middle: Name field, Price field, Add button.
 * Bottom: Delete button, Back button.
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

        // --- Top: Book table ---
        String[] columns = {"Book Name", "Book Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // owner can't edit cells, only add/delete whole books
            }
        };
        bookTable = new JTable(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // --- Middle: Name, Price, Add ---
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(new JLabel("Name:"));
        nameField = new JTextField(15);
        addPanel.add(nameField);
        addPanel.add(new JLabel("Price:"));
        priceField = new JTextField(8);
        addPanel.add(priceField);
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

        deleteButton.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row >= 0) {
                Book toDelete = BookStoreApplication.inventory.get(row);
                Owner.deleteBook(toDelete);
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to delete.");
            }
        });

        backButton.addActionListener(e -> {
            parentGUI.showScreen("OWNER_START");
        });
    }

    /**
     * Refreshes the table from the inventory ArrayList.
     * Called every time we navigate to this screen.
     */
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Book b : BookStoreApplication.inventory) {
            tableModel.addRow(new Object[]{b.getName(), b.getPrice()});
        }
    }
}
