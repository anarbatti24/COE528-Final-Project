package bookstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Customer start screen. Shows welcome message with points/status,
 * book table with checkboxes for selection, and Buy/Redeem/Logout buttons.
 */
public class CustomerStartPanel extends JPanel {

    private BookStoreGUI parentGUI;
    private Customer currentCustomer;
    private DefaultTableModel tableModel;
    private JTable bookTable;
    private JLabel welcomeLabel;

    public CustomerStartPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: welcome message (updated dynamically in refresh())
        welcomeLabel = new JLabel("Welcome");
        add(welcomeLabel, BorderLayout.NORTH);

        // Middle: book table with checkbox column
        // Overriding getColumnClass to return Boolean.class makes Swing
        // render column 2 as checkboxes automatically
        tableModel = new DefaultTableModel(
                new String[]{"Book Name", "Book Price", "Select"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) return Boolean.class;
                if (column == 1) return Double.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 2; // only checkboxes are clickable
            }
        };
        bookTable = new JTable(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // Bottom: Buy, Redeem Points and Buy, Logout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton buyButton = new JButton("Buy");
        JButton redeemButton = new JButton("Redeem Points and Buy");
        JButton logoutButton = new JButton("Logout");
        bottomPanel.add(buyButton);
        bottomPanel.add(redeemButton);
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        buyButton.addActionListener(e -> {
            ArrayList<Book> selected = getSelectedBooks();
            if (selected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select at least one book.");
                return;
            }
            double cost = currentCustomer.buyBook(selected);
            parentGUI.getCustomerCostPanel().setData(currentCustomer, cost);
            parentGUI.showScreen("CUSTOMER_COST");
        });

        redeemButton.addActionListener(e -> {
            ArrayList<Book> selected = getSelectedBooks();
            if (selected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select at least one book.");
                return;
            }
            double cost = currentCustomer.redeemAndBuy(selected);
            parentGUI.getCustomerCostPanel().setData(currentCustomer, cost);
            parentGUI.showScreen("CUSTOMER_COST");
        });

        logoutButton.addActionListener(e -> parentGUI.showScreen("LOGIN"));
    }

    /** Sets the logged-in customer and refreshes the screen. Called from LoginPanel. */
    public void setCustomer(Customer c) {
        this.currentCustomer = c;
        refresh();
    }

    /** Updates welcome label and repopulates book table from inventory. */
    public void refresh() {
        welcomeLabel.setText("Welcome " + currentCustomer.getUsername()
                + ". You have " + currentCustomer.getPoints()
                + " points. Your status is " + currentCustomer.getStatusName());

        tableModel.setRowCount(0);
        for (Book b : BookStoreApplication.inventory)
            tableModel.addRow(new Object[]{b.getName(), b.getPrice(), false});
    }

    /** Returns all books whose checkbox is checked. */
    private ArrayList<Book> getSelectedBooks() {
        ArrayList<Book> selected = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Boolean) tableModel.getValueAt(i, 2))
                selected.add(BookStoreApplication.inventory.get(i));
        }
        return selected;
    }
}
