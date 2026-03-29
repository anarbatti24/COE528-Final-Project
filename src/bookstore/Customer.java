package bookstore;

import java.util.ArrayList;

/**
 * Represents a registered customer. Holds username, password, points, and
 * a CustomerState object (Gold or Silver) managed via the State Design Pattern.
 *
 * Created by the owner (0 points) or loaded from customers.txt.
 * Persisted as "Username,Password,Points".
 */
public class Customer {

    private String username;
    private String password;
    private int points;
    private CustomerState status;

    /**
     * Constructor method for a customer
     * @param username  The username we are assigning to the customer
     * @param password  The password we are assigning to the customer
     * @param points    The number of points the customer has
     */
    public Customer(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = points;
        // Pick initial state based on points
        this.status = (points >= 1000) ? new GoldStatus() : new SilverStatus();
    }

    /**
     * Getter methods for the customer
     */
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getPoints() { return points; }
    public CustomerState getStatus() { return status; }

    /**
     * Method to set the number of points a customer has
     * @param points the number of points we are trying to set
     */
    public void setPoints(int points) { this.points = points; }

    /** 
     * Called by GoldStatus/SilverStatus to swap the state object 
     * @param status The status we are updating the customer to
     */
    public void setStatus(CustomerState status) { this.status = status; }

    /** 
     * Returns "Gold" or "Silver" for GUI display 
     */
    public String getStatusName() {
        return (status instanceof GoldStatus) ? "Gold" : "Silver";
    }

    /**
     * Buy books without redeeming points.
     * Earns 10 points per dollar spent, then updates state.
     * @param books The book they are trying to buy
     * @return The total cost of their transaction
     */
    public double buyBook(ArrayList<Book> books) {
        double tc = 0;
        for (Book b : books) tc += b.getPrice();

        int earned = (int) (tc * 10);
        this.points += earned;

        status.updateState(this);
        return tc;
    }

    /**
     * Redeem all possible points, then buy books.
     * 100 points = 1 CAD off. TC cannot go below 0.
     * Earns 10 points per dollar of the remaining cost after redemption.
     * @param books The book the customer is trying to buy
     * @return The total cost of their transaction
     */
    public double redeemAndBuy(ArrayList<Book> books) {
        double tc = 0;
        for (Book b : books) tc += b.getPrice();

        // Redeem: 100 points = 1 dollar off, can't exceed total cost
        int maxRedeemDollars = this.points / 100;
        int actualRedeemDollars = (int) Math.min(maxRedeemDollars, tc);

        tc = tc - actualRedeemDollars;
        if (tc < 0) tc = 0;

        // Deduct spent points, earn new points on remaining cost
        this.points -= (actualRedeemDollars * 100);
        this.points += (int) (tc * 10);

        status.updateState(this);
        return tc;
    }

    /** Format for customers.txt persistence: "Username,Password,Points" */
    @Override
    public String toString() {
        return username + "," + password + "," + points;
    }
}
