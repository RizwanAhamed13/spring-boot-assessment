package occ.restaurant;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final FoodOrderRepository orders;
    private final CustomerRepository customers;

    @PostMapping("/customers")
    Customer addCustomer(@RequestBody Customer customer) {
        return customers.save(customer);
    }

    @PostMapping("/orders")
    FoodOrder add(@RequestBody FoodOrder order) {
        return save(order);
    }

    @GetMapping("/orders/{id}")
    FoodOrder get(@PathVariable Long id) {
        return orders.findById(id).orElseThrow();
    }

    @PutMapping("/orders/{id}")
    FoodOrder update(@PathVariable Long id, @RequestBody FoodOrder order) {
        get(id);
        order.setOrderId(id);
        return save(order);
    }

    @DeleteMapping("/orders/{id}")
    void delete(@PathVariable Long id) {
        orders.deleteById(id);
    }

    @GetMapping("/orders/delivered-coimbatore")
    List<FoodOrder> search() {
        return orders.findByStatusAndTotalAmountGreaterThanAndCustomerCity("Delivered", 1000.0, "Coimbatore");
    }

    private FoodOrder save(FoodOrder order) {
        order.setCustomer(customers.findById(order.getCustomer().getCustomerId()).orElseThrow());
        return orders.save(order);
    }
}
