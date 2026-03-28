package bookstore;

/**
 * Gold status — customer has >= 1000 points.
 * Demotes to Silver if points drop below 1000 (e.g. after redeeming).
 */
public class GoldStatus extends CustomerState {

    public GoldStatus() {}

    @Override
    public void updateState(Customer c) {
        if (c.getPoints() < 1000) {
            c.setStatus(new SilverStatus());
        }
    }
}
