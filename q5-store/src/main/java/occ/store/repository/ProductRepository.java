package occ.store.repository;

import java.util.List;
import occ.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryAndStockQuantityLessThanAndSupplierCity(String category, Integer stockQuantity, String city);
}
