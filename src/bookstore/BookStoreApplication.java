package bookstore;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main application class. Holds the inventory and customers as static ArrayLists,
 * handles file I/O (load on startup, save on window close), and launches the GUI.
 *
 * books.txt format:     "BookName,Price"
 * customers.txt format: "Username,Password,Points"
 */
public class BookStoreApplication {

    static File customerFile = new File("customers.txt");
    static File bookFile = new File("books.txt");
    protected static ArrayList<Book> inventory = new ArrayList<Book>();
    protected static ArrayList<Customer> customers = new ArrayList<Customer>();

    /** Loads books and customers from text files into the ArrayLists. */
    public static void loadData() {
        if (customerFile.exists()) {
            try (Scanner reader = new Scanner(customerFile)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length == 3) {
                            customers.add(new Customer(
                                parts[0].trim(), parts[1].trim(),
                                Integer.parseInt(parts[2].trim())));
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Could not find customers.txt");
            }
        }

        if (bookFile.exists()) {
            try (Scanner reader = new Scanner(bookFile)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length == 2) {
                            inventory.add(new Book(
                                parts[0].trim(),
                                Double.parseDouble(parts[1].trim())));
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Could not find books.txt");
            }
        }
    }

    /** Saves all data to text files. Overwrites old data each time (spec requirement). */
    public static void saveData() {
        try {
            FileWriter writer = new FileWriter("customers.txt");
            for (int i = 0; i < customers.size(); i++)
                writer.write(customers.get(i).toString() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }

        try {
            FileWriter writer = new FileWriter("books.txt");
            for (int i = 0; i < inventory.size(); i++)
                writer.write(inventory.get(i).toString() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    /** Finds a customer by username+password. Returns null if not found. */
    public static Customer findCustomer(String username, String password) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username) && c.getPassword().equals(password))
                return c;
        }
        return null;
    }

    public static void main(String[] args) {
        Owner.getInstance();
        loadData();
        javax.swing.SwingUtilities.invokeLater(() -> {
            BookStoreGUI gui = new BookStoreGUI();
            gui.setVisible(true);
        });
    }
}
