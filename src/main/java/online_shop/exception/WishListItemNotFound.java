package online_shop.exception;

public class WishListItemNotFound extends RuntimeException {
    public WishListItemNotFound(String message) {
        super(message);
    }
}
