/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstore;

/**
 *
 * @author ayarramreddy
 */

// represents a single book in the bookstore with a name and price
// only one copy of each book exists in the store
public class Book {
    private String name;   // the title of the book
    private double price;  // price 

    // constructor - creates a book with the given name and price
    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // returns the book name
    public String getName() {
        return name;
    }

    // returns the book price
    public double getPrice() {
        return price;
    }

    // returns comma seperated string to write into books.txt
    // "BookName,Price"  e.g. "Harry Potter,19.99"
    @Override
    public String toString() {
        return name + "," + price;
    }
}
