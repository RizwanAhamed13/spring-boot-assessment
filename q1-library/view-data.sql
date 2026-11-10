USE library_db;

SELECT b.book_id, b.title, b.category, b.price, a.author_id, a.author_name, a.country
FROM book b
JOIN author a ON a.author_id = b.author_id
ORDER BY b.book_id;

SELECT b.title, a.author_name, b.price
FROM book b
JOIN author a ON a.author_id = b.author_id
WHERE b.category = 'Programming'
  AND b.price = (SELECT MAX(price) FROM book WHERE category = 'Programming');
