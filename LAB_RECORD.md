# Spring Boot Practical Assessment: Lab Record

Five REST applications built with Spring Boot and MySQL. Every application uses the layered MVC structure (model, repository, service, controller). The outputs below are the real responses returned by each running application.


\newpage

# Experiment 1: Library Management System

Books and Authors with a one-to-many relationship (one author, many books).

## Required dependencies

| Item | Version / Artifact |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 (`spring-boot-starter-parent`) |
| Build tool | Maven (wrapper included) |
| `spring-boot-starter-data-jpa` | Spring Data JPA and Hibernate |
| `spring-boot-starter-webmvc` | REST controllers (Spring MVC) |
| `mysql-connector-j` | MySQL JDBC driver (runtime) |
| `lombok` | `@Data` and `@RequiredArgsConstructor` |
| Database | MySQL 9 |
| Test client | Postman |

## Project folder structure

```text
q1-library/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/main/
    ├── java/occ/library/
    │   ├── Application.java
    │   ├── model/
    │   │   ├── Author.java
    │   │   └── Book.java
    │   ├── repository/
    │   │   ├── AuthorRepository.java
    │   │   └── BookRepository.java
    │   ├── service/
    │   │   ├── AuthorService.java
    │   │   └── BookService.java
    │   ├── controller/
    │   │   ├── AuthorController.java
    │   │   └── BookController.java
    │   └── exception/
    │       └── NotFoundHandler.java
    └── resources/
        └── application.properties
```

## Complete Java source files

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>4.1.2</version>
		<relativePath/>
	</parent>
	<groupId>occ</groupId>
	<artifactId>library</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>library</name>
	<properties>
		<java.version>21</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc</artifactId>
		</dependency>
		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

### src/main/resources/application.properties

```properties
spring.application.name=library
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/library_db?createDatabaseIfNotExist=true}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.jpa.hibernate.ddl-auto=update
```

### src/main/java/occ/library/Application.java

```java
package occ.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### src/main/java/occ/library/model/Author.java

```java
package occ.library.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;
    private String authorName;
    private String country;
}
```

### src/main/java/occ/library/model/Book.java

```java
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
```

### src/main/java/occ/library/repository/AuthorRepository.java

```java
package occ.library.repository;

import occ.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
```

### src/main/java/occ/library/repository/BookRepository.java

```java
package occ.library.repository;

import java.util.List;
import occ.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategoryAndPriceLessThanAndAuthorCountry(String category, Double price, String country);
}
```

### src/main/java/occ/library/service/AuthorService.java

```java
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
```

### src/main/java/occ/library/service/BookService.java

```java
package occ.library.service;

import java.util.List;
import occ.library.model.Book;
import occ.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;
    private final AuthorService authorService;

    public Book add(Book book) {
        book.setAuthor(authorService.get(book.getAuthor().getAuthorId()));
        return repository.save(book);
    }

    public Book get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Book update(Long id, Book book) {
        get(id);
        book.setBookId(id);
        return add(book);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Book> programmingBooksByIndianAuthors() {
        return repository.findByCategoryAndPriceLessThanAndAuthorCountry("Programming", 800.0, "India");
    }
}
```

### src/main/java/occ/library/controller/AuthorController.java

```java
package occ.library.controller;

import occ.library.model.Author;
import occ.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService service;

    @PostMapping
    Author add(@RequestBody Author author) {
        return service.add(author);
    }
}
```

### src/main/java/occ/library/controller/BookController.java

```java
package occ.library.controller;

import java.util.List;
import occ.library.model.Book;
import occ.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService service;

    @PostMapping
    Book add(@RequestBody Book book) {
        return service.add(book);
    }

    @GetMapping("/{id}")
    Book get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    Book update(@PathVariable Long id, @RequestBody Book book) {
        return service.update(id, book);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/programming-india")
    List<Book> programmingBooksByIndianAuthors() {
        return service.programmingBooksByIndianAuthors();
    }
}
```

### src/main/java/occ/library/exception/NotFoundHandler.java

```java
package occ.library.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound() {
        return "Not found";
    }
}
```

## Sample API requests executed in Postman and the corresponding outputs

Header for every request with a body: `Content-Type: application/json`.

### 1.1 Create author 1 (India)

**Request:** `POST http://localhost:8080/authors`

Body (raw, JSON):

```json
{
  "authorName": "Rajesh Kumar",
  "country": "India"
}
```

