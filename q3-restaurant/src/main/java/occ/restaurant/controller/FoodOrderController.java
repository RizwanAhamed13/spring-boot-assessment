package occ.restaurant.controller;

import java.util.List;
import occ.restaurant.model.FoodOrder;
import occ.restaurant.service.FoodOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class FoodOrderController {
    private final FoodOrderService service;

    @PostMapping
    FoodOrder add(@RequestBody FoodOrder order) {
        return service.add(order);
    }

    @GetMapping("/{id}")
    FoodOrder get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    FoodOrder update(@PathVariable Long id, @RequestBody FoodOrder order) {
        return service.update(id, order);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/delivered-coimbatore")
    List<FoodOrder> deliveredOrdersFromCoimbatore() {
        return service.deliveredOrdersFromCoimbatore();
    }
}
