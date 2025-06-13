package online_shop.exception;

public class CartIsEmptyException extends Exception {
    public CartIsEmptyException(String message) {
        super(message);
    }
}
