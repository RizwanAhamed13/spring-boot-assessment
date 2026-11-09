package occ.restaurant.controller;

import occ.restaurant.model.Customer;
import occ.restaurant.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    Customer add(@RequestBody Customer customer) {
        return service.add(customer);
    }
}
