package online_shop.repository;

import online_shop.dto.ProductDto;
import online_shop.entity.Product;
import online_shop.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    @Query("select p from Product p order by p.rating desc limit 10")
    List<Product> getTop10Products();
}
