# JavaFX Inventory Management System

A robust, desktop-based Inventory Management System built with **Java 21** and **JavaFX 21**. This application follows a clean MVC architecture and uses the Data Access Object (DAO) pattern to securely communicate with a **PostgreSQL** database using pure JDBC.

## ✨ Features
*   **Secure Authentication:** SHA-256 hashed password verification.
*   **Dynamic Dashboard:** Real-time summary statistics for low-stock items, total categories, and active suppliers.
*   **Inventory Tracking:** Full CRUD (Create, Read, Update, Delete) management for Items, Categories, and Suppliers.
*   **Stock Transactions:** An append-only transaction log that tracks Stock IN and Stock OUT events, preventing outbound transactions that exceed current stock levels.
*   **Secure Configuration:** Environment variables securely managed via `.env` files (no hardcoded credentials in the source code).

## 🛠️ Technology Stack
*   **Language:** Java 21
*   **GUI Framework:** JavaFX 21
*   **Database:** PostgreSQL (Compatible with local installs or Supabase)
*   **Build Tool:** Maven
*   **Environment Management:** `dotenv-java`

## 🚀 Getting Started

### Prerequisites
*   Java Development Kit (JDK) 21 installed.
*   Apache Maven installed (or use the included `mvnw` wrapper).
*   A running PostgreSQL instance (Local or Cloud like Supabase).

### 1. Database Setup
1. Create a PostgreSQL database (e.g., `inventory_db`).
2. Execute the included `inventory_system.sql` script into your database. This will build the required tables (`users`, `categories`, `suppliers`, `items`, `stock_transactions`), configure automated update triggers, and insert default test data.

### 2. Environment Configuration
Duplicate the `.env.example` file, rename it to `.env`, and update it with your actual database connection credentials:
```env
DB_HOST=your_host_address
DB_PORT=5432
DB_NAME=postgres
DB_USER=postgres
DB_PASSWORD=your_secure_password
