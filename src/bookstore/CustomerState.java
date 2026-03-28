package bookstore;

/**
 * Abstract base class for the State Design Pattern.
 *
 * Customer is the Context, this is the State interface,
 * GoldStatus and SilverStatus are the ConcreteStates.
 *
 * After every transaction, the Customer calls status.updateState(this)
 * and the current state object decides if a transition is needed.
 */
public abstract class CustomerState {
    public abstract void updateState(Customer c);
}
