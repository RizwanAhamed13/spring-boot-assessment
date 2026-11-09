package occ.store.service;

import java.util.List;
import occ.store.model.Product;
import occ.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final SupplierService supplierService;

    public Product add(Product product) {
        product.setSupplier(supplierService.get(product.getSupplier().getSupplierId()));
        return repository.save(product);
    }

    public Product get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Product update(Long id, Product product) {
        get(id);
        product.setProductId(id);
        return add(product);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Product> lowStockLaptopsFromChennai() {
        return repository.findByCategoryAndStockQuantityLessThanAndSupplierCity("Laptop", 10, "Chennai");
    }
}
