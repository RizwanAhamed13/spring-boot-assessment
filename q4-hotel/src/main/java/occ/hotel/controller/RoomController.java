package occ.hotel.controller;

import occ.hotel.model.Room;
import occ.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService service;

    @PostMapping
    Room add(@RequestBody Room room) {
        return service.add(room);
    }
}
