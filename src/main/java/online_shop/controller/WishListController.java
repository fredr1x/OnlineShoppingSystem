package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.dto.WishListItemDeleteDto;
import online_shop.dto.WishListItemDto;
import online_shop.exception.ProductNotFoundException;
import online_shop.exception.UserNotFoundException;
import online_shop.exception.WishListItemNotFound;
import online_shop.service.WishListService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wish_lists")
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("/{id}")
    public ResponseEntity<List<WishListItemDto>> getWishListByUserId(@PathVariable("id") Long userId) throws UserNotFoundException {
        return ResponseEntity.ok().body(wishListService.getWishListByUserId(userId));
    }
    
    @PostMapping("/add_item")
    public ResponseEntity<List<WishListItemDto>> addWishListItem(@RequestBody @Validated WishListItemDto wishListItemDto) throws ProductNotFoundException {
        return ResponseEntity.ok().body(wishListService.addWishListItem(wishListItemDto));
    }
    
    @DeleteMapping("/delete_item")
    public ResponseEntity<List<WishListItemDto>> deleteWishListItem(@RequestBody WishListItemDeleteDto wishListItemDto) throws WishListItemNotFound {
        return ResponseEntity.ok().body(wishListService.deleteWishListItem(wishListItemDto));
    }

}
