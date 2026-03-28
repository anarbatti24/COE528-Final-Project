package bookstore;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen. Username field, password field, Login button.
 * Checks for owner (admin/admin) or registered customer.
 */
public class LoginPanel extends JPanel {

    private BookStoreGUI parentGUI;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    public LoginPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Title
        titleLabel = new JLabel("Welcome to the BookStore App");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Username label
        usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(usernameLabel, gbc);

        // Username field
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        // Password label
        passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(passwordLabel, gbc);

        // Password field
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        // Action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.equals("admin") && password.equals("admin")) {
                // Owner login
                parentGUI.showScreen("OWNER_START");
            } else {
                // Customer login
                Customer c = BookStoreApplication.findCustomer(username, password);
                if (c != null) {
                    parentGUI.getCustomerStartPanel().setCustomer(c);
                    parentGUI.showScreen("CUSTOMER_START");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                }
            }

            // Clear fields after login attempt
            usernameField.setText("");
            passwordField.setText("");
        });
    }
}
