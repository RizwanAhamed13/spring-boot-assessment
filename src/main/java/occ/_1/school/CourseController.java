package occ._1.school;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseRepository courses;
    private final StudentRepository students;

    @PostMapping("/students")
    Student addStudent(@RequestBody Student student) {
        return students.save(student);
    }

    @PostMapping("/courses")
    Course add(@RequestBody Course course) {
        return save(course);
    }

    @GetMapping("/courses/{id}")
    Course get(@PathVariable Long id) {
        return courses.findById(id).orElseThrow();
    }

    @PutMapping("/courses/{id}")
    Course update(@PathVariable Long id, @RequestBody Course course) {
        get(id);
        course.setCourseId(id);
        return save(course);
    }

    @DeleteMapping("/courses/{id}")
    void delete(@PathVariable Long id) {
        courses.deleteById(id);
    }

    @GetMapping("/courses/cse-high-credit")
    List<Course> search() {
        return courses.findByCreditsGreaterThanAndStudentDepartmentAndStudentCgpaGreaterThan(3, "CSE", 8.0);
    }

    private Course save(Course course) {
        course.setStudent(students.findById(course.getStudent().getStudentId()).orElseThrow());
        return courses.save(course);
    }
}
