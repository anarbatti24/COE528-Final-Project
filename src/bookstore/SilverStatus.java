package bookstore;

/**
 * Silver status — customer has < 1000 points.
 * Promotes to Gold if points reach 1000 or above (e.g. after buying).
 */
public class SilverStatus extends CustomerState {

    @Override
    public void updateState(Customer c) {
        if (c.getPoints() >= 1000) {
            c.setStatus(new GoldStatus());
        }
    }
}
