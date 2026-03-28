package bookstore;

/**
 * Concrete state class representing Silver status in the State Design Pattern.
 *
 * Spec rule:
 * - A customer with fewer than 1000 points has Silver status.
 * - A customer whose points reach 1000 or above transitions to Gold status.
 *
 * This class is only responsible for checking if the customer should be
 * PROMOTED from Silver to Gold. The demotion from Gold to Silver is handled
 * by GoldStatus.updateState().
 *
 * State Design Pattern role: ConcreteState (along with GoldStatus).
 */
public class SilverStatus extends CustomerState {

    /**
     * Checks if the customer's points have reached 1000 or above.
     * If so, promotes them to Gold by replacing this state object.
     *
     * This is called by Customer after every transaction (buyBook or redeemAndBuy).
     * For example, if a Silver customer buys $100 worth of books and earns 1000
     * points, this method swaps in a new GoldStatus.
     *
     * @param c the customer whose state may need to change
     */
    @Override
    public void updateState(Customer c) {
        if (c.getPoints() >= 1000) {
            c.setStatus(new GoldStatus());
        }
    }
}
