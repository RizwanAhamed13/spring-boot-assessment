package occ.library.repository;

import java.util.List;
import occ.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategoryAndPriceLessThanAndAuthorCountry(String category, Double price, String country);
}
