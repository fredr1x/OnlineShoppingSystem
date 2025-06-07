package online_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"cart", "wishList", "reviewList", "orders", "userRoles"})
public class User {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registered_at")
    private Instant registeredAt;

    @Column(name = "modified_at")
    private Instant modifiedAt;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private WishList wishList;

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoles> userRoles = new HashSet<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public void setRole(UserRoles role) {
        userRoles.add(role);
        role.setUser(this);
    }

    public void setReview(Review review) {
        reviewList.add(review);
        review.setUser(this);
    }

    public void setOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }
}
