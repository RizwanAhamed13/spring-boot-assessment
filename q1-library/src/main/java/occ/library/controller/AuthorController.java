package occ.library.controller;

import occ.library.model.Author;
import occ.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService service;

    @PostMapping
    Author add(@RequestBody Author author) {
        return service.add(author);
    }
}