**Response:** `200 OK`

```json
{
  "authorId": 1,
  "authorName": "Rajesh Kumar",
  "country": "India"
}
```

### 1.2 Create a book for author 1 (Programming, 700)

**Request:** `POST http://localhost:8080/books`

Body (raw, JSON):

```json
{
  "title": "Effective Java",
  "category": "Programming",
  "price": 700,
  "author": {
    "authorId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "author": {
    "authorId": 1,
    "authorName": "Rajesh Kumar",
    "country": "India"
  },
  "bookId": 1,
  "category": "Programming",
  "price": 700.0,
  "title": "Effective Java"
}
```

### 1.3 Create a book for author 1 (Programming, 950)

**Request:** `POST http://localhost:8080/books`

Body (raw, JSON):

```json
{
  "title": "Spring in Action",
  "category": "Programming",
  "price": 950,
  "author": {
    "authorId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "author": {
    "authorId": 1,
    "authorName": "Rajesh Kumar",
    "country": "India"
  },
  "bookId": 2,
  "category": "Programming",
  "price": 950.0,
  "title": "Spring in Action"
}
```

### 1.4 Read book 1

**Request:** `GET http://localhost:8080/books/1`

**Response:** `200 OK`

```json
{
  "author": {
    "authorId": 1,
    "authorName": "Rajesh Kumar",
    "country": "India"
  },
  "bookId": 1,
  "category": "Programming",
  "price": 700.0,
  "title": "Effective Java"
}
```

### 1.5 Update book 1

**Request:** `PUT http://localhost:8080/books/1`

Body (raw, JSON):

```json
{
  "title": "Effective Java 2nd Edition",
  "category": "Programming",
  "price": 650,
  "author": {
    "authorId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "author": {
    "authorId": 1,
    "authorName": "Rajesh Kumar",
    "country": "India"
  },
  "bookId": 1,
  "category": "Programming",
  "price": 650.0,
  "title": "Effective Java 2nd Edition"
}
```

### 1.6 Conditional select: Programming books below 800 by authors from India

**Request:** `GET http://localhost:8080/books/programming-india`

**Response:** `200 OK`

```json
[
  {
    "author": {
      "authorId": 1,
      "authorName": "Rajesh Kumar",
      "country": "India"
    },
    "bookId": 1,
    "category": "Programming",
    "price": 650.0,
    "title": "Effective Java 2nd Edition"
  }
]
```

### 1.7 Delete book 2

**Request:** `DELETE http://localhost:8080/books/2`

**Response:** `200 OK`

```text
(empty body)
```


\newpage

# Experiment 2: Student Course Registration System

Students and Courses with a one-to-many relationship (one student, many courses).

## Required dependencies

| Item | Version / Artifact |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 (`spring-boot-starter-parent`) |
| Build tool | Maven (wrapper included) |
| `spring-boot-starter-data-jpa` | Spring Data JPA and Hibernate |
| `spring-boot-starter-webmvc` | REST controllers (Spring MVC) |
| `mysql-connector-j` | MySQL JDBC driver (runtime) |
| `lombok` | `@Data` and `@RequiredArgsConstructor` |
| Database | MySQL 9 |
| Test client | Postman |

## Project folder structure

```text
q2-student-course/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/main/
    ├── java/occ/school/
    │   ├── Application.java
    │   ├── model/
    │   │   ├── Course.java
    │   │   └── Student.java
    │   ├── repository/
    │   │   ├── CourseRepository.java
    │   │   └── StudentRepository.java
    │   ├── service/
    │   │   ├── CourseService.java
    │   │   └── StudentService.java
    │   ├── controller/
    │   │   ├── CourseController.java
    │   │   └── StudentController.java
    │   └── exception/
    │       └── NotFoundHandler.java
    └── resources/
        └── application.properties
```

## Complete Java source files

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>4.1.2</version>
		<relativePath/>
	</parent>
	<groupId>occ</groupId>
	<artifactId>school</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>school</name>
	<properties>
		<java.version>21</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc</artifactId>
		</dependency>
		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

### src/main/resources/application.properties

```properties
spring.application.name=school
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/school_db?createDatabaseIfNotExist=true}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.jpa.hibernate.ddl-auto=update
```

### src/main/java/occ/school/Application.java

