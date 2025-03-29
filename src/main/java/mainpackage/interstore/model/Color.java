package mainpackage.interstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "colors")
public class Color implements Comparable<Color>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Must start from capital letter")
    @Column(name = "name")
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_color",
        joinColumns = @JoinColumn(name = "color_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Color{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Color o) {
        if (o == null) {
            throw new NullPointerException("Cannot compare with null");
        }
        if (this.id == null || o.id == null) {
            throw new IllegalStateException("Cannot compare entities with null ID");
        }
        return this.id.compareTo(o.id);
    }

    public void setId(Long id) {
        this.id = id;
    }
}
