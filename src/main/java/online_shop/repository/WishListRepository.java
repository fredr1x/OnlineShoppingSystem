package online_shop.repository;

import online_shop.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {

    WishList findById(Long id);
    WishList findByUserId(Long userId);
}
