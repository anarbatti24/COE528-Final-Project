package bookstore;

/**
 * Represents a single book in the bookstore inventory.
 *
 * Spec rules:
 * - A book has two properties: name and price.
 * - Only one copy of a particular book is allowed in the store.
 * - Once the owner adds a book, its name and price cannot be changed.
 * - The owner can only add or delete entire books, not edit them.
 *
 * Books are created in two places:
 * 1. By the owner via OwnerBooksPanel (typing name + price and clicking Add)
 * 2. By BookStoreApplication.loadData() when reading from books.txt on startup
 *
 * Books are persisted to books.txt via toString() which outputs "Name,Price".
 */
public class Book {

    private String name;   // title of the book
    private double price;  // price in CAD

    /**
     * Constructs a new Book with the given name and price.
     *
     * @param name  the book title
     * @param price the book price in CAD
     */
    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * @return the book title
     */
    public String getName() {
        return name;
    }

    /**
     * @return the book price in CAD
     */
    public double getPrice() {
        return price;
    }

    // No setters — spec says books cannot be edited once added.

    /**
     * Returns the book as a comma-separated string for file persistence.
     * Format: "BookName,Price"
     *
     * This format is written to books.txt when the app closes (via saveData()),
     * and parsed back when the app starts (via loadData()).
     *
     * @return comma-separated representation, e.g. "Harry Potter,19.95"
     */
    @Override
    public String toString() {
        return name + "," + price;
    }
}
