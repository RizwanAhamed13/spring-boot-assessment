package occ.library.service;

import occ.library.model.Author;
import occ.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository repository;

    public Author add(Author author) {
        return repository.save(author);
    }

    public Author get(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
