package bookstore;

public class SilverStatus extends CustomerState {

    @Override
    public void updateState(Customer c) {
        if (c.getPoints() >= 1000) {      // FIXED: was < 1000 (backwards)
            c.setStatus(new GoldStatus());
        }
    }
}
