package bookstore;

import static bookstore.BookStoreApplication.inventory;
import static bookstore.BookStoreApplication.customers;

/**
 * Singleton owner. Username/password are always admin/admin.
 * Manages books and customers via static methods that operate on
 * BookStoreApplication's inventory and customers lists.
 */
public class Owner {

    private static final String username = "admin";
    private static final String password = "admin";
    private static Owner instance;

    /**
     * Constructor method for the owner
     * @param username The username of the owner -> "admin" (as per spec)
     * @param password The password of the owner -> "admin" (as per spec)
     */
    private Owner(String username, String password) {}

    /**
     * Can only have one owner, so we used Singleton Design Pattern
     */
    public static Owner getInstance() {
        if (instance == null) {
            instance = new Owner(username, password);
        }
        return instance;
    }

    /**
     * Owner method capability functions
     */
    public static void addBook(Book b) { inventory.add(b); }
    public static void deleteBook(Book b) { inventory.remove(b); }
    public static void addCustomer(Customer c) { customers.add(c); }
    public static void deleteCustomer(Customer c) { customers.remove(c); }
}
