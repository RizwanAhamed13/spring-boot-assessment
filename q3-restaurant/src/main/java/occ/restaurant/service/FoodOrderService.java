package occ.restaurant.service;

import java.util.List;
import occ.restaurant.model.FoodOrder;
import occ.restaurant.repository.FoodOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodOrderService {
    private final FoodOrderRepository repository;
    private final CustomerService customerService;

    public FoodOrder add(FoodOrder order) {
        order.setCustomer(customerService.get(order.getCustomer().getCustomerId()));
        return repository.save(order);
    }

    public FoodOrder get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public FoodOrder update(Long id, FoodOrder order) {
        get(id);
        order.setOrderId(id);
        return add(order);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<FoodOrder> deliveredOrdersFromCoimbatore() {
        return repository.findByStatusAndTotalAmountGreaterThanAndCustomerCity("Delivered", 1000.0, "Coimbatore");
    }
}
