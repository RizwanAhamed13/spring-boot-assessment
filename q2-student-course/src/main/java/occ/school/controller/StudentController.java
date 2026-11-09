package occ.school.controller;

import occ.school.model.Student;
import occ.school.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService service;

    @PostMapping
    Student add(@RequestBody Student student) {
        return service.add(student);
    }
}
