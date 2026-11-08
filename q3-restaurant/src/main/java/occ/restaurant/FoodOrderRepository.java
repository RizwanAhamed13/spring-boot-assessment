package occ.restaurant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findByStatusAndTotalAmountGreaterThanAndCustomerCity(String status, Double totalAmount, String city);
}
