package online_shop.exception;

public class CartItemNotFound extends RuntimeException {
    public CartItemNotFound(String message) {
        super(message);
    }
}
