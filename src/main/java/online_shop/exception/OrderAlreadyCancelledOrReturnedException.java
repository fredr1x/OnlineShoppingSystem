package online_shop.exception;

public class OrderAlreadyCancelledOrReturnedException extends RuntimeException {
    public OrderAlreadyCancelledOrReturnedException(String message) {
        super(message);
    }
}
