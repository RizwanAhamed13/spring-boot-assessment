package occ.library.controller;

import java.util.List;
import occ.library.model.Book;
import occ.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService service;

    @PostMapping
    Book add(@RequestBody Book book) {
        return service.add(book);
    }

    @GetMapping("/{id}")
    Book get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    Book update(@PathVariable Long id, @RequestBody Book book) {
        return service.update(id, book);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/programming-india")
    List<Book> programmingBooksByIndianAuthors() {
        return service.programmingBooksByIndianAuthors();
    }
}
