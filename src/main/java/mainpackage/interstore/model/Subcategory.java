package mainpackage.interstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "subcategories")
public class Subcategory implements Comparable<Subcategory>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "main_category_id", nullable = false)
    private MainCategory mainCategory;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NestedCategory> nestedCategories = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(MainCategory mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NestedCategory> getNestedCategories() {
        return nestedCategories;
    }

    public void setNestedCategories(List<NestedCategory> nestedCategories) {
        this.nestedCategories = nestedCategories;
    }

    @Override
    public int compareTo(Subcategory o) {
        if (o == null) {
            throw new NullPointerException("Cannot compare with null");
        }
        if (this.id == null || o.id == null) {
            throw new IllegalStateException("Cannot compare entities with null ID");
        }
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return "Subcategory{" +
                "name='" + name + '\'' +
                '}';
    }
}

