package bookstore;

/**
 * Abstract base class for the State Design Pattern.
 *
 * The spec requires that customer status (Gold or Silver) is managed using
 * the State Design Pattern. This abstract class defines the interface that
 * both GoldStatus and SilverStatus implement.
 *
 * How the pattern works:
 * - Customer holds a reference to a CustomerState object (either GoldStatus or SilverStatus).
 * - After every purchase transaction (buyBook or redeemAndBuy), the Customer calls
 *   status.updateState(this) to let the current state decide if a transition is needed.
 * - If the state determines a transition should happen, it calls c.setStatus(new OtherStatus())
 *   to replace itself with the new state object.
 * - This avoids scattering if/else status checks throughout the codebase.
 *
 * State Design Pattern participants (for the class diagram / report):
 * - Context:       Customer (holds the state reference)
 * - State:         CustomerState (this abstract class)
 * - ConcreteState: GoldStatus, SilverStatus
 */
public abstract class CustomerState {

    /**
     * Checks whether the customer's current point total requires a state transition.
     * If so, updates the customer's status by calling c.setStatus().
     *
     * @param c the customer whose state may need to change
     */
    public abstract void updateState(Customer c);
}
