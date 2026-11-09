package occ.library.service;

import java.util.List;
import occ.library.model.Book;
import occ.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;
    private final AuthorService authorService;

    public Book add(Book book) {
        book.setAuthor(authorService.get(book.getAuthor().getAuthorId()));
        return repository.save(book);
    }

    public Book get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Book update(Long id, Book book) {
        get(id);
        book.setBookId(id);
        return add(book);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Book> programmingBooksByIndianAuthors() {
        return repository.findByCategoryAndPriceLessThanAndAuthorCountry("Programming", 800.0, "India");
    }
}
