package bookstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Owner books screen — allows the owner to manage the book inventory.
 *
 * Spec requirement:
 * "When the owner clicks the button [Books], she sees a screen having three parts:
 *
 * Top part: A table with two columns — Book Name and Book Price. A row in the
 * table contains the name of a book and its price. Only one copy of a particular
 * book is allowed.
 *
 * Middle part: Two textfields (Name and Price) and one button [Add]. When the
 * owner enters a book's name and price and clicks [Add], a new row containing
 * the book's information gets entered in the table.
 *
 * Bottom part: A [Delete] button and a [Back] button. When the owner selects
 * a row in the table and clicks [Delete], the selected row gets deleted from
 * the table. If the owner clicks [Back], she is taken back to the
 * owner-start-screen."
 *
 * Data synchronization:
 * - The JTable's DefaultTableModel is the visual representation.
 * - BookStoreApplication.inventory is the actual data.
 * - Both are updated in sync on every add/delete operation.
 * - refreshTable() rebuilds the table from the inventory ArrayList.
 *
 * Layout: BorderLayout with table in CENTER, add fields at NORTH, buttons at SOUTH.
 */
public class OwnerBooksPanel extends JPanel {

    private BookStoreGUI parentGUI;       // reference to the main frame for screen switching
    private DefaultTableModel tableModel; // table model backing the JTable
    private JTable bookTable;             // the visual table showing books
    private JTextField nameField;         // text field for entering book name
    private JTextField priceField;        // text field for entering book price

    /**
     * Constructs the owner books panel with the book table,
     * add fields, and delete/back buttons.
     *
     * @param parent the main BookStoreGUI frame
     */
    public OwnerBooksPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Top: Book table ---
        // Two columns as specified: Book Name and Book Price.
        // Cells are not editable — the owner can only add or delete whole books.
        String[] columns = {"Book Name", "Book Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;  // spec: owner cannot edit books, only add/delete
            }
        };
        bookTable = new JTable(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // --- Middle: Name field, Price field, Add button ---
        // Spec: "The middle part contains two textfields Name and Price; one button [Add]."
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(new JLabel("Name:"));
        nameField = new JTextField(15);
        addPanel.add(nameField);
        addPanel.add(new JLabel("Price:"));
        priceField = new JTextField(8);
        addPanel.add(priceField);
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
        // Creates a new Book, adds it to the inventory ArrayList AND the table.
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
                Owner.addBook(b);                          // add to data (inventory ArrayList)
                tableModel.addRow(new Object[]{name, price}); // add to visual table
                nameField.setText("");                      // clear input fields
                priceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Price must be a number.");
            }
        });

        // --- Delete button action ---
        // Removes the selected book from both the inventory ArrayList and the table.
        deleteButton.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row >= 0) {
                // Get the Book object from the inventory at the same index as the table row
                Book toDelete = BookStoreApplication.inventory.get(row);
                Owner.deleteBook(toDelete);     // remove from data
                tableModel.removeRow(row);      // remove from visual table
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to delete.");
            }
        });

        // --- Back button action ---
        // Returns to the owner-start-screen (spec requirement)
        backButton.addActionListener(e -> {
            parentGUI.showScreen("OWNER_START");
        });
    }

    /**
     * Rebuilds the table from the inventory ArrayList.
     * Called every time the user navigates to this screen (from OwnerStartPanel)
     * to ensure the table reflects the current state of the inventory.
     */
    public void refreshTable() {
        tableModel.setRowCount(0);  // clear all existing rows
        for (Book b : BookStoreApplication.inventory) {
            tableModel.addRow(new Object[]{b.getName(), b.getPrice()});
        }
    }
}