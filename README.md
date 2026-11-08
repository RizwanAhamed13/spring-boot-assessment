# Spring Boot Practical Assessment

One Spring Boot app (Java 21, Maven, MySQL) covering all five questions. Each question lives in its own package under `src/main/java/occ/_1`.

| Q | Package | Entities | Conditional endpoint |
|---|---------|----------|----------------------|
| 1 | `library` | Author, Book | `GET /books/programming-india` |
| 2 | `school` | Student, Course | `GET /courses/cse-high-credit` |
| 3 | `restaurant` | Customer, FoodOrder | `GET /orders/delivered-coimbatore` |
| 4 | `hotel` | Room, Booking | `GET /bookings/deluxe-november` |
| 5 | `store` | Supplier, Product | `GET /products/low-stock-laptops` |

## Run

1. Start MySQL. The `assessment` database is created automatically.
2. Set your MySQL login (defaults are user `root`, password `root`):

```bash
export DB_USER=root
export DB_PASSWORD=your_password
```

3. Start the app:

```bash
./mvnw spring-boot:run
```

It runs on http://localhost:8080 and the tables are created on first start.

## API pattern

Every question has the same endpoints. Create the parent first, then the child that points to it by id.

| Operation | Request |
|-----------|---------|
| Create parent | `POST /authors` |
| Create child | `POST /books` |
| Read | `GET /books/{id}` |
| Update | `PUT /books/{id}` |
| Delete | `DELETE /books/{id}` |
| Conditional select | `GET /books/programming-india` |

Parent paths: `/authors`, `/students`, `/customers`, `/rooms`, `/suppliers`.
Child paths: `/books`, `/courses`, `/orders`, `/bookings`, `/products`.

A missing id returns 404.

## Try it (Question 1)

```bash
curl -X POST localhost:8080/authors -H "Content-Type: application/json" \
  -d '{"authorName":"Joshua Bloch","country":"India"}'

curl -X POST localhost:8080/books -H "Content-Type: application/json" \
  -d '{"title":"Effective Java","category":"Programming","price":700,"author":{"authorId":1}}'

curl localhost:8080/books/1
curl localhost:8080/books/programming-india
```

## Sample bodies for the other questions

```json
POST /students   {"studentName":"Ravi","department":"CSE","cgpa":8.5}
POST /courses    {"courseName":"AI","department":"CSE","credits":4,"student":{"studentId":1}}

POST /customers  {"customerName":"Anu","city":"Coimbatore","phone":"9999999999"}
POST /orders     {"orderDate":"2026-11-05","totalAmount":1500,"status":"Delivered","customer":{"customerId":1}}

POST /rooms      {"roomNumber":"101","roomType":"Deluxe","pricePerNight":3500}
POST /bookings   {"guestName":"Sam","checkInDate":"2026-11-10","checkOutDate":"2026-11-12","room":{"roomId":1}}

POST /suppliers  {"supplierName":"TechCo","city":"Chennai","rating":4.5}
POST /products   {"productName":"Laptop X","category":"Laptop","price":50000,"stockQuantity":5,"supplier":{"supplierId":1}}
```

## What the conditional endpoints return

1. Programming books under 800 by authors from India
2. Courses with more than 3 credits for CSE students with CGPA above 8.0
3. Delivered orders above 1000 from customers in Coimbatore
4. Bookings for Deluxe rooms under 4000 per night, check-in between 1 and 30 November 2026
5. Laptops with fewer than 10 in stock from suppliers in Chennai