```java
package occ.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### src/main/java/occ/school/model/Course.java

```java
package occ.school.model;

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
```

### src/main/java/occ/school/model/Student.java

```java
package occ.school.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    private String studentName;
    private String department;
    private Double cgpa;
}
```

### src/main/java/occ/school/repository/CourseRepository.java

```java
package occ.school.repository;

import java.util.List;
import occ.school.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCreditsGreaterThanAndStudentDepartmentAndStudentCgpaGreaterThan(Integer credits, String department, Double cgpa);
}
```

### src/main/java/occ/school/repository/StudentRepository.java

```java
package occ.school.repository;

import occ.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
```

### src/main/java/occ/school/service/CourseService.java

```java
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
```

### src/main/java/occ/school/service/StudentService.java

```java
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
```

### src/main/java/occ/school/controller/CourseController.java

```java
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
```

### src/main/java/occ/school/controller/StudentController.java

```java
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
```

### src/main/java/occ/school/exception/NotFoundHandler.java

```java
package occ.school.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound() {
        return "Not found";
    }
}
```

## Sample API requests executed in Postman and the corresponding outputs

Header for every request with a body: `Content-Type: application/json`.

### 2.1 Create student 1 (CSE, 8.5)

**Request:** `POST http://localhost:8080/students`

Body (raw, JSON):

```json
{
  "studentName": "Ravi",
  "department": "CSE",
  "cgpa": 8.5
}
```

**Response:** `200 OK`

```json
{
  "cgpa": 8.5,
  "department": "CSE",
  "studentId": 1,
  "studentName": "Ravi"
}
```

### 2.2 Register a 4-credit course for student 1

**Request:** `POST http://localhost:8080/courses`

Body (raw, JSON):

```json
{
  "courseName": "Artificial Intelligence",
  "department": "CSE",
  "credits": 4,
  "student": {
    "studentId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "courseId": 1,
  "courseName": "Artificial Intelligence",
  "credits": 4,
  "department": "CSE",
  "student": {
    "cgpa": 8.5,
    "department": "CSE",
    "studentId": 1,
    "studentName": "Ravi"
  }
}
```

### 2.3 Register a 3-credit course for student 1

**Request:** `POST http://localhost:8080/courses`

Body (raw, JSON):

```json
{
  "courseName": "Data Structures",
  "department": "CSE",
  "credits": 3,
  "student": {
    "studentId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "courseId": 2,
  "courseName": "Data Structures",
  "credits": 3,
  "department": "CSE",
  "student": {
    "cgpa": 8.5,
    "department": "CSE",
    "studentId": 1,
    "studentName": "Ravi"
  }
}
```

### 2.4 Read course 1

**Request:** `GET http://localhost:8080/courses/1`

**Response:** `200 OK`

```json
{
  "courseId": 1,
  "courseName": "Artificial Intelligence",
  "credits": 4,
  "department": "CSE",
  "student": {
    "cgpa": 8.5,
    "department": "CSE",
    "studentId": 1,
    "studentName": "Ravi"
  }
}
```

### 2.5 Update course 1

**Request:** `PUT http://localhost:8080/courses/1`

Body (raw, JSON):

```json
{
  "courseName": "Artificial Intelligence Advanced",
  "department": "CSE",
  "credits": 4,
  "student": {
    "studentId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "courseId": 1,
  "courseName": "Artificial Intelligence Advanced",
  "credits": 4,
  "department": "CSE",
  "student": {
    "cgpa": 8.5,
    "department": "CSE",
    "studentId": 1,
    "studentName": "Ravi"
  }
}
```

### 2.6 Conditional select: courses above 3 credits registered by CSE students with CGPA above 8.0

**Request:** `GET http://localhost:8080/courses/cse-high-credit`

**Response:** `200 OK`

```json
[
  {
    "courseId": 1,
    "courseName": "Artificial Intelligence Advanced",
    "credits": 4,
    "department": "CSE",
    "student": {
      "cgpa": 8.5,
      "department": "CSE",
      "studentId": 1,
      "studentName": "Ravi"
    }
  }
]
```

### 2.7 Delete course registration 2

**Request:** `DELETE http://localhost:8080/courses/2`

**Response:** `200 OK`

```text
(empty body)
```


\newpage

# Experiment 3: Restaurant Order Management System

Customers and Food Orders with a one-to-many relationship (one customer, many orders).

## Required dependencies

