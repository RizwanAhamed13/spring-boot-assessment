USE restaurant_db;

SHOW TABLES;

SELECT * FROM customer;

SELECT * FROM food_order;

SELECT o.order_id, o.order_date, o.total_amount, o.status, c.customer_id, c.customer_name, c.city
FROM food_order o
JOIN customer c ON c.customer_id = o.customer_id
ORDER BY o.order_id;

SELECT o.order_id, o.order_date, o.total_amount, o.status, c.customer_name
FROM food_order o
JOIN customer c ON c.customer_id = o.customer_id
WHERE o.status = 'Pending'
  AND o.total_amount > 500
  AND o.order_date BETWEEN '2026-11-01' AND '2026-11-10';
