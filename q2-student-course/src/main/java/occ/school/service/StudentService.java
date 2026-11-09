package occ.school.service;

import occ.school.model.Student;
import occ.school.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository repository;

    public Student add(Student student) {
        return repository.save(student);
    }

    public Student get(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
