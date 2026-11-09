# Spring Boot Practical Assessment

Five separate Spring Boot apps (Java 21, Maven, MySQL), one per question. Each has its own `pom.xml`, Maven wrapper and database, so you can open, build and run any of them on its own.

| Q | Folder | Entities | Database | Conditional endpoint |
|---|--------|----------|----------|----------------------|
| 1 | `q1-library` | Author, Book | `library_db` | `GET /books/programming-india` |
| 2 | `q2-student-course` | Student, Course | `school_db` | `GET /courses/cse-high-credit` |
| 3 | `q3-restaurant` | Customer, FoodOrder | `restaurant_db` | `GET /orders/delivered-coimbatore` |
| 4 | `q4-hotel` | Room, Booking | `hotel_db` | `GET /bookings/deluxe-november` |
| 5 | `q5-store` | Supplier, Product | `store_db` | `GET /products/low-stock-laptops` |

## Architecture

Each app follows the same layered MVC structure. The model is the JPA entities, and the view is the JSON the controllers return.

```
occ/<name>/
├── Application.java
├── model/        entities (Author, Book, ...)
├── repository/   Spring Data JPA interfaces, including the conditional query
├── service/      business logic: parent lookup, update, search
├── controller/   REST endpoints, call the service only
└── exception/    NotFoundHandler: turns a missing id into 404
```

A request flows `controller -> service -> repository -> MySQL`. Controllers never touch repositories, and the child service calls the parent service to check that the referenced parent exists.

## Run one app

1. Start MySQL. The database is created automatically on first start.
2. Set your MySQL login (defaults are user `root`, password `root`):

```bash
export DB_USER=root
export DB_PASSWORD=your_password
```

3. Start the app from its folder:

```bash
cd q1-library
./mvnw spring-boot:run
```

It runs on http://localhost:8080. To run several at once, give each its own port:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

## API pattern

All five apps have the same endpoints. Create the parent first, then the child that points to it by id.

| Operation | Request |
|-----------|---------|
| Create parent | `POST /authors` |
| Create child | `POST /books` |
| Read | `GET /books/{id}` |
| Update | `PUT /books/{id}` |
| Delete | `DELETE /books/{id}` |
| Conditional select | `GET /books/programming-india` |

Parent paths: `/authors`, `/students`, `/customers`, `/rooms`. Q5 has no supplier API: suppliers 1 (Chennai) and 2 (Mumbai) are inserted on startup from `q5-store/src/main/resources/data.sql`, so Q5 has only the five product endpoints.
Child paths: `/books`, `/courses`, `/orders`, `/bookings`, `/products`.

A missing id returns 404.

## Sample requests

Question 1:

```bash
curl -X POST localhost:8080/authors -H "Content-Type: application/json" \
  -d '{"authorName":"Joshua Bloch","country":"India"}'

curl -X POST localhost:8080/books -H "Content-Type: application/json" \
  -d '{"title":"Effective Java","category":"Programming","price":700,"author":{"authorId":1}}'

curl localhost:8080/books/1
curl localhost:8080/books/programming-india
```

Request bodies for the other questions:

```json
POST /students   {"studentName":"Ravi","department":"CSE","cgpa":8.5}
POST /courses    {"courseName":"AI","department":"CSE","credits":4,"student":{"studentId":1}}

POST /customers  {"customerName":"Anu","city":"Coimbatore","phone":"9999999999"}
POST /orders     {"orderDate":"2026-11-05","totalAmount":1500,"status":"Delivered","customer":{"customerId":1}}

POST /rooms      {"roomNumber":"101","roomType":"Deluxe","pricePerNight":3500}
POST /bookings   {"guestName":"Sam","checkInDate":"2026-11-10","checkOutDate":"2026-11-12","room":{"roomId":1}}

POST /products   {"productName":"Laptop X","category":"Laptop","price":50000,"stockQuantity":5,"supplier":{"supplierId":1}}
```

## What the conditional endpoints return

1. Programming books under 800 by authors from India
2. Courses with more than 3 credits for CSE students with CGPA above 8.0
3. Delivered orders above 1000 from customers in Coimbatore
4. Bookings for Deluxe rooms under 4000 per night, check-in between 1 and 30 November 2026
5. Laptops with fewer than 10 in stock from suppliers in Chennai
