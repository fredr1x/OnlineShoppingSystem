package online_shop.repository;

import online_shop.dto.WishListItemDto;
import online_shop.entity.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {

    @Query("")
    List<WishListItem> getAllByWishListId(Long wishListId);
}
