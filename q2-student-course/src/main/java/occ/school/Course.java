package occ.school;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String courseName;
    private String department;
    private Integer credits;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