| Item | Version / Artifact |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 (`spring-boot-starter-parent`) |
| Build tool | Maven (wrapper included) |
| `spring-boot-starter-data-jpa` | Spring Data JPA and Hibernate |
| `spring-boot-starter-webmvc` | REST controllers (Spring MVC) |
| `mysql-connector-j` | MySQL JDBC driver (runtime) |
| `lombok` | `@Data` and `@RequiredArgsConstructor` |
| Database | MySQL 9 |
| Test client | Postman |

## Project folder structure

```text
q3-restaurant/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/main/
    ├── java/occ/restaurant/
    │   ├── Application.java
    │   ├── model/
    │   │   ├── Customer.java
    │   │   └── FoodOrder.java
    │   ├── repository/
    │   │   ├── CustomerRepository.java
    │   │   └── FoodOrderRepository.java
    │   ├── service/
    │   │   ├── CustomerService.java
    │   │   └── FoodOrderService.java
    │   ├── controller/
    │   │   ├── CustomerController.java
    │   │   └── FoodOrderController.java
    │   └── exception/
    │       └── NotFoundHandler.java
    └── resources/
        └── application.properties
```

## Complete Java source files

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>4.1.2</version>
		<relativePath/>
	</parent>
	<groupId>occ</groupId>
	<artifactId>restaurant</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>restaurant</name>
	<properties>
		<java.version>21</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc</artifactId>
		</dependency>
		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

### src/main/resources/application.properties

```properties
spring.application.name=restaurant
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/restaurant_db?createDatabaseIfNotExist=true}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.jpa.hibernate.ddl-auto=update
```

### src/main/java/occ/restaurant/Application.java

```java
package occ.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### src/main/java/occ/restaurant/model/Customer.java

```java
package occ.restaurant.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String customerName;
    private String city;
    private String phone;
}
```

### src/main/java/occ/restaurant/model/FoodOrder.java

```java
package occ.restaurant.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private LocalDate orderDate;
    private Double totalAmount;
    private String status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
```

### src/main/java/occ/restaurant/repository/CustomerRepository.java

```java
package occ.restaurant.repository;

import occ.restaurant.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
```

### src/main/java/occ/restaurant/repository/FoodOrderRepository.java

```java
package occ.restaurant.repository;

import java.util.List;
import occ.restaurant.model.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findByStatusAndTotalAmountGreaterThanAndCustomerCity(String status, Double totalAmount, String city);
}
```

### src/main/java/occ/restaurant/service/CustomerService.java

```java
package occ.restaurant.service;

