package occ._1.hotel;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRoomRoomTypeAndRoomPricePerNightLessThanAndCheckInDateBetween(String roomType, Double pricePerNight, LocalDate from, LocalDate to);
}
