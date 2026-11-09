package occ.store.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import occ.store.model.Product;
import occ.store.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public Product add(Product product) {
        return repository.save(product);
    }

    public Product get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Product update(Long id, Product product) {
        get(id);
        product.setProductId(id);
        return repository.save(product);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Product> lowStockLaptopsFromChennai() {
        return repository.findByCategoryAndStockQuantityLessThanAndSupplierCity("Laptop", 10, "Chennai");
    }
}
