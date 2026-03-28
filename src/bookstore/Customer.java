package bookstore;

import java.util.ArrayList;

/**
 * Represents a registered customer in the bookstore.
 *
 * Spec rules:
 * - Customers are created by the owner with a username, password, and 0 starting points.
 * - Customers earn 10 points for every 1 CAD spent.
 * - Customers can redeem points: 100 points = 1 CAD off the transaction cost.
 * - When redeeming, ALL accumulated points must be redeemed when possible.
 * - Transaction cost must not go below 0.
 * - Status is Gold (>= 1000 points) or Silver (< 1000 points).
 *
 * State Design Pattern:
 * - Customer is the "Context" in the State pattern.
 * - It holds a reference to a CustomerState object (GoldStatus or SilverStatus).
 * - After every transaction, status.updateState(this) is called to check
 *   if a state transition is needed.
 * - The state object replaces itself via setStatus() rather than Customer
 *   doing if/else checks — this is the core benefit of the State pattern.
 *
 * Customers are persisted to customers.txt via toString() as "Username,Password,Points".
 */
public class Customer {

    private String username;       // set by the owner when creating the customer
    private String password;       // set by the owner when creating the customer
    private int points;            // earned by buying books, spent by redeeming
    private CustomerState status;  // current state: GoldStatus or SilverStatus

    /**
     * Constructs a Customer with the given credentials and point total.
     *
     * Called in two places:
     * 1. When the owner adds a new customer via OwnerCustomersPanel (points = 0)
     * 2. When loadData() reads a customer from customers.txt (points = saved value)
     *
     * The constructor picks the correct initial state based on the point total,
     * so a customer loaded from file with 5000 points starts as Gold.
     *
     * @param username the customer's login username
     * @param password the customer's login password
     * @param points   the customer's current point total
     */
    public Customer(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = points;

        // Assign initial state based on point total.
        // New customers (0 points) always start Silver.
        // Customers loaded from file get whichever matches their saved points.
        if (points >= 1000) {
            this.status = new GoldStatus();
        } else {
            this.status = new SilverStatus();
        }
    }

    // --- Getters ---

    /** @return the customer's username */
    public String getUsername() {
        return username;
    }

    /** @return the customer's password */
    public String getPassword() {
        return password;
    }

    /** @return the customer's current point total */
    public int getPoints() {
        return points;
    }

    /**
     * Returns the actual CustomerState object (GoldStatus or SilverStatus).
     * Used internally by the state pattern.
     *
     * @return the current state object
     */
    public CustomerState getStatus() {
        return status;
    }

    // --- Setters ---

    /**
     * Updates the point total.
     * Called from buyBook() and redeemAndBuy() during transactions.
     *
     * @param points the new point total
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Swaps the state object. Called by GoldStatus and SilverStatus
     * when a state transition happens.
     *
     * Example: SilverStatus.updateState() calls c.setStatus(new GoldStatus())
     * when points reach 1000. The state object replaces itself — this is the
     * State Design Pattern in action.
     *
     * @param status the new state object to set
     */
    public void setStatus(CustomerState status) {
        this.status = status;
    }

    /**
     * Returns "Gold" or "Silver" as a display string for the GUI.
     * Used in the welcome message: "Your status is Gold".
     *
     * Uses instanceof to check the state object type, since status is an
     * object (State pattern), not a string field.
     *
     * @return "Gold" if current status is GoldStatus, "Silver" otherwise
     */
    public String getStatusName() {
        if (status instanceof GoldStatus) {
            return "Gold";
        } else {
            return "Silver";
        }
    }

    // --- Transaction Methods ---

    /**
     * Buys the selected books WITHOUT redeeming any points.
     *
     * Spec rules applied here:
     * 1. Total cost = sum of all selected book prices (no tax).
     * 2. Customer earns 10 points for every 1 CAD spent.
     * 3. Status is updated via the State pattern (may transition Silver -> Gold).
     *
     * Spec example: Buying books priced 200 + 500 = TC 700, earns 7000 points.
     *
     * @param books the list of books the customer selected (checked in the table)
     * @return the total transaction cost
     */
    public double buyBook(ArrayList<Book> books) {
        // Step 1: Calculate total cost (sum of prices, no tax per spec)
        double tc = 0;
        for (Book b : books) {
            tc += b.getPrice();
        }

        // Step 2: Earn 10 points per dollar spent (cast to int — points are whole numbers)
        int earned = (int) (tc * 10);
        this.points += earned;

        // Step 3: Let the current state check if a transition is needed.
        // e.g. if customer was Silver and points just hit 1000+, switch to Gold.
        status.updateState(this);

        return tc;
    }

    /**
     * Redeems existing points and buys the selected books.
     *
     * Spec rules applied here:
     * 1. Total cost = sum of all selected book prices (no tax).
     * 2. Redeem as many points as possible: 100 points = 1 CAD off.
     *    "When possible, ALL accumulated points must be redeemed."
     * 3. Transaction cost must not go below 0.
     * 4. Customer earns 10 points per 1 CAD of the FINAL cost (after redemption).
     * 5. Status is updated via the State pattern.
     *
     * Spec example walkthrough:
     * - Customer has 7000 points, buys book priced 50.
     * - Max redeem = 7000/100 = 70 dollars. But book only costs 50.
     * - Actual redeem = 50 dollars (5000 points spent). TC = 0.
     * - Remaining points = 7000 - 5000 = 2000. Earns 0 new points (TC is 0).
     * - Final: TC=0, points=2000, status=Gold.
     *
     * @param books the list of books the customer selected (checked in the table)
     * @return the total transaction cost after redemption
     */
    public double redeemAndBuy(ArrayList<Book> books) {
        // Step 1: Calculate total cost before redemption
        double tc = 0;
        for (Book b : books) {
            tc += b.getPrice();
        }

        // Step 2: Figure out how many dollars we can redeem (100 points = 1 dollar)
        int maxRedeemDollars = this.points / 100;

        // Step 3: Can't redeem more dollars than the total cost
        int actualRedeemDollars = (int) Math.min(maxRedeemDollars, tc);

        // Step 4: Subtract the redemption from the cost
        tc = tc - actualRedeemDollars;
        if (tc < 0) tc = 0;  // spec says transaction cost must not be less than 0

        // Step 5: Deduct the points we actually used
        int pointsSpent = actualRedeemDollars * 100;
        this.points -= pointsSpent;

        // Step 6: Earn 10 points per dollar of the REMAINING cost (after redemption)
        int earned = (int) (tc * 10);
        this.points += earned;

        // Step 7: Let the current state check if a transition is needed
        status.updateState(this);

        return tc;
    }

    /**
     * Returns the customer as a comma-separated string for file persistence.
     * Format: "Username,Password,Points"
     *
     * Written to customers.txt when the app closes (via saveData()),
     * and parsed back when the app starts (via loadData()).
     *
     * @return comma-separated representation, e.g. "jane,pass123,7000"
     */
    @Override
    public String toString() {
        return username + "," + password + "," + points;
    }
}
