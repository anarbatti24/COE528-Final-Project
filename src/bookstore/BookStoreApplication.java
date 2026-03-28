package bookstore;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main application class — the entry point and central data holder.
 *
 * Responsibilities:
 * 1. Holds the inventory (ArrayList of Books) and customers (ArrayList of Customers)
 *    as static fields so Owner and GUI panels can access them directly.
 * 2. Handles file I/O: loads data from books.txt and customers.txt on startup,
 *    saves data back to those files when the window is closed.
 * 3. Provides findCustomer() for login authentication.
 * 4. Launches the GUI via main().
 *
 * Spec rules for file I/O:
 * - Every time the app starts, data from books.txt and customers.txt is loaded.
 * - Every time the [x] button is clicked, current data overwrites the old files.
 * - books.txt format:    "BookName,Price"     (one book per line)
 * - customers.txt format: "Username,Password,Points" (one customer per line)
 */
public class BookStoreApplication {

    // File references for persistence
    static File customerFile = new File("customers.txt");
    static File bookFile = new File("books.txt");

    // Central data structures accessed by Owner and all GUI panels
    protected static ArrayList<Book> inventory = new ArrayList<Book>();
    protected static ArrayList<Customer> customers = new ArrayList<Customer>();

    /**
     * Loads all data from the two text files into the ArrayLists.
     * Called once at startup in main() before the GUI is created.
     *
     * customers.txt is parsed first, then books.txt.
     * Each line is split on commas to extract the fields.
     * Empty lines and malformed lines (wrong number of fields) are skipped.
     */
    public static void loadData() {
        // --- Load customers from customers.txt ---
        // Format per line: "Username,Password,Points"
        if (customerFile.exists()) {
            try (Scanner reader = new Scanner(customerFile)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length == 3) {
                            String username = parts[0].trim();
                            String password = parts[1].trim();
                            int points = Integer.parseInt(parts[2].trim());
                            // Customer constructor picks Gold/Silver based on points
                            customers.add(new Customer(username, password, points));
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Could not find customers.txt");
            }
        }

        // --- Load books from books.txt ---
        // Format per line: "BookName,Price"
        if (bookFile.exists()) {
            try (Scanner reader = new Scanner(bookFile)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length == 2) {
                            String name = parts[0].trim();
                            double price = Double.parseDouble(parts[1].trim());
                            inventory.add(new Book(name, price));
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Could not find books.txt");
            }
        }
    }

    /**
     * Saves all current data to the two text files.
     * Overwrites old data each time, as specified by the spec.
     *
     * Called by BookStoreGUI's WindowListener when the user clicks the [x]
     * button to close the application window.
     *
     * Each object's toString() method produces the comma-separated format
     * that loadData() expects.
     */
    public static void saveData() {
        // --- Save customers to customers.txt ---
        try {
            FileWriter writer = new FileWriter("customers.txt");
            for (int i = 0; i < customers.size(); i++) {
                writer.write(customers.get(i).toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }

        // --- Save books to books.txt ---
        try {
            FileWriter writer = new FileWriter("books.txt");
            for (int i = 0; i < inventory.size(); i++) {
                writer.write(inventory.get(i).toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    /**
     * Searches for a customer matching the given username and password.
     * Used by LoginPanel to authenticate customer logins.
     *
     * The owner login (admin/admin) is checked separately in LoginPanel
     * before this method is called.
     *
     * @param username the username entered in the login screen
     * @param password the password entered in the login screen
     * @return the matching Customer object, or null if no match is found
     */
    public static Customer findCustomer(String username, String password) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Application entry point.
     *
     * 1. Initializes the singleton Owner.
     * 2. Loads saved data from books.txt and customers.txt.
     * 3. Launches the GUI on the Swing event dispatch thread (required by Swing).
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize the singleton owner
        Owner.getInstance();

        // Load saved data from files
        loadData();

        // Launch the GUI on the Swing event thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            BookStoreGUI gui = new BookStoreGUI();
            gui.setVisible(true);
        });
    }
}