import occ.restaurant.model.Customer;
import occ.restaurant.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;

    public Customer add(Customer customer) {
        return repository.save(customer);
    }

    public Customer get(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
```

### src/main/java/occ/restaurant/service/FoodOrderService.java

```java
package occ.restaurant.service;

import java.util.List;
import occ.restaurant.model.FoodOrder;
import occ.restaurant.repository.FoodOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodOrderService {
    private final FoodOrderRepository repository;
    private final CustomerService customerService;

    public FoodOrder add(FoodOrder order) {
        order.setCustomer(customerService.get(order.getCustomer().getCustomerId()));
        return repository.save(order);
    }

    public FoodOrder get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public FoodOrder update(Long id, FoodOrder order) {
        get(id);
        order.setOrderId(id);
        return add(order);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<FoodOrder> deliveredOrdersFromCoimbatore() {
        return repository.findByStatusAndTotalAmountGreaterThanAndCustomerCity("Delivered", 1000.0, "Coimbatore");
    }
}
```

### src/main/java/occ/restaurant/controller/CustomerController.java

```java
package occ.restaurant.controller;

import occ.restaurant.model.Customer;
import occ.restaurant.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    Customer add(@RequestBody Customer customer) {
        return service.add(customer);
    }
}
```

### src/main/java/occ/restaurant/controller/FoodOrderController.java

```java
package occ.restaurant.controller;

import java.util.List;
import occ.restaurant.model.FoodOrder;
import occ.restaurant.service.FoodOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class FoodOrderController {
    private final FoodOrderService service;

    @PostMapping
    FoodOrder add(@RequestBody FoodOrder order) {
        return service.add(order);
    }

    @GetMapping("/{id}")
    FoodOrder get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    FoodOrder update(@PathVariable Long id, @RequestBody FoodOrder order) {
        return service.update(id, order);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/delivered-coimbatore")
    List<FoodOrder> deliveredOrdersFromCoimbatore() {
        return service.deliveredOrdersFromCoimbatore();
    }
}
```

### src/main/java/occ/restaurant/exception/NotFoundHandler.java

```java
package occ.restaurant.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound() {
        return "Not found";
    }
}
```

## Sample API requests executed in Postman and the corresponding outputs

Header for every request with a body: `Content-Type: application/json`.

### 3.1 Create customer 1 (Coimbatore)

**Request:** `POST http://localhost:8080/customers`

Body (raw, JSON):

```json
{
  "customerName": "Anu",
  "city": "Coimbatore",
  "phone": "9876543210"
}
```

**Response:** `200 OK`

```json
{
  "city": "Coimbatore",
  "customerId": 1,
  "customerName": "Anu",
  "phone": "9876543210"
}
```

### 3.2 Create a Delivered order of 1500 for customer 1

**Request:** `POST http://localhost:8080/orders`

Body (raw, JSON):

```json
{
  "orderDate": "2026-11-05",
  "totalAmount": 1500,
  "status": "Delivered",
  "customer": {
    "customerId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "customer": {
    "city": "Coimbatore",
    "customerId": 1,
    "customerName": "Anu",
    "phone": "9876543210"
  },
  "orderDate": "2026-11-05",
  "orderId": 1,
  "status": "Delivered",
  "totalAmount": 1500.0
}
```

### 3.3 Create a Delivered order of 800 for customer 1

**Request:** `POST http://localhost:8080/orders`

Body (raw, JSON):

```json
{
  "orderDate": "2026-11-06",
  "totalAmount": 800,
  "status": "Delivered",
  "customer": {
    "customerId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "customer": {
    "city": "Coimbatore",
    "customerId": 1,
    "customerName": "Anu",
    "phone": "9876543210"
  },
  "orderDate": "2026-11-06",
  "orderId": 2,
  "status": "Delivered",
  "totalAmount": 800.0
}
```

### 3.4 Read order 1

**Request:** `GET http://localhost:8080/orders/1`

**Response:** `200 OK`

```json
{
  "customer": {
    "city": "Coimbatore",
    "customerId": 1,
    "customerName": "Anu",
    "phone": "9876543210"
  },
  "orderDate": "2026-11-05",
  "orderId": 1,
  "status": "Delivered",
  "totalAmount": 1500.0
}
```

### 3.5 Update order 1

**Request:** `PUT http://localhost:8080/orders/1`

Body (raw, JSON):

```json
{
  "orderDate": "2026-11-05",
  "totalAmount": 1600,
  "status": "Delivered",
  "customer": {
    "customerId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "customer": {
    "city": "Coimbatore",
    "customerId": 1,
    "customerName": "Anu",
    "phone": "9876543210"
  },
  "orderDate": "2026-11-05",
  "orderId": 1,
  "status": "Delivered",
  "totalAmount": 1600.0
}
```

### 3.6 Conditional select: delivered orders above 1000 by customers from Coimbatore

**Request:** `GET http://localhost:8080/orders/delivered-coimbatore`

**Response:** `200 OK`

```json
[
  {
    "customer": {
      "city": "Coimbatore",
      "customerId": 1,
      "customerName": "Anu",
      "phone": "9876543210"
    },
    "orderDate": "2026-11-05",
    "orderId": 1,
    "status": "Delivered",
    "totalAmount": 1600.0
  }
]
```

### 3.7 Delete order 2

**Request:** `DELETE http://localhost:8080/orders/2`

**Response:** `200 OK`

```text
(empty body)
```


\newpage

# Experiment 4: Hotel Booking Management System

Rooms and Bookings with a one-to-many relationship (one room, many bookings).

## Required dependencies

| Item | Version / Artifact |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 (`spring-boot-starter-parent`) |
| Build tool | Maven (wrapper included) |
| `spring-boot-starter-data-jpa` | Spring Data JPA and Hibernate |
| `spring-boot-starter-webmvc` | REST controllers (Spring MVC) |
| `mysql-connector-j` | MySQL JDBC driver (runtime) |
| `lombok` | `@Data` and `@RequiredArgsConstructor` |
| Database | MySQL 9 |
| Test client | Postman |

## Project folder structure

```text
q4-hotel/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/main/
    ├── java/occ/hotel/
    │   ├── Application.java
    │   ├── model/
    │   │   ├── Booking.java
    │   │   └── Room.java
    │   ├── repository/
    │   │   ├── BookingRepository.java
    │   │   └── RoomRepository.java
    │   ├── service/
    │   │   ├── BookingService.java
    │   │   └── RoomService.java
    │   ├── controller/
    │   │   ├── BookingController.java
    │   │   └── RoomController.java
    │   └── exception/
    │       └── NotFoundHandler.java
    └── resources/
        └── application.properties
```

## Complete Java source files

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>4.1.2</version>
		<relativePath/>
	</parent>
	<groupId>occ</groupId>
	<artifactId>hotel</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>hotel</name>
	<properties>
		<java.version>21</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc</artifactId>
		</dependency>
		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

### src/main/resources/application.properties

```properties
spring.application.name=hotel
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/hotel_db?createDatabaseIfNotExist=true}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.jpa.hibernate.ddl-auto=update
```

### src/main/java/occ/hotel/Application.java

```java
package occ.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### src/main/java/occ/hotel/model/Booking.java

```java
package occ.hotel.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
```

### src/main/java/occ/hotel/model/Room.java

```java
package occ.hotel.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private Double pricePerNight;
}
```

### src/main/java/occ/hotel/repository/BookingRepository.java

```java
package occ.hotel.repository;

