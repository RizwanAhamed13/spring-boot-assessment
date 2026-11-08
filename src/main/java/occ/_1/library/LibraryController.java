package occ._1.library;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LibraryController {
    private final BookRepository books;
    private final AuthorRepository authors;

    @PostMapping("/authors")
    Author addAuthor(@RequestBody Author author) {
        return authors.save(author);
    }

    @PostMapping("/books")
    Book add(@RequestBody Book book) {
        return save(book);
    }

    @GetMapping("/books/{id}")
    Book get(@PathVariable Long id) {
        return books.findById(id).orElseThrow();
    }

    @PutMapping("/books/{id}")
    Book update(@PathVariable Long id, @RequestBody Book book) {
        get(id);
        book.setBookId(id);
        return save(book);
    }

    @DeleteMapping("/books/{id}")
    void delete(@PathVariable Long id) {
        books.deleteById(id);
    }

    @GetMapping("/books/programming-india")
    List<Book> search() {
        return books.findByCategoryAndPriceLessThanAndAuthorCountry("Programming", 800.0, "India");
    }

    private Book save(Book book) {
        book.setAuthor(authors.findById(book.getAuthor().getAuthorId()).orElseThrow());
        return books.save(book);
    }
}
