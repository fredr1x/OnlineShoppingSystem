package online_shop.exception;

public class OrderAlreadyCancelledOrReturnedException extends Exception {
    public OrderAlreadyCancelledOrReturnedException(String message) {
        super(message);
    }
}
