package online_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import online_shop.entity.enums.Category;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;


}
