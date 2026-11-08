package occ.store;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String category;
    private Double price;
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
}
