package occ.school.repository;

import java.util.List;
import occ.school.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCreditsGreaterThanAndStudentDepartmentAndStudentCgpaGreaterThan(Integer credits, String department, Double cgpa);
}
