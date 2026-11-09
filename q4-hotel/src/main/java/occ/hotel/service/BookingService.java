package occ.hotel.service;

import java.time.LocalDate;
import java.util.List;
import occ.hotel.model.Booking;
import occ.hotel.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository repository;
    private final RoomService roomService;

    public Booking add(Booking booking) {
        booking.setRoom(roomService.get(booking.getRoom().getRoomId()));
        return repository.save(booking);
    }

    public Booking get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Booking update(Long id, Booking booking) {
        get(id);
        booking.setBookingId(id);
        return add(booking);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Booking> deluxeBookingsInNovember() {
        return repository.findByRoomRoomTypeAndRoomPricePerNightLessThanAndCheckInDateBetween("Deluxe", 4000.0, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
    }
}
