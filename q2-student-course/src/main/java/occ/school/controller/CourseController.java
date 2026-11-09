package occ.school.controller;

import java.util.List;
import occ.school.model.Course;
import occ.school.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;

    @PostMapping
    Course add(@RequestBody Course course) {
        return service.add(course);
    }

    @GetMapping("/{id}")
    Course get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    Course update(@PathVariable Long id, @RequestBody Course course) {
        return service.update(id, course);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/cse-high-credit")
    List<Course> highCreditCseCourses() {
        return service.highCreditCseCourses();
    }
}
