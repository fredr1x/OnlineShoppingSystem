package online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.lang.annotation.Target;
import java.time.Instant;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wish_list_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"wish_list_id", "product_id"}))
public class WishListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private WishList wishList;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "added_at")
    private Instant addedAt;
}
