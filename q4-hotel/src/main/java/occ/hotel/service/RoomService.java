package occ.hotel.service;

import occ.hotel.model.Room;
import occ.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository repository;

    public Room add(Room room) {
        return repository.save(room);
    }

    public Room get(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
