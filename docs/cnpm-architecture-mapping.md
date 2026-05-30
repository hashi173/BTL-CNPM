# Cnpm Architecture Mapping

## Architecture Overview

This project implements a 3-tier MVC + DAO architecture using Java Swing.

### 1. View (Presentation Layer)
Contains Java Swing GUI components (`JFrame`). Responsible for rendering UI and capturing user input.

**Client Views:**
- `LoginFrm`: User authentication
- `HomeFrm`: Main dashboard for client
- `MenuFrm`: Product listing
- `ProductDetailFrm`: Detailed product view & Add to Cart
- `CartFrm`: View cart items & Checkout preparation
- `CheckoutFrm`: Process orders
- `OrderListFrm`: List user's orders
- `CancelConfirmFrm`: Order cancellation

**Admin Views:**
- `AdminHomeFrm`: Admin dashboard
- `ManageProductFrm` / `EditProductFrm`: Manage products
- `ManageCategoryFrm` / `AddCategoryFrm`: Manage categories
- `OrderManagementFrm` / `OrderDetailFrm`: Manage orders
- `StatFrm`: Sales statistics

### 2. Model (Entity Layer)
Represents the database tables as Java objects (Entities).
- `Users`: Handles accounts and roles
- `Products`: Manage items for sale
- `Categories`: Product grouping
- `Orders`: Order information
- `OrderItems`: Details of items in an order
- `CartItems`: In-memory DTO for shopping cart

### 3. DAO (Data Access Layer)
Handles all interactions with the PostgreSQL database.
- `DAO`: Base class managing JDBC connections
- `UserDAO`: Authentication & User queries
- `ProductDAO`: CRUD for Products
- `CategoryDAO`: CRUD for Categories
- `OrderDAO`: Order creation and status management
- `OrderItemDAO`: Storing order items
- `CartDAO`: Manages in-memory cart items (Session-based)

---

## Technical Stack
- **Language**: Java 17+
- **GUI Framework**: Java Swing
- **Database**: PostgreSQL
- **Driver**: PostgreSQL JDBC Driver
