package occ.store.controller;

import occ.store.model.Supplier;
import occ.store.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService service;

    @PostMapping
    Supplier add(@RequestBody Supplier supplier) {
        return service.add(supplier);
    }
}