import java.time.LocalDate;
import java.util.List;
import occ.hotel.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRoomRoomTypeAndRoomPricePerNightLessThanAndCheckInDateBetween(String roomType, Double pricePerNight, LocalDate from, LocalDate to);
}
```

### src/main/java/occ/hotel/repository/RoomRepository.java

```java
package occ.hotel.repository;

import occ.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
```

### src/main/java/occ/hotel/service/BookingService.java

```java
package occ.hotel.service;

import java.time.LocalDate;
import java.util.List;
import occ.hotel.model.Booking;
import occ.hotel.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository repository;
    private final RoomService roomService;

    public Booking add(Booking booking) {
        booking.setRoom(roomService.get(booking.getRoom().getRoomId()));
        return repository.save(booking);
    }

    public Booking get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Booking update(Long id, Booking booking) {
        get(id);
        booking.setBookingId(id);
        return add(booking);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Booking> deluxeBookingsInNovember() {
        return repository.findByRoomRoomTypeAndRoomPricePerNightLessThanAndCheckInDateBetween("Deluxe", 4000.0, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
    }
}
```

### src/main/java/occ/hotel/service/RoomService.java

```java
package occ.hotel.service;

import occ.hotel.model.Room;
import occ.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository repository;

    public Room add(Room room) {
        return repository.save(room);
    }

    public Room get(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
```

### src/main/java/occ/hotel/controller/BookingController.java

```java
package occ.hotel.controller;

import java.util.List;
import occ.hotel.model.Booking;
import occ.hotel.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    Booking add(@RequestBody Booking booking) {
        return service.add(booking);
    }

    @GetMapping("/{id}")
    Booking get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    Booking update(@PathVariable Long id, @RequestBody Booking booking) {
        return service.update(id, booking);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/deluxe-november")
    List<Booking> deluxeBookingsInNovember() {
        return service.deluxeBookingsInNovember();
    }
}
```

### src/main/java/occ/hotel/controller/RoomController.java

```java
package occ.hotel.controller;

import occ.hotel.model.Room;
import occ.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService service;

    @PostMapping
    Room add(@RequestBody Room room) {
        return service.add(room);
    }
}
```

### src/main/java/occ/hotel/exception/NotFoundHandler.java

```java
package occ.hotel.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound() {
        return "Not found";
    }
}
```

## Sample API requests executed in Postman and the corresponding outputs

Header for every request with a body: `Content-Type: application/json`.

### 4.1 Create room 1 (Deluxe, 3500)

**Request:** `POST http://localhost:8080/rooms`

Body (raw, JSON):

```json
{
  "roomNumber": "101",
  "roomType": "Deluxe",
  "pricePerNight": 3500
}
```

**Response:** `200 OK`

```json
{
  "pricePerNight": 3500.0,
  "roomId": 1,
  "roomNumber": "101",
  "roomType": "Deluxe"
}
```

### 4.2 Book room 1 on 10 Nov 2026

**Request:** `POST http://localhost:8080/bookings`

Body (raw, JSON):

```json
{
  "guestName": "Sam",
  "checkInDate": "2026-11-10",
  "checkOutDate": "2026-11-12",
  "room": {
    "roomId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "bookingId": 1,
  "checkInDate": "2026-11-10",
  "checkOutDate": "2026-11-12",
  "guestName": "Sam",
  "room": {
    "pricePerNight": 3500.0,
    "roomId": 1,
    "roomNumber": "101",
    "roomType": "Deluxe"
  }
}
```

