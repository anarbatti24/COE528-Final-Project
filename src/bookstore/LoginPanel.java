package bookstore;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen panel — the first screen shown when the app starts.
 *
 * Spec requirement:
 * "The app starts with a login-screen. The login-screen has three GUI items:
 * a username field, a password field, and the button [login]."
 *
 * Login logic:
 * - If username is "admin" and password is "admin", the owner is logged in
 *   and the screen switches to OwnerStartPanel (owner-start-screen).
 * - Otherwise, the app searches for a matching registered customer using
 *   BookStoreApplication.findCustomer(). If found, the customer is logged in
 *   and the screen switches to CustomerStartPanel (customer-start-screen).
 * - If no match is found, an error dialog is shown.
 *
 * The password field uses JPasswordField to mask input characters.
 */
public class LoginPanel extends JPanel {

    private BookStoreGUI parentGUI;       // reference to the main frame for screen switching
    private JTextField usernameField;     // text field for username input
    private JPasswordField passwordField; // password field (masks characters with dots)

    /**
     * Constructs the login panel with all GUI components and wires up
     * the login button action.
     *
     * @param parent the main BookStoreGUI frame (used for screen switching)
     */
    public LoginPanel(BookStoreGUI parent) {
        this.parentGUI = parent;

        // Stack rows vertically using BoxLayout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // --- Title row ---
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleRow.add(new JLabel("Welcome to the BookStore App"));
        add(titleRow);

        add(Box.createVerticalStrut(20)); // spacer

        // --- Username row ---
        JPanel usernameRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernameRow.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        usernameRow.add(usernameField);
        add(usernameRow);

        // --- Password row ---
        JPanel passwordRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordRow.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        passwordRow.add(passwordField);
        add(passwordRow);

        add(Box.createVerticalStrut(10)); // spacer

        // --- Login button row ---
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        buttonRow.add(loginButton);
        add(buttonRow);

        // --- Login button action ---
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.equals("admin") && password.equals("admin")) {
                // Owner login — switch to owner-start-screen
                parentGUI.showScreen("OWNER_START");
            } else {
                // Attempt customer login — search registered customers
                Customer c = BookStoreApplication.findCustomer(username, password);
                if (c != null) {
                    // Customer found — set the customer on the start screen
                    // (this updates the welcome message and book table)
                    // then switch to customer-start-screen
                    parentGUI.getCustomerStartPanel().setCustomer(c);
                    parentGUI.showScreen("CUSTOMER_START");
                } else {
                    // No match — show error dialog
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                }
            }

            // Clear the input fields after every login attempt
            usernameField.setText("");
            passwordField.setText("");
        });
    }
}
