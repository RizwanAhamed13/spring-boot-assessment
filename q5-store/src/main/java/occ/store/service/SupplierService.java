package occ.store.service;

import occ.store.model.Supplier;
import occ.store.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository repository;

    public Supplier add(Supplier supplier) {
        return repository.save(supplier);
    }

    public Supplier get(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
