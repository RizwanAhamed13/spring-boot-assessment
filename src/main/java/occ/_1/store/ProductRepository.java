package occ._1.store;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryAndStockQuantityLessThanAndSupplierCity(String category, Integer stockQuantity, String city);
}
