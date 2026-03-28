package bookstore;

/**
 * Represents a single book. Has a name and price, both immutable once created.
 * Only one copy of each book is allowed in the store (spec requirement).
 * Persisted to books.txt as "Name,Price".
 */
public class Book {

    private String name;
    private double price;

    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    // No setters — spec says books can only be added or deleted, not edited.

    /** Format for books.txt persistence: "BookName,Price" */
    @Override
    public String toString() {
        return name + "," + price;
    }
}
