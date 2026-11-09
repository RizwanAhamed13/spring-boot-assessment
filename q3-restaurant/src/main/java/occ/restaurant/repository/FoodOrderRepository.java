package occ.restaurant.repository;

import java.util.List;
import occ.restaurant.model.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findByStatusAndTotalAmountGreaterThanAndCustomerCity(String status, Double totalAmount, String city);
}
