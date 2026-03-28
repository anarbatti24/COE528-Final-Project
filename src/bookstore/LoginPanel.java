package bookstore;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen. Username field, password field, Login button.
 * Checks for owner (admin/admin) or registered customer.
 * Switches to OwnerStartPanel or CustomerStartPanel accordingly.
 */
public class LoginPanel extends JPanel {

    private BookStoreGUI parentGUI;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(BookStoreGUI parent) {
        this.parentGUI = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Title
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleRow.add(new JLabel("Welcome to the BookStore App"));
        add(titleRow);

        add(Box.createVerticalStrut(20));

        // Username row
        JPanel usernameRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernameRow.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        usernameRow.add(usernameField);
        add(usernameRow);

        // Password row
        JPanel passwordRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordRow.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        passwordRow.add(passwordField);
        add(passwordRow);

        add(Box.createVerticalStrut(10));

        // Login button
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        buttonRow.add(loginButton);
        add(buttonRow);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.equals("admin") && password.equals("admin")) {
                parentGUI.showScreen("OWNER_START");
            } else {
                Customer c = BookStoreApplication.findCustomer(username, password);
                if (c != null) {
                    parentGUI.getCustomerStartPanel().setCustomer(c);
                    parentGUI.showScreen("CUSTOMER_START");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                }
            }

            usernameField.setText("");
            passwordField.setText("");
        });
    }
}
