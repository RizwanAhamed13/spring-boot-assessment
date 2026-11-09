package occ.school.service;

import java.util.List;
import occ.school.model.Course;
import occ.school.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository repository;
    private final StudentService studentService;

    public Course add(Course course) {
        course.setStudent(studentService.get(course.getStudent().getStudentId()));
        return repository.save(course);
    }

    public Course get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Course update(Long id, Course course) {
        get(id);
        course.setCourseId(id);
        return add(course);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Course> highCreditCseCourses() {
        return repository.findByCreditsGreaterThanAndStudentDepartmentAndStudentCgpaGreaterThan(3, "CSE", 8.0);
    }
}