### 4.3 Read booking 1

**Request:** `GET http://localhost:8080/bookings/1`

**Response:** `200 OK`

```json
{
  "bookingId": 1,
  "checkInDate": "2026-11-10",
  "checkOutDate": "2026-11-12",
  "guestName": "Sam",
  "room": {
    "pricePerNight": 3500.0,
    "roomId": 1,
    "roomNumber": "101",
    "roomType": "Deluxe"
  }
}
```

### 4.4 Update booking 1

**Request:** `PUT http://localhost:8080/bookings/1`

Body (raw, JSON):

```json
{
  "guestName": "Sam Kumar",
  "checkInDate": "2026-11-10",
  "checkOutDate": "2026-11-13",
  "room": {
    "roomId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "bookingId": 1,
  "checkInDate": "2026-11-10",
  "checkOutDate": "2026-11-13",
  "guestName": "Sam Kumar",
  "room": {
    "pricePerNight": 3500.0,
    "roomId": 1,
    "roomNumber": "101",
    "roomType": "Deluxe"
  }
}
```

### 4.5 Conditional select: Deluxe rooms below 4000 per night, check-in 1 to 30 November 2026

**Request:** `GET http://localhost:8080/bookings/deluxe-november`

**Response:** `200 OK`

```json
[
  {
    "bookingId": 1,
    "checkInDate": "2026-11-10",
    "checkOutDate": "2026-11-13",
    "guestName": "Sam Kumar",
    "room": {
      "pricePerNight": 3500.0,
      "roomId": 1,
      "roomNumber": "101",
      "roomType": "Deluxe"
    }
  }
]
```

### 4.6 Delete booking 1

**Request:** `DELETE http://localhost:8080/bookings/1`

**Response:** `200 OK`

```text
(empty body)
```


\newpage

# Experiment 5: Electronics Store Inventory Management System

Suppliers and Products with a one-to-many relationship (one supplier, many products). Suppliers 1 (Chennai) and 2 (Mumbai) are inserted automatically from data.sql, so there is no supplier API.

## Required dependencies

| Item | Version / Artifact |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 (`spring-boot-starter-parent`) |
| Build tool | Maven (wrapper included) |
| `spring-boot-starter-data-jpa` | Spring Data JPA and Hibernate |
| `spring-boot-starter-webmvc` | REST controllers (Spring MVC) |
| `mysql-connector-j` | MySQL JDBC driver (runtime) |
| `lombok` | `@Data` and `@RequiredArgsConstructor` |
| Database | MySQL 9 |
| Test client | Postman |

## Project folder structure

```text
q5-store/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/main/
    ├── java/occ/store/
    │   ├── Application.java
    │   ├── model/
    │   │   ├── Product.java
    │   │   └── Supplier.java
    │   ├── repository/
    │   │   └── ProductRepository.java
    │   ├── service/
    │   │   └── ProductService.java
    │   ├── controller/
    │   │   └── ProductController.java
    │   └── exception/
    │       └── NotFoundHandler.java
    └── resources/
        ├── application.properties
        └── data.sql
```

## Complete Java source files

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>4.1.2</version>
		<relativePath/>
	</parent>
	<groupId>occ</groupId>
	<artifactId>store</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>store</name>
	<properties>
		<java.version>21</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc</artifactId>
		</dependency>
		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

### src/main/resources/application.properties

```properties
spring.application.name=store
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/store_db?createDatabaseIfNotExist=true}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
```

### src/main/resources/data.sql

```sql
INSERT IGNORE INTO supplier (supplier_id, supplier_name, city, rating) VALUES
(1, 'TechWorld', 'Chennai', 4.5),
(2, 'GadgetHub', 'Mumbai', 4.0);
```

### src/main/java/occ/store/Application.java

```java
package occ.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### src/main/java/occ/store/model/Product.java

```java
package occ.store.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String category;
    private Double price;
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
}
```

### src/main/java/occ/store/model/Supplier.java

```java
package occ.store.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;
    private String supplierName;
    private String city;
    private Double rating;
}
```

### src/main/java/occ/store/repository/ProductRepository.java

```java
package occ.store.repository;

