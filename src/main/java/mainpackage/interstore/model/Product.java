package mainpackage.interstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 8, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "brand")
    private String brand;

    @ManyToOne
    @JoinColumn(name = "nested_category_id")
    private NestedCategory nestedCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "product_color",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id"))
    private List<Color> colors;

}