package occ.restaurant.service;

import occ.restaurant.model.Customer;
import occ.restaurant.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;

    public Customer add(Customer customer) {
        return repository.save(customer);
    }

    public Customer get(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
