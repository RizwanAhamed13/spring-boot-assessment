package occ.library.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    private String title;
    private String category;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
