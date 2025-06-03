package online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wish_list",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WishList {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "added_at", nullable = false)
    private Instant addedAt;
}
