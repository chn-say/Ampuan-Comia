# 👟 Ecommerce API Project (Premium Shoe Store)

Welcome to the Ecommerce API Project. This is a fully integrated full-stack web application featuring a robust Spring Boot backend powered by a MySQL database, coupled with a dynamic frontend user interface that fetches and renders live product data.

---

## 🗄️ Database Schema
The database consists of two primary tables with a structured **One-to-Many (1:N)** relationship:

* **`categories`**: Stores product classification details (e.g., Shoes, Clothing).
* **`products`**: Stores specific store items (Name, Description, Price, Stock, Image URL). Each product belongs to a specific category via the `category_id` foreign key constraint.

---

## 🌐 API Endpoint Reference

| Method | Path | Description | Expected Response | Status Code |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/api/v1/products` | Fetches all products with their associated categories | JSON Array of Products | `200 OK` |
| **GET** | `/api/v1/categories` | Fetches all available categories in the store | JSON Array of Categories | `200 OK` |
| **POST** | `/api/v1/products` | Creates and saves a new product to the database | Created Product Object | `201 Created` |

---

## 🛠️ Setup & Run Instructions

### 1. Backend Setup (Spring Boot)
1. Open the project folder in **IntelliJ IDEA**.
2. Configure your MySQL credentials in `src/main/resources/application.properties`.
3. Locate `EcommerceApiApplication.java` in your main package.
4. Click the **Green Play Button** ($\blacktriangleright$) to start the application server on port `8080`.

### 2. Frontend Setup (Live Server)
1. Open the `src/main/resources/static` folder inside **VS Code**.
2. Right-click on `index.html` and select **"Open with Live Server"**.
3. The storefront will automatically open and run at `http://127.0.0.1:5500/index.html`.

---

## 📝 Code Quality & Implementations
* **JPA & Relational Mapping:** Leveraged Hibernate and JPA entities (`@ManyToOne` and `@OneToMany`) to link products seamlessly with categories.
* **Error Handling & Safety:** Built-in global exception handlers in Java, paired with robust JavaScript `try...catch` blocks to gracefully log debugging data and handle server downs or empty states.
* **CORS Enabled:** Implemented global CORS mapping configuration inside `WebConfig.java` to securely bridge communication between cross-origins (`port 5500` to `port 8080`).

---

## 📸 Project Evidence & Screenshots

### 1. Database Table Status (MySQL Workbench)
*Below is the snapshot of the database populated with live product assets (e.g., Nike Air Max 270 row entry):*
*(Insert your MySQL Workbench screenshot here)*

### 2. Browser Console (Successful Live Fetch Validation)
*Below is the verified frontend UI showing successful dynamically rendered product objects with zero CORS errors in the inspector log:*
*(Insert your browser console snapshot here)*