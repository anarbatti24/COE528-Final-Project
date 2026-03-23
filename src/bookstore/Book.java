package bookstore;

/*
 * represents a single book in the book store
 * 
 * book has 2 properties, name and price
 * only 1 copy of a particular book allowed
 * 
 * once owner adds a book name and price dont change
 * owner can only add or delete entire books, not edit them
 */
public class Book {
    private String name;   // book title
    private double price;  // price 
    
    
  
    //creates a new book, called when the owner types a name and price into the fields on OwnerBookScreen and clicks Add    
    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // getters only no setters needed since books can't be edited, only added or deleted
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    /*
     * returns the book as a comma-separated string
     * "Harry Potter,19.99"
     * 
     * this is the format written to books.txt when the app closes
     * When the app starts, BookStoreApplication.loadData() reads each line, splits on the comma, and constructs a Book from the two parts
     */
    @Override
    public String toString() {
        return name + "," + price;
    }
}