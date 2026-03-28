package bookstore;

/**
 * Concrete state class representing Gold status in the State Design Pattern.
 *
 * Spec rule:
 * - A customer with 1000 or more points has Gold status.
 * - A customer whose points drop below 1000 transitions to Silver status.
 *
 * This class is only responsible for checking if the customer should be
 * DEMOTED from Gold to Silver. The promotion from Silver to Gold is handled
 * by SilverStatus.updateState().
 *
 * State Design Pattern role: ConcreteState (along with SilverStatus).
 */
public class GoldStatus extends CustomerState {

    public GoldStatus() {
    }

    /**
     * Checks if the customer's points have dropped below 1000.
     * If so, demotes them to Silver by replacing this state object.
     *
     * This is called by Customer after every transaction (buyBook or redeemAndBuy).
     * For example, if a Gold customer redeems enough points that their total
     * falls below 1000, this method swaps in a new SilverStatus.
     *
     * @param c the customer whose state may need to change
     */
    @Override
    public void updateState(Customer c) {
        if (c.getPoints() < 1000) {
            c.setStatus(new SilverStatus());
        }
    }
}
