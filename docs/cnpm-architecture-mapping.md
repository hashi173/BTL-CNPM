# Cnpm Architecture Mapping

## Architecture Overview

This project implements a 3-tier MVC + DAO architecture using JavaFX.

### 1. View (Presentation Layer)
Contains JavaFX GUI components. Responsible for rendering UI and capturing user input.

**Client Views (`src/com/coffeeshop/view/client/`):**
- `LoginView` (in `auth/`): User authentication
- `RegisterView` (in `auth/`): User registration
- `HomeView`: Main dashboard for client
- `MenuView`: Product listing
- `ProductDetailView`: Detailed product view & Add to Cart
- `CartView`: View cart items & Checkout preparation
- `CheckoutView`: Process orders
- `OrderListView`: List user's orders
- `ClientOrderDetailView`: Client's detailed order view
- `CancelConfirmView`: Order cancellation

**Admin Views (`src/com/coffeeshop/view/admin/`):**
- `AdminHomeView`: Admin dashboard
- `ManageProductView` / `EditProductView`: Manage products
- `ManageCategoryView` / `AddCategoryView`: Manage categories
- `OrderManagementView` / `OrderDetailView`: Manage orders
- `StatView` / `StatDetailView`: Sales statistics

**Shared Components:**
- `DashboardView`: Main shell with Sidebar and Content Area
- `SceneManager`: Centralized navigation controller
- `ThemeFX`: Design system and UI utilities

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
- **GUI Framework**: JavaFX (with AtlantaFX Theme)
- **Database**: PostgreSQL
- **Driver**: PostgreSQL JDBC Driver
