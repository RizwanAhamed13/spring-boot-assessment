package occ.store.controller;

import java.util.List;
import occ.store.model.Product;
import occ.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    Product add(@RequestBody Product product) {
        return service.add(product);
    }

    @GetMapping("/{id}")
    Product get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    Product update(@PathVariable Long id, @RequestBody Product product) {
        return service.update(id, product);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/low-stock-laptops")
    List<Product> lowStockLaptopsFromChennai() {
        return service.lowStockLaptopsFromChennai();
    }
}
