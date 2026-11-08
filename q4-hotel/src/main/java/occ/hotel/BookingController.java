package occ.hotel;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookingController {
    private final BookingRepository bookings;
    private final RoomRepository rooms;

    @PostMapping("/rooms")
    Room addRoom(@RequestBody Room room) {
        return rooms.save(room);
    }

    @PostMapping("/bookings")
    Booking add(@RequestBody Booking booking) {
        return save(booking);
    }

    @GetMapping("/bookings/{id}")
    Booking get(@PathVariable Long id) {
        return bookings.findById(id).orElseThrow();
    }

    @PutMapping("/bookings/{id}")
    Booking update(@PathVariable Long id, @RequestBody Booking booking) {
        get(id);
        booking.setBookingId(id);
        return save(booking);
    }

    @DeleteMapping("/bookings/{id}")
    void delete(@PathVariable Long id) {
        bookings.deleteById(id);
    }

    @GetMapping("/bookings/deluxe-november")
    List<Booking> search() {
        return bookings.findByRoomRoomTypeAndRoomPricePerNightLessThanAndCheckInDateBetween("Deluxe", 4000.0, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
    }

    private Booking save(Booking booking) {
        booking.setRoom(rooms.findById(booking.getRoom().getRoomId()).orElseThrow());
        return bookings.save(booking);
    }
}
