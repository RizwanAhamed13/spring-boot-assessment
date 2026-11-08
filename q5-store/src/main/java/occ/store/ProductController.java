package occ.store;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository products;
    private final SupplierRepository suppliers;

    @PostMapping("/suppliers")
    Supplier addSupplier(@RequestBody Supplier supplier) {
        return suppliers.save(supplier);
    }

    @PostMapping("/products")
    Product add(@RequestBody Product product) {
        return save(product);
    }

    @GetMapping("/products/{id}")
    Product get(@PathVariable Long id) {
        return products.findById(id).orElseThrow();
    }

    @PutMapping("/products/{id}")
    Product update(@PathVariable Long id, @RequestBody Product product) {
        get(id);
        product.setProductId(id);
        return save(product);
    }

    @DeleteMapping("/products/{id}")
    void delete(@PathVariable Long id) {
        products.deleteById(id);
    }

    @GetMapping("/products/low-stock-laptops")
    List<Product> search() {
        return products.findByCategoryAndStockQuantityLessThanAndSupplierCity("Laptop", 10, "Chennai");
    }

    private Product save(Product product) {
        product.setSupplier(suppliers.findById(product.getSupplier().getSupplierId()).orElseThrow());
        return products.save(product);
    }
}
