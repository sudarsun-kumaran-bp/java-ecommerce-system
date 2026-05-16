# Java E-Commerce System

A console-based E-Commerce application built with Java, MySQL, and JDBC.

## Features
- User Registration and Login
- Product Browsing (10+ products across 3 categories)
- Cart Management (Add, View, Remove)
- Order Placement and Order History
- Admin Panel (Add, Update, Delete Products)
- Optimized SQL JOIN queries across 6 relational tables
- Structured exception handling and input validation

## Tech Stack
- Java (OOP, Collections, JDBC)
- MySQL (Normalized schema, Indexing, JOINs)
- JDBC (Database connectivity)

## Database Schema
- `users` — stores customer and admin accounts
- `categories` — product categories
- `products` — product catalog with stock management
- `cart` — user cart items
- `orders` — placed orders
- `order_items` — individual items per order

## How to Run
1. Clone the repository
2. Import `ecommerce.sql` into MySQL
3. Add MySQL JDBC driver JAR to `lib/` folder
4. Update `src/util/DBConnection.java` with your MySQL credentials
5. Compile: `javac -cp "src;lib/mysql-connector-j-9.6.0.jar" -d out src/**/*.java`
6. Run: `java -cp "out;lib/mysql-connector-j-9.6.0.jar" main.Main`

## Project Structure
src/
├── util/        # Database connection
├── model/       # User, Product, Order classes
├── dao/         # Database operations
└── main/        # Main application entry point