import java.util.List;
import occ.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryAndStockQuantityLessThanAndSupplierCity(String category, Integer stockQuantity, String city);
}
```

### src/main/java/occ/store/service/ProductService.java

```java
package occ.store.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import occ.store.model.Product;
import occ.store.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public Product add(Product product) {
        return repository.save(product);
    }

    public Product get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Product update(Long id, Product product) {
        get(id);
        product.setProductId(id);
        return repository.save(product);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Product> lowStockLaptopsFromChennai() {
        return repository.findByCategoryAndStockQuantityLessThanAndSupplierCity("Laptop", 10, "Chennai");
    }
}
```

### src/main/java/occ/store/controller/ProductController.java

```java
package occ.store.controller;

import java.util.List;
import occ.store.model.Product;
import occ.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    Product add(@RequestBody Product product) {
        return service.add(product);
    }

    @GetMapping("/{id}")
    Product get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    Product update(@PathVariable Long id, @RequestBody Product product) {
        return service.update(id, product);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/low-stock-laptops")
    List<Product> lowStockLaptopsFromChennai() {
        return service.lowStockLaptopsFromChennai();
    }
}
```

### src/main/java/occ/store/exception/NotFoundHandler.java

```java
package occ.store.exception;

import java.util.NoSuchElementException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound() {
        return "Not found";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badSupplier() {
        return "Supplier does not exist";
    }
}
```

## Sample API requests executed in Postman and the corresponding outputs

Header for every request with a body: `Content-Type: application/json`.

### 5.1 Add a laptop with 5 units from supplier 1

**Request:** `POST http://localhost:8080/products`

Body (raw, JSON):

```json
{
  "productName": "Dell Inspiron",
  "category": "Laptop",
  "price": 55000,
  "stockQuantity": 5,
  "supplier": {
    "supplierId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "category": "Laptop",
  "price": 55000.0,
  "productId": 1,
  "productName": "Dell Inspiron",
  "stockQuantity": 5,
  "supplier": {
    "city": null,
    "rating": null,
    "supplierId": 1,
    "supplierName": null
  }
}
```

### 5.2 Add a laptop with 25 units from supplier 1

**Request:** `POST http://localhost:8080/products`

Body (raw, JSON):

```json
{
  "productName": "HP Pavilion",
  "category": "Laptop",
  "price": 62000,
  "stockQuantity": 25,
  "supplier": {
    "supplierId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "category": "Laptop",
  "price": 62000.0,
  "productId": 2,
  "productName": "HP Pavilion",
  "stockQuantity": 25,
  "supplier": {
    "city": null,
    "rating": null,
    "supplierId": 1,
    "supplierName": null
  }
}
```

### 5.3 Read product 1

**Request:** `GET http://localhost:8080/products/1`

**Response:** `200 OK`

```json
{
  "category": "Laptop",
  "price": 55000.0,
  "productId": 1,
  "productName": "Dell Inspiron",
  "stockQuantity": 5,
  "supplier": {
    "city": "Chennai",
    "rating": 4.5,
    "supplierId": 1,
    "supplierName": "TechWorld"
  }
}
```

### 5.4 Update product 1

**Request:** `PUT http://localhost:8080/products/1`

Body (raw, JSON):

```json
{
  "productName": "Dell Inspiron",
  "category": "Laptop",
  "price": 54000,
  "stockQuantity": 4,
  "supplier": {
    "supplierId": 1
  }
}
```

**Response:** `200 OK`

```json
{
  "category": "Laptop",
  "price": 54000.0,
  "productId": 1,
  "productName": "Dell Inspiron",
  "stockQuantity": 4,
  "supplier": {
    "city": "Chennai",
    "rating": 4.5,
    "supplierId": 1,
    "supplierName": "TechWorld"
  }
}
```

### 5.5 Conditional select: laptops with fewer than 10 units supplied from Chennai

**Request:** `GET http://localhost:8080/products/low-stock-laptops`

**Response:** `200 OK`

```json
[
  {
    "category": "Laptop",
    "price": 54000.0,
    "productId": 1,
    "productName": "Dell Inspiron",
    "stockQuantity": 4,
    "supplier": {
      "city": "Chennai",
      "rating": 4.5,
      "supplierId": 1,
      "supplierName": "TechWorld"
    }
  }
]
```

### 5.6 Delete product 2

**Request:** `DELETE http://localhost:8080/products/2`

**Response:** `200 OK`

```text
(empty body)
```
