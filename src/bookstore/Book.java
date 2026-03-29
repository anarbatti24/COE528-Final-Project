package bookstore;

/**
 * Represents a single book. Has a name and price, both immutable once created.
 * Only one copy of each book is allowed in the store (spec requirement).
 * Persisted to books.txt as "Name,Price".
 */
public class Book {

    private String name;
    private double price;

    /**
     * Constructor for the function
     * @param name     The name of the book
     * @param price     The price of the book
     */
    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Getter method for the name
     */
    public String getName() { return name; }

    /**
     * Getter method for the price
     */
    public double getPrice() { return price; }

    // No setters — spec says books can only be added or deleted, not edited.

    /** Format for books.txt persistence: "BookName,Price" */
    @Override
    public String toString() {
        return name + "," + price;
    }
}
