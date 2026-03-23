package bookstore;

import java.util.ArrayList;

/*
 * Represents a registered customer in the bookstore.
 * 
 * Customers are created by the owner (via OwnerCustomerScreen) with a username, password, and 0 starting points. They earn points by buying books and can redeem points for discounts.
 * 
 * The Gold/Silver status uses the State Design Pattern:
 * - Customer holds a reference to a CustomerState object (either GoldStatus or SilverStatus)
 * - After every transaction, status.updateState(this) is called
 * - That method checks the point total and swaps the status object if needed
 * - This avoids having if/else checks for status scattered across the codebase
 */

public class Customer {
    private String username;       // set by owner
    private String password;       // set by owner
    private int points;            // points earned by buying, spent by redeeming
    private CustomerState status;  // current state object: GoldStatus or SilverStatus

    /*
     * Creates a customer, called in two places:
     * 1. When the owner adds a new customer (points = 0)
     * 2. When loadData() reads a customer from customers.txt (points = whatever was saved)
     * 
     * the constructor picks the right initial status based on points,
     * so a customer loaded from file with 5000 points starts as Gold
     */
    public Customer(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = points;

        // pick initial status based on point total
        // New customers (0 points) always start silver
        // customers loaded from file get whichever matches their saved points
        if (points >= 1000) {
            this.status = new GoldStatus();
        } else {
            this.status = new SilverStatus();
        }
    }

  

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    /*
     * Returns the actual CustomerState object (GoldStatus or SilverStatus).
     * used by the state pattern.
     */
    public CustomerState getStatus() {
        return status;
    }

    // --- Setters ---

    
    //updates the point total. Called from buyBook() and redeemAndBuy() below, and also by the state classes when they need to check/update points.
     
    public void setPoints(int points) {
        this.points = points;
    }

    /*
     * Swaps the status object. Called by GoldStatus and SilverStatus when a state transition happens.
     * 
     * e.g. SilverStatus.updateState() calls c.setStatus(new GoldStatus())
     * when points reach 1000. This is the state design pattern the state object replaces itself rather than the customer doing if/else
     */
    public void setStatus(CustomerState status) {
        this.status = status;
    }

    /*
     * Returns Gold or Silver as a string for display in the GUI
     * welcome message says "Your status is Gold" this method provides that text
     * 
     * We check instanceof rather than storing a string because the status is an object (State pattern), not a string field.
     */
    public String getStatusName() {
        if (status instanceof GoldStatus) {
            return "Gold";
        } else {
            return "Silver";
        }
    }

    //  Transaction Methods 

    /*
     * buy selected books WITHOUT redeeming any points
     * 
     * how it works:
     * 1. total cost = sum of all selected book prices no tax
     * 2. Customer earns 10 points for every 1 dollar spent
     * 3. status is updated via the State pattern (might go from Silver to Gold)
     */
    public double buyBook(ArrayList<Book> books) {
        //calculate total cost
        double tc = 0;
        for (Book b : books) {
            tc += b.getPrice();
        }

        // earn 10 points per dollar spent
        // cast to int, points are whole nums
        int earned = (int) (tc * 10);
        this.points += earned;

        // tell the current status to check if a transition is needed
        // e.g. if customer was silver and points just hit 1000+, switch to Gold
        status.updateState(this);

        return tc;
    }

    /*
     * Redeem existing points and buy selected books.
     * 
     * how it works:  
     * 1. Total cost = sum of all selected book prices
     * 2. Redeem as many points as possible: 100 points = 1 dollar off
     *    "When possible, ALL accumulated points must be redeemed."
     * 3. TC cannot go below 0.
     * 4. Customer earns 10 points per 1 CAD of the FINAL cost (after redemption)
     * 5. Status is updated via State pattern
     */
    public double redeemAndBuy(ArrayList<Book> books) {
        // calculate total cost before redemption
        double tc = 0;
        for (Book b : books) {
            tc += b.getPrice();
        }

        //figure out how many dollars we can redeem
        int maxRedeemDollars = this.points / 100;

        // cant redeem more dollars than the total cost
        int actualRedeemDollars = (int) Math.min(maxRedeemDollars, tc);

        // subtract the redemption from the cost
        tc = tc - actualRedeemDollars;
        if (tc < 0) tc = 0;  // spec says total cost must not go below 0

        // deduct the points we actually used
        int pointsSpent = actualRedeemDollars * 100;
        this.points -= pointsSpent;

        // earn 10 points per dollar of the remaining cost
        // If cost  after redemption is $80, earn 800 points
        int earned = (int) (tc * 10);
        this.points += earned;

        // update status 
        status.updateState(this);

        return tc;
    }

    
     // Returns the customer as a comma-separated string, e.g. "jane,pass123,7000"
    //same idea as Book.toString()
     
    @Override
    public String toString() {
        return username + "," + password + "," + points;
    }
}