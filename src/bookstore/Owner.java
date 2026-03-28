package bookstore;

import static bookstore.BookStoreApplication.inventory;
import static bookstore.BookStoreApplication.customers;

/**
 * Represents the single owner of the bookstore.
 *
 * Spec rules:
 * - There is exactly one owner. Username is "admin", password is "admin".
 * - The owner can add/delete books from the inventory.
 * - The owner can add/delete (register/unregister) customers.
 *
 * Implemented as a Singleton pattern — getInstance() ensures only one
 * Owner object exists. The owner operates on the static inventory and
 * customers lists in BookStoreApplication.
 *
 * The owner's add/delete methods are called from the GUI panels:
 * - addBook / deleteBook are called from OwnerBooksPanel
 * - addCustomer / deleteCustomer are called from OwnerCustomersPanel
 */
public class Owner {

    // Owner credentials are hardcoded as per the spec
    private static final String username = "admin";
    private static final String password = "admin";

    // Singleton instance — only one owner exists
    private static Owner instance;

    /**
     * Private constructor enforces singleton pattern.
     * Cannot be called from outside this class.
     *
     * @param username owner's username (always "admin")
     * @param password owner's password (always "admin")
     */
    private Owner(String username, String password) {
        // No initialization needed — owner just provides methods
        // to manage inventory and customers
    }

    /**
     * Returns the single Owner instance, creating it if it doesn't exist.
     * Called once in BookStoreApplication.main() to initialize.
     *
     * @return the singleton Owner instance
     */
    public static Owner getInstance() {
        if (instance == null) {
            instance = new Owner(username, password);
        }
        return instance;
    }

    /**
     * Adds a book to the bookstore inventory.
     * Called from OwnerBooksPanel when the owner fills in the Name and Price
     * fields and clicks the [Add] button.
     *
     * @param b the Book to add
     */
    public static void addBook(Book b) {
        inventory.add(b);
    }

    /**
     * Removes a book from the bookstore inventory.
     * Called from OwnerBooksPanel when the owner selects a row in the
     * table and clicks the [Delete] button.
     *
     * @param b the Book to remove
     */
    public static void deleteBook(Book b) {
        inventory.remove(b);
    }

    /**
     * Registers a new customer in the bookstore.
     * Called from OwnerCustomersPanel when the owner fills in the Username
     * and Password fields and clicks the [Add] button.
     * New customers always start with 0 points (enforced in the panel).
     *
     * @param c the Customer to add
     */
    public static void addCustomer(Customer c) {
        customers.add(c);
    }

    /**
     * Removes a customer from the bookstore.
     * Called from OwnerCustomersPanel when the owner selects a row in
     * the table and clicks the [Delete] button.
     *
     * @param c the Customer to remove
     */
    public static void deleteCustomer(Customer c) {
        customers.remove(c);
    }
}
