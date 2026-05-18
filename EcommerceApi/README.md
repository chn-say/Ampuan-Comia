# 👟 Ecommerce API Project (Premium Shoe Store)

Welcome to the Ecommerce API Project. This is a fully integrated full-stack web application featuring a robust Spring Boot backend powered by a MySQL database, coupled with a dynamic frontend user interface that fetches and renders live product data securely.

---

## 🔒 1. Security Architecture
The application implements a robust **Session-Based Authentication Architecture** coupled with stateful verification mechanisms to ensure high-grade context isolation:

* **Session Lifecycle:** Upon authenticating credentials via the `/login` pipeline, Spring Security issues an opaque token identifier mapped into a server-side store and injects a state tracker cookie (`JSESSIONID`) onto the user agent domain registry.
* **CSRF Mitigation:** Cross-Site Request Forgery defenses are strictly managed via a localized cookie token exchanger layout (`CookieCsrfTokenRepository`). Secure interaction sequences mandate retrieving an anti-forgery value wrapper key (`XSRF-TOKEN`) using state verification fetches (`GET`) prior to transmitting transactional or structural state mutations (`POST`/`PUT`/`DELETE`).
* **Route Guards & Interceptors:** The frontend application monitors response network footprints globally. Intercepted `401 Unauthorized` responses automatically kick unauthenticated clients back to `login.html`, while `403 Forbidden` statuses prompt explicit access denials for unauthorized administrative actions.

---

## 🗄️ 2. Database Schema
The database consists of primary relational tables managing identity, classification, and inventory with a structured layout:

* **`users`**: Stores user identity credentials, hashed passwords, and assigned system authorization boundaries (Roles).
* **`categories`**: Stores product classification details (e.g., Shoes, Clothing).
* **`products`**: Stores specific store items (Name, Description, Price, Stock, Image URL). Each product belongs to a specific category via the `category_id` foreign key constraint.

---

## 🛠️ 3. Comprehensive Validation Rules
Field-level structural boundaries are enforced at the Data Transfer Object layer using declarative Bean Validation annotations to prevent poisoned payloads from breaching relational tables:

### A. RegisterUserDto (Identity Records)
* `username`: Required, non-blank text value sequence (`@NotBlank`). Constrained strictly between **8 and 20 alphanumeric characters** (`@Size(min=8, max=20)`).
* `password`: Required, non-blank context element (`@NotBlank`). Must scale to a minimum length ceiling of **4 characters long** (`@Size(min=4)`).
* `role`: Non-blank string schema structure (`@NotBlank`) mapping explicit permission sets (e.g., `ROLE_USER`, `ROLE_ADMIN`).

### B. CreateProductDto (Inventory Models)
* `prodName`: Required text context segment (`@NotBlank`). Cannot consist entirely of whitespace markers.
* `prodPrice`: Required object metric element (`@NotNull`). Bound rigidly to an absolute decimal value configuration scale evaluating to greater than or equal to **0.01 currency units** (`@DecimalMin("0.01")`).

---

## 🌐 4. API Endpoint Reference Matrix

| Method | Path | Required Security Authorization Level | Description / Target Intent Operational Scope | Status Code |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/login` | Public Access Allowed | Pull and trigger standard XSRF Token values. | `200 OK` |
| **POST** | `/api/v1/auth/register` | Public Access Allowed | Creates a brand-new User identity inside DB records using DTO constraints. | `200 OK` |
| **POST** | `/login` | Public Access Allowed | Submits credentials to verify active session keys. | `302 Found / 200 OK` |
| **GET** | `/api/v1/products` | Public Access Allowed | Fetches all products with their associated categories for viewing. | `200 OK` |
| **POST** | `/api/v1/products` | `hasRole('ADMIN')` | Creates and saves a new product to the database (Admin only). | `201 Created` |
| **DELETE** | `/api/v1/products/{id}` | `hasRole('ADMIN')` | Permanently drops an obsolete product model row (Admin only). | `200 OK` |
| **POST** | `/api/v1/orders` | `isAuthenticated()` | Finalizes an ongoing shoe order checkout request. | `200 OK` |

---

## 📢 5. Centralized Validation Interception Output Sample
When a payload triggers structural boundary validation exceptions, the custom system controller context intercepts the event flow via `@ControllerAdvice`, rendering sanitized structured JSON response bodies with strict client tracking data blocks:

```json
{
  "timestamp": "2026-05-18T22:15:30.123456",
  "status": 400,
  "errors": {
    "username": "Username must be between 8 and 20 characters",
    "prodPrice": "Product price is required"
  }
}