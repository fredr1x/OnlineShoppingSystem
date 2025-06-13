package online_shop.service;

import lombok.RequiredArgsConstructor;
import online_shop.dto.WishListItemDeleteDto;
import online_shop.dto.WishListItemDto;
import online_shop.entity.Product;
import online_shop.entity.WishList;
import online_shop.entity.WishListItem;
import online_shop.exception.ProductNotFoundException;
import online_shop.exception.UserNotFoundException;
import online_shop.exception.WishListItemNotFound;
import online_shop.mapper.WishListItemMapper;
import online_shop.mapper.WishListMapper;
import online_shop.repository.ProductRepository;
import online_shop.repository.UserRepository;
import online_shop.repository.WishListItemRepository;
import online_shop.repository.WishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishListService {

    private final WishListMapper wishListMapper;
    private final WishListItemMapper wishListItemMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;
    private final WishListItemRepository wishListItemRepository;

    public List<WishListItemDto> getWishListByUserId(Long userId) throws UserNotFoundException {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        var wishList = wishListRepository.findByUserId(userId);

        if (wishList == null) {
            wishList = WishList.builder()
                    .user(user)
                    .build();
        }

        var saved = wishListRepository.save(wishList);

        return wishListItemMapper.toDto(wishListItemRepository.getAllByWishListId(saved.getId()));
    }

    @Transactional
    public List<WishListItemDto> addWishListItem(WishListItemDto wishListItemDto) throws ProductNotFoundException {

        var wishList = wishListRepository.findById(wishListItemDto.getWishListId());

        var product = productRepository.findById(wishListItemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + wishListItemDto.getProductId() + " not found"));

        var wishListItem = WishListItem.builder()
                .wishList(wishList)
                .product(product)
                .addedAt(Instant.now())
                .build();

        wishListItemRepository.save(wishListItem);
        return wishListItemMapper.toDto(wishListItemRepository.getAllByWishListId(wishList.getId()));

    }

    @Transactional
    public List<WishListItemDto> deleteWishListItem(WishListItemDeleteDto wishListItemDto) throws WishListItemNotFound {

        var wishList = wishListRepository.findById(wishListItemDto.getWishListId());

        var wishListItem = wishListItemRepository.findById(wishListItemDto.getWishListItemId())
                .orElseThrow(() -> new WishListItemNotFound("Wish list item with id " + wishListItemDto.getWishListItemId() + " not found"));

        wishListItemRepository.delete(wishListItem);
        return wishListItemMapper.toDto(wishListItemRepository.getAllByWishListId(wishList.getId()));
    }
}
