/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstore;

import static bookstore.BookStoreApplication.inventory;
import static bookstore.BookStoreApplication.customers;

/**
 *
 * @author ayarramr
 */
public class Owner {

    private static final String username = "admin";
    private static final String password = "admin";
    private static Owner instance;


    private Owner(String username, String password) {

    }

    public static Owner getInstance() {

        if (instance == null) {
            instance = new Owner(username, password);
        }
        return instance;
    }

    
    /**
     * Method to add a book to the arrayList
     *
     * @param b The book we are adding
     */
    public static void addBook(Book b) {

        inventory.add(b);
        
        System.out.println("IN ADD BOOK");

    }

    /**
     * Method to delete a book from the arrayList
     *
     * @param b
     */
    public static void deleteBook(Book b) {

        inventory.remove(b);

    }

    /**
     * Method to add a customer to the application
     *
     * @param c The customer we are adding
     */
    public static void addCustomer(Customer c) {

        customers.add(c);
    }

    /**
     * Method to remove a customer from the application
     *
     * @param c The customer we are removing
     */
    public static void deleteCustomer(Customer c) {

        customers.remove(c);
    }

}
