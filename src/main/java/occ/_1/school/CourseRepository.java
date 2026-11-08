package occ._1.school;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCreditsGreaterThanAndStudentDepartmentAndStudentCgpaGreaterThan(Integer credits, String department, Double cgpa);
}
