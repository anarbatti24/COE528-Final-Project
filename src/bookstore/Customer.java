/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstore;

/**
 *
 * @author ayarramreddy
 */

// represents registered customer
// customer created by owner with 0 points initially
public class Customer {
    private String username;  // login username (set by owner)
    private String password;  // login password (set by owner)
    private int points;       // accumulated loyalty points

    // constructor creates a customer with the entered credentials and point total
    public Customer(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = points;
    }

    // return customer username
    public String getUsername() {
        return username;
    }

    // return customer password
    public String getPassword() {
        return password;
    }

    // return current point total of customer
    public int getPoints() {
        return points;
    }

    // updates customers points after transaction (earned/redeemed)
    public void setPoints(int points) {
        this.points = points;
    }

    // returns gold if points >= 1k otherwise silver
    // state design pattern
    public String getStatus() {
        return points >= 1000 ? "Gold" : "Silver";
    }

    // reutrns comma seperated string to write into customers.txt
    // "username,password,points"  e.g. "Bob,pass123,7000"
    @Override
    public String toString() {
        return username + "," + password + "," + points;
    }
}