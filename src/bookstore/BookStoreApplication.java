package bookstore;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main application class.
 * Holds the inventory and customers lists as static fields.
 * Handles file I/O and launches the GUI.
 */
public class BookStoreApplication {
    static File customerFile = new File("customers.txt");
    static File bookFile = new File("books.txt");
    protected static ArrayList<Book> inventory = new ArrayList<Book>();
    protected static ArrayList<Customer> customers = new ArrayList<Customer>();

    /**
     * Loads books from books.txt and customers from customers.txt.
     * books.txt format:    BookName,Price
     * customers.txt format: Username,Password,Points
     */
    public static void loadData() {
        // Load customers
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
                            customers.add(new Customer(username, password, points));
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Could not find customers.txt");
            }
        }

        // Load books
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
     * Saves all books to books.txt and all customers to customers.txt.
     * Overwrites old data each time.
     */
    public static void saveData() {
        // Save customers
        try {
            FileWriter writer = new FileWriter("customers.txt");
            for (int i = 0; i < customers.size(); i++) {
                writer.write(customers.get(i).toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }

        // Save books
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
     * Finds a customer by username and password.
     * Returns null if no match found.
     */
    public static Customer findCustomer(String username, String password) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // Initialize the singleton owner
        Owner.getInstance();

        // Load saved data from files
        loadData();

        // Launch the GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            BookStoreGUI gui = new BookStoreGUI();
            gui.setVisible(true);
        });
    }
}
