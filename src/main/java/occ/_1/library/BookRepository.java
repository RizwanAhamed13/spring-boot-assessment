package occ._1.library;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategoryAndPriceLessThanAndAuthorCountry(String category, Double price, String country);
}
