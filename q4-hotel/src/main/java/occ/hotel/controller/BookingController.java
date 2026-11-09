package occ.hotel.controller;

import java.util.List;
import occ.hotel.model.Booking;
import occ.hotel.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    Booking add(@RequestBody Booking booking) {
        return service.add(booking);
    }

    @GetMapping("/{id}")
    Booking get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    Booking update(@PathVariable Long id, @RequestBody Booking booking) {
        return service.update(id, booking);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/deluxe-november")
    List<Booking> deluxeBookingsInNovember() {
        return service.deluxeBookingsInNovember();
    }
}
