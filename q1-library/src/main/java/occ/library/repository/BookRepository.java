package occ.library.repository;

import java.util.List;
import occ.library.model.Book;
import occ.library.model.BookSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategoryAndPriceLessThanAndAuthorCountry(String category, Double price, String country);

    @Query("""
            select new occ.library.model.BookSummary(b.title, b.author.authorName, b.price)
            from Book b
            where b.category = :category
              and b.price = (select max(x.price) from Book x where x.category = :category)
            """)
    List<BookSummary> findMostExpensiveByCategory(@Param("category") String category);
}
