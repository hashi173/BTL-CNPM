|  |
| --- |
| **HỌC VIỆN CÔNG NGHỆ BƯU CHÍNH VIỄN THÔNG**   **KHOA CÔNG NGHỆ THÔNG TIN**   ![](./Hashi_images/image-001.png)   **BÁO CÁO BÀI TẬP LỚN**   **NHẬP MÔN CÔNG NGHỆ PHẦN MỀM**   **MODULE: QUẢN LÝ ĐƠN HÀNG**   Chủ đề: **QUẢN LÝ QUÁN CÀ PHÊ**   Giảng viên hướng dẫn: **Đỗ Thị Liên**   Lớp: **D23CQCE01-B**   Nhóm thực hiện: **8**Phạm Thị Thiên Hà B23DCCN266 |
| **Hà Nội, 2026** |

# MỤC LỤC

MỤC LỤC 2

I. REQUIREMENTS 4

1\. System description in the natural language 4

1.1. Objective and scope 4

1.2. User and functions that each user could use 4

1.3. Detailed business process of functions 4

1.4. Information about related objects 5

1.5. Relation among objects 5

2\. System description in UML 6

2.1. Actors 6

2.2. Use cases 6

2.3. General Use case diagram 6

2.4. Use case view order detail 6

2.5. Use case update order status 6

II. ANALYSIS 7

1\. Standard and exceptional scenario 7

1.1. Standard and exceptional scenario of use case: view order detail 7

1.2. Standard and exceptional scenario of use case: update order status 7

2\. Static analysis: Entity class extraction 7

2.1. Describe the system within a paragraph 7

2.2. Extract nouns and classify them 7

2.3. Quantity and object relationships among classes 7

3\. Static analysis: Classes Diagram 7

3.1. View order detail 7

3.2. Update order status 7

4\. Dynamic analysis: Sequence Diagram 7

4.1. View order detail 7

4.2. Update order status 7

III. DESIGN 8

1\. Entity class design 8

2\. Database design 8

3\. Static design: class diagram 8

3.1. View order detail 8

3.2. Update order status 8

4\. Dynamic design: sequence diagram 8

4.1. View order detail 8

4.2. Update order status 8

IV. IMPLEMENTATION 9

1\. Coding 9

1.1. Organization of Java project 9

1.2. Code for module view order detail 9

1.3. Code for module update order status 9

2\. Junit test for control classes 9

2.1. JUnit test case list 9

2.2. Input (current) database 9

2.3. JUnit test case code for view order detail 9

2.4. JUnit test case code for update order status 9

V. TESTING 10

1\. Black-box testcase 10

1.1. Black-box test case list 10

1.2. Test case for view order detail 10

1.3. Test case for cancel order 10

1.4. Test case for update order status 10

# I. REQUIREMENTS

## 1\. System description in the natural language

## 1.1. Objective and scope

\- This is a desktop-based application which will be internally used inside a coffee shop.

\- Clients and Administrators (Admin) of the coffee shop could use this application.

\- This application support the management of only one coffee shop.

\- This application could be installed on many computers of the coffee shop. However, the database is stocked on the coffee shop server.

## 1.2. User and functions that each user could use

\- Both Clients and Administrators of the coffee shop could use this application.

\- Clients could use the following functions:

-   Manage cart items: customize options (quantity, size, ice, sugar, note), add to cart.
-   Manage orders: search, view order detail, confirm placing order, track order.

\- Administrators could use the following functions:

-   Manage products: add, edit, delete.
-   Manage categories: add, edit, delete.
-   Manage orders: view order detail, change order status (pending, confirmed, shipping, completed or cancelled).
-   View statistical table: by all-time, by day, by week, by month, by customized day, by product.

## 1.3. Detailed business process of functions

This section presents only some selected fuctions mainly in module “Manage orders” (Administrators). The others could be described in the same manner.

a. View order detail

The Admin logs in into the system -> The administrator’s UI appeared, it has the following options: manage orders, manage products, manage categories and view the statistical table -> Admin selects to manage orders -> The orders management UI appeared with a list of all orders (each row corresponding to an order with: order code (tracking code), order date, subtotal, status), a search bar and one button: view order detail/ process -> The Admin clicks on a specific order row then clicks on the view order detail/ process button to view its detail -> The order detail UI appeared showing the following information: order code (tracking code), order date, status, customer name, phone number, delivery address, note, a list of ordered items (each row corresponding to an item with: product name, options, quantity, unit price, subtotal), a change status combo box, an update button and a back button -> The Admin views alls information and clicks the back button to return to the order management list.

b. Change order status

The Admin logs in into the system -> The administrator’s UI appeared, it has the following options: manage orders, manage products, manage categories and view the statistical table -> Admin selects to manage orders -> The orders management UI appeared with a list of all orders (each row corresponding to an order with: order code (tracking code), order date, subtotal, status), a search bar and one button: view order detail/ process -> The Admin clicks on a specific order row then clicks on the view order detail/ process button to view its detail -> The order detail UI appeared showing the following information: order code (tracking code), order date, status, customer name, phone number, delivery address, note, a list of ordered items (each row corresponding to an item with: product name, options, quantity, unit price, subtotal), a change status combo box, an update button and a back button -> The admin selects the desired new status (from PENDING to CONFIRMED, from CONFIRMED to SHIPPING, or from SHIPPING to COMPLETED, if the order’s current status is PENDING or CONFIRMED, then Admin can select “CANCELLED” from the combo box) and then clicks on the update button -> The system announces a succes alert and then, return to the orders detail UI.

## 1.4. Information about related objects

\- Coffee shop: name, address, description (including image/ text/ video).

\- Product: name, category, unit price, size (base size), description, image, status (available/ unavailable).

\- Category: name, description.

\- Orders: tracking code, client name, phone, address, note, status (PENDING / CONFIRMED / SHIPPING / COMPLETED / CANCELLED), subtotal, quantity, order date

\- Client: username, full name, email, phone, password, role (Client/ Admin), note.

\- Admin: username, password, role

## 1.5. Relation among objects

\- A coffeshop has many product categories.

\- A category contains many products. Each product belongs to a specific category.

\- The Admin can manage many categories and products.

\- A client can place one or many orders at any time. Each order belongs to a specific client.

\- An order can contain many products. A product can appear in many different orders.

\- A product can only be placed if it is available.

\- The client could cancel their order before the order is shipped (the orders’ status is PENDING or CONFIRMED).

\- The Admin can update the status of the order.

## 2\. System description in UML

## 2.1. Actors

\- Direct actors: Admin, who manages orders (view order detail, update order status).

## 2.2. Use cases

The Admin could use the following functions:

\- View orders list and order detail -> view order detail.

\- Change order status. -> update order status.

## 2.3. General Use case diagram

![](./Hashi_images/image-002.png)

The use case are described as follow:

\- View order detail: this use case enables the Admin to view detail information about the orders.

\- Update order status: this use case enables the Admin to update the order’s status.

## 2.4. Use case

![](./Hashi_images/image-003.png)

Use case description:

\- View order detail: this use case enables the Admin to view detail information about the orders.

\- Update order status: this use case enables the Admin to update the order’s status.

# II. ANALYSIS

## 1\. Standard and exceptional scenario

## 1.1. Standard and exceptional scenario of use case: view order detail

1\. Admin starts the application to view order ORD-000058’s detail.

2\. The login interface appeared with: an input text for username, an input text for password, a button to login.

3\. Admin enters username as “admin”, password as “123456” and then, clicks on the login button.

4\. The admin home UI appeared with four options: manage orders, manage categories, manage products and view statistical table.

5\. Admin chooses to manage order.

6\. The order management UI appeared with a list of all orders (each row corresponds to an order with: order code (tracking code), order date, subtotal, status) and a view order detail button.

7\. Admin chooses an order with a tracking code of ORD-000058 and then clicks on the view order detail button.

8\. The order detail UI appeared showing: Order code: ORD-000058; Order date: 01/06/2026 11:21; status: PENDING; customer name: Nguyễn Văn Khách, phone: 0987654321; address: Số 6, núi Trúc; note: Giao hàng nhanh, a status combo box, a back button and a list of ordered items as follows:

product name, options, quantity, unit price, subtotal

| STT | Product name | Options | Quantity | Unit Price | Subtotal |
| --- | --- | --- | --- | --- | --- |
| 1 | Bạc xỉu | Sugar: 100%, Ice: 100% | 2 | 35,000 | 70,000 |

9\. Admin reviews all order information and clicks the Back button.

10\. The order detail window is closed and the system returns to the order management UI.

**Exceptional scenarios:**

4\. The system alerts that the username/ password is incorrect.

7\. There is no order with a tracking code of ORD-000058.

## 1.2. Standard and exceptional scenario of use case: update order status

1\. Admin starts the application to view order ORD-000058’s detail.

2\. The login interface appeared with: an input text for username, an input text for password, a button to login.

3\. Admin enters username as “admin”, password as “123456” and then, clicks on the login button.

4\. The admin home UI appeared with four options: manage orders, manage categories, manage products and view statistical table.

5\. Admin chooses to manage order.

6\. The order management UI appeared with a list of all orders (each row corresponds to an order with: order code (tracking code), order date, subtotal, status) and a view order detail button.

7\. Admin chooses an order with a tracking code of ORD-000058 and then clicks on the view order detail button.

8\. The order detail UI appeared showing: Order code: ORD-000058; Order date: 01/06/2026 11:21; status: CANCELLED; customer name: Nguyễn Văn Khách, phone: 0987654321; address: Số 6, núi Trúc; note: Giao hàng nhanh, a status combo box, a back button and a list of ordered items as follows:

product name, options, quantity, unit price, subtotal

| STT | Product name | Options | Quantity | Unit Price | Subtotal |
| --- | --- | --- | --- | --- | --- |
| 1 | Bạc xỉu | Sugar: 100%, Ice: 100% | 2 | 35,000 | 70,000 |

9\. Admin opens the status combo box.

10\. The combo box displays available options (PENDING, CONFIRMED, CANCELLED), an update button and a back button.

11\. Admin selects “CANCELLED” and clicks on the update button.

12\. A success alert appeared and then, the system returns to order detail UI.

**Exceptional scenarios:**

4\. The system alerts that the username/ password is incorrect.

7\. There is no order with a tracking code of ORD-000058.

9\. The order’s status is “CANCELLED”.

11\. The order’s status is “SHIPPING” or “COMPLETED”.

## 2\. Static analysis: Entity class extraction

## 2.1. Describe the function within a paragraph

This belongs to the overall report.

## 2.2. Extract nouns and classify them

This belongs to the overall report.

## 2.3. Quantity and object relationships among classes

This belongs to the overall report.

## 3\. Static analysis: Classes Diagram

Analysis this module:

-   Enter the system -> The login interface is appeared -> need a class: LoginView
-   input for user name -> inUsername
-   input for password -> inPassword
-   a submit to Login -> subLogin
-   Enter the username/ password -> the system must check if the login is correct -> need a method:
-   name: checkLongin()
-   input: username, password (of the class User)
-   output: boolean
-   assign to the entity class: User
-   Once login is succesful -> the main interface of the manager is appeared -> need a class: AdminHomeView which has at least:
-   An option to choose to manage order -> subOrderManagement
-   Choose the option to manage order -> The order management interface appeared -> need a class: OrderManagmentView:
-   A list of orders -> outOrderList
-   A view order detail button -> subViewDetail
-   Choose the option to view order detail -> The order detail interface appeared -> need a class OrderDetailView:
-   Order information: read only -> outOrderInfo
-   Order item list: read only -> outItemList
-   Order status: read and editable by clicking -> outsubStatus
-   Button to save status change -> subUpdate
-   Button to go back: submit -> subBack
-   Change the status and click save -> The system has to update into the DB -> need a method:
-   Name: updateOrderStatus()
-   Input: an object of Orders
-   Output: none or boolean
-   Assign to the entity class: Orders
-   After updating, the system returns to the AdminHomeView.

![](./Hashi_images/image-004.png)

## 4\. Dynamic analysis: Sequence Diagram

## 4.1. View order detail

![](./Hashi_images/image-005.png)

Scenario version 2

1.  The Admin enters username/ password and then clicks on the Login button.
2.  The class LoginView calls the class User to process.
3.  The class User calls the method checkLogin(). The login is successful.
4.  The class User returns the results to the class LoginView.
5.  The class LoginView calls the class AdminHomeView.
6.  The class AdminHomeView displays itself to the Admin.
7.  The Admin chooses the option of manage orders.
8.  The class AdminHomeView call the class OrderManagementView.
9.  The class OrderManagementView calls the class Orders to get data.
10.  The class Orders calls the method getAllOrders().
11.  The class Orders returns the result to the class OrderManagementView.
12.  The class OrderManagementView displays the order list to the Admin.
13.  The Admin clicks on the button View Order Detail.
14.  The class OrderManagementView calls the class OrderDetailView.
15.  The class OrderDetailView calls the class Orders to process.
16.  The class Orders calls the method getOrderDetails().
17.  The class Orders return the order detail to the class OrderDetailView.
18.  The class OrderDetailView displays itself to the Admin.

## 4.2. Update order status

![](./Hashi_images/image-006.png)

Scenario version 2

1.  The Admin enters username/ password and then clicks on the Login button.
2.  The class LoginView calls the class User to process.
3.  The class User calls the method checkLogin(). The login is successful.
4.  The class User returns the results to the class LoginView.
5.  The class LoginView calls the class AdminHomeView.
6.  The class AdminHomeView displays itself to the Admin.
7.  The Admin chooses the option of manage orders.
8.  The class AdminHomeView call the class OrderManagementView.
9.  The class OrderManagementView calls the class Orders to get data.
10.  The class Orders calls the method getAllOrders().
11.  The class Orders returns the result to the class OrderManagementView.
12.  The class OrderManagementView displays the order list to the Admin.
13.  The Admin clicks on the button View Order Detail.
14.  The class OrderManagementView calls the class OrderDetailView.
15.  The class OrderDetailView calls the class Orders to process.
16.  The class Orders calls the method getOrderDetails().
17.  The class Orders return the order detail to the class OrderDetailView.
18.  The class OrderDetailView displays itself to the Admin.
19.  The Admin clicks on the combo box Change Order Status to change order status.
20.  The class OrderDetailView calls the class Orders to process.
21.  The class Orders calls the method updateOrderStatus() to process.
22.  The class Orders returns result to the class OrderDetailView.
23.  The class OrderDetailView display itself to the Admin.

# III. DESIGN

## 1\. Entity class design

This belongs to the overall report.

## 2\. Database design

This belongs to the overall report.

## 3\. Static design: class diagram

Design based on MVC model:

\- View classes:

-   LoginFrm is the interface to login. It needs a text field to enter the username, a text field to enter password, and a button to login.
-   AdminHomeFrm is the home interface for the Admin. It needs at least a button to go to the room management function.
-   OrderManagementFrm is the interface to manage order. It needs a table to display orders list and a button to view order detail.
-   OrderDetailFrm is the interface to view order detail and change order status. It needs a table to display order item list, a combo box to change order status, a button to update order status, a button to go back.

\- Control classes:

-   DAO is a general class of DAO. It has only the construction to connect to the DB and provides the common connection for all inherited DAO classes in the system.
-   UserDAO is the class for manipulating with DB related to the User object. In this module, it needs a method to verify whether the login information is correct or not, it is checkLogin() method.
-   OrderDAO is the class for manipulating with DB related to the Orders object. In this module, it needs three methods:

\+ getAllOrders(): to return all orders.

\+ getOrderDetal(): to return orders detail.

\+ updateOrderStatus(): to update order status change.

\- Entity classes: User, Orders, OrderItems.

![](./Hashi_images/image-007.jpeg)

## 4\. Dynamic design: sequence diagram

Scenario v3

1.  The admin enters her username, password, and clicks on the login button on LoginFrm.
2.  The method actionPerformed() of LoginFrm is called.
3.  The method actionPerformed() calls User to create a User object.
4.  The class User packs the information into a User object.
5.  The class User returns the User object to the method actionPerformed().
6.  The method actionPerformed() calls method checkLogin() of the class UserDAO.
7.  The method checkLogin() checks the login information.
8.  The method checkLogin() returns the result to the method actionPerformed().
9.  The method actionPerformed() calls the class AdminHomeFrm.
10.  The constructor AdminHomeFrm() is called.
11.  The corresponding interface is displayed.
12.  The admin clicks on the order management function.
13.  The method actionPerformed() is activated.
14.  The method actionPerformed() calls the class OrderManagementFrm.
15.  The constructor OrderManagementFrm() is activated.
16.  The method OrderManagementFrm() calls the method getAllOrders() of the class OrderDAO.
17.  The method getAllOrders() gets the list of orders from the database.
18.  The method getAllOrders() returns the result to the method OrderManagementFrm().
19.  The method OrderManagementFrm() calls the class Order to pack the order information.
20.  The class Order packs the information into Order entities.
21.  The class Order returns the objects to the method OrderManagementFrm().
22.  The corresponding interface is displayed with the list of orders.
23.  The admin clicks to view the details of an order.
24.  The method actionPerformed() is activated.
25.  The method actionPerformed() calls the class OrderDetailFrm.
26.  The constructor OrderDetailFrm() is activated.
27.  The method OrderDetailFrm() calls the method getOrderDetail(orderId) of the class OrderDAO.
28.  The method getOrderDetail() gets the order details from the database.
29.  The method getOrderDetail() returns the result to the method OrderDetailFrm().
30.  The method OrderDetailFrm() calls the class OrderItem to pack the detailed information.
31.  The class OrderItem packs the information into OrderItem entities.
32.  The class OrderItem returns the objects to the method OrderDetailFrm().
33.  **The order detail interface displays full information for the admin.**
34.  The admin selects a new order status and clicks the "Update Status" button.
35.  The method actionPerformed() is activated.
36.  The method actionPerformed() calls the method updateOrderStatus(orderId, newStatus) of the class OrderDAO.
37.  The method updateOrderStatus() executes the update query in the database.
38.  The method updateOrderStatus() returns the success result to the method actionPerformed().
39.  The method actionPerformed() displays a success message to the admin.

![](./Hashi_images/image-008.jpeg)

# IV. IMPLEMENTATION

## 1\. Coding

## 1.1. Organization of Java project

The project is organized in the following package structure:

-   com.coffeeshop.model: package of all entity classes.
-   com.coffeeshop.dao: package of all DAO classes.
-   com.coffeeshop.view.admin: package of all view classes related to admin-side
-   com.coffeeshop.view.client: package of all view classes related to client-side
-   com.coffeeshop.view.common: package for shared components (SceneManager, DashboardView, ThemeFX)
-   com.coffeeshop.test: package of Junit test classes corresponding to the DAO classes.

## 1.2. Code for module view order detail

Orders.java (entity class – selected methods):
```java
package com.coffeeshop.model;
import java.sql.Timestamp;
import java.util.UUID;

public class Orders {
    private UUID id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String addressText;
    private String customerName;
    private String note;
    private String orderType;
    private String phone;
    private String status;
    private Double subTotal;
    private Double totalAmount;
    private String trackingCode;
    private UUID userId;

    public Orders() {}
    // ... (full getters and setters in source)
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTrackingCode() { return trackingCode; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getAddressText() { return addressText; }
    public String getNote() { return note; }
    public Timestamp getCreatedAt() { return createdAt; }
}
```

OrderDAO.java – getAllOrders() and getOrderDetail():
```java
public List<Orders> getAllOrders() {
    List<Orders> list = new ArrayList<>();
    String sql = "SELECT * FROM orders ORDER BY created_at DESC";
    try {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) { list.add(mapResultSet(rs)); }
    } catch (SQLException e) { e.printStackTrace(); }
    return list;
}

public Orders getOrderDetail(UUID orderId) {
    String sql = "SELECT * FROM orders WHERE id = ?";
    try {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, orderId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) { return mapResultSet(rs); }
    } catch (SQLException e) { e.printStackTrace(); }
    return null;
}
```

OrderDetailView.java – constructor and loadOrderDetail() (key excerpt):
```java
public OrderDetailView(Users admin, UUID orderId) {
    this.currentAdmin = admin;
    this.orderId = orderId;
    // ... build UI layout (header cards, TableView, footer buttons)
    loadOrderDetail();
    btnBack.setOnAction(e -> ((Stage) getScene().getWindow()).close());
    btnUpdate.setOnAction(e -> {
        String newStatus = cmbStatus.getValue();
        if (newStatus != null && !newStatus.equals(order.getStatus())) {
            if (orderDAO.updateOrderStatus(orderId, newStatus)) {
                new Alert(Alert.AlertType.INFORMATION, "Cap nhat thanh cong!").showAndWait();
                lblStatus.setText("Trang thai: " + ThemeFX.translateStatus(newStatus));
                order.setStatus(newStatus);
                setupStatusCombo(newStatus);
            } else {
                new Alert(Alert.AlertType.ERROR, "Cap nhat that bai!").showAndWait();
            }
        }
    });
}

private void loadOrderDetail() {
    order = orderDAO.getOrderDetail(orderId);
    if (order != null) {
        // Populate order info labels and customer info labels
        setupStatusCombo(order.getStatus());
        List<OrderItems> items = itemDAO.getOrderItems(orderId);
        table.setItems(FXCollections.observableArrayList(items));
    }
}
```

## 1.3. Code for module cancel order

The cancel order functionality is integrated within OrderDetailView via the status combo box. The relevant DAO method is:
```java
// OrderDAO.java
public boolean cancelOrder(UUID orderId) {
    String sql = "UPDATE orders SET status = 'CANCELLED', updated_at = NOW() WHERE id = ?";
    try {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, orderId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) { e.printStackTrace(); }
    return false;
}
```

The cancel order UI flow is handled in OrderDetailView – when the admin selects CANCELLED from cmbStatus and clicks btnUpdate, the updateOrderStatus(orderId, "CANCELLED") method is called (see section 1.2). Additionally, there is a dedicated cancelOrder() helper in OrderDAO for consistency. The setupStatusCombo() method governs which states are reachable:
```java
private void setupStatusCombo(String currentStatus) {
    cmbStatus.getItems().clear();
    if ("PENDING".equals(currentStatus)) {
        cmbStatus.getItems().addAll("PENDING", "CONFIRMED", "CANCELLED");
    } else if ("CONFIRMED".equals(currentStatus)) {
        cmbStatus.getItems().addAll("CONFIRMED", "SHIPPING", "CANCELLED");
    } else if ("SHIPPING".equals(currentStatus)) {
        cmbStatus.getItems().addAll("SHIPPING", "COMPLETED", "CANCELLED");
    } else {
        // COMPLETED or CANCELLED -> terminal state, disable
        cmbStatus.getItems().add(currentStatus);
        cmbStatus.setDisable(true);
        btnUpdate.setDisable(true);
    }
    cmbStatus.setValue(currentStatus);
}
```

## 1.4. Code for module update order status

The core DAO method for updating any status (including cancel) is:
```java
// OrderDAO.java
public boolean updateOrderStatus(UUID orderId, String status) {
    String sql = "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?";
    try {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ps.setObject(2, orderId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) { e.printStackTrace(); }
    return false;
}
```

The checkOrderStatus() method is also available in OrderDAO for external validation:
```java
// OrderDAO.java
public String checkOrderStatus(UUID orderId) {
    String sql = "SELECT status FROM orders WHERE id = ?";
    try {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, orderId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) { return rs.getString("status"); }
    } catch (SQLException e) { e.printStackTrace(); }
    return null;
}
```

## 2. JUnit test for control classes

### 2.1. JUnit test case list

| No. | Test class | Test method | Description |
|---|---|---|---|
| 1 | OrderDAOTest | testGetAllOrders1() | Standard: DB has 3 orders → returns list of 3. |
| 2 | OrderDAOTest | testGetAllOrders2() | Standard: DB has 0 orders → returns empty list. |
| 3 | OrderDAOTest | testGetOrderDetail1() | Standard: existing orderId → returns correct orders object. |
| 4 | OrderDAOTest | testGetOrderDetail2() | Exception: non-existing orderId → returns null. |
| 5 | OrderDAOTest | testCheckOrderStatus1() | Standard: existing orderId → returns "PENDING". |
| 6 | OrderDAOTest | testCheckOrderStatus2() | Standard: orderId with CONFIRMED status → returns "CONFIRMED". |
| 7 | OrderDAOTest | testCheckOrderStatus3() | Exception: non-existing orderId → returns null. |
| 8 | OrderDAOTest | testUpdateOrderStatus1() | Standard: PENDING → CONFIRMED → returns true and DB updated. |
| 9 | OrderDAOTest | testUpdateOrderStatus2() | Standard: CONFIRMED → SHIPPING → returns true. |
| 10 | OrderDAOTest | testUpdateOrderStatus3() | Standard: SHIPPING → COMPLETED → returns true. |
| 11 | OrderDAOTest | testUpdateOrderStatus4_CancelPending() | Standard: PENDING order → updateOrderStatus(CANCELLED) → returns true. |
| 12 | OrderDAOTest | testUpdateOrderStatus5_CancelFakeId() | Exception: non-existing orderId → returns false. |

### 2.2. Input (current) database

Table: orders (seed data for JUnit tests)

| id (short) | tracking_code | customer_name | phone | total_amount | status |
|---|---|---|---|---|---|
| UUID-1 | CS-0001 | Nguyen Van A | 0912345678 | 45000 | PENDING |
| UUID-2 | CS-0002 | Tran Thi B | 0987654321 | 90000 | CONFIRMED |
| UUID-3 | CS-0003 | Le Van C | 0901234567 | 120000 | CANCELLED |

### 2.3. JUnit test case code for view order detail

```java
package com.coffeeshop.dao;
import org.junit.Assert;
import org.junit.Test;
import com.coffeeshop.model.Orders;
import java.util.List;
import java.util.UUID;

public class OrderDAOTest {

    // Test 1: getAllOrders - standard (3 orders exist)
    @Test
    public void testGetAllOrders1() {
        OrderDAO dao = new OrderDAO();
        List<Orders> list = dao.getAllOrders();
        Assert.assertNotNull(list);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("CS-0001", list.get(0).getTrackingCode());
    }

    // Test 2: getAllOrders - empty DB
    @Test
    public void testGetAllOrders2() {
        // Assumes the test DB has been cleared before this test
        OrderDAO dao = new OrderDAO();
        List<Orders> list = dao.getAllOrders();
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());
    }

    // Test 3: getOrderDetail - existing order
    @Test
    public void testGetOrderDetail1() {
        OrderDAO dao = new OrderDAO();
        // UUID of CS-0001 in test DB:
        UUID existingId = UUID.fromString("<UUID-1>");
        Orders o = dao.getOrderDetail(existingId);
        Assert.assertNotNull(o);
        Assert.assertEquals("CS-0001", o.getTrackingCode());
        Assert.assertEquals("Nguyen Van A", o.getCustomerName());
        Assert.assertEquals("PENDING", o.getStatus());
    }

    // Test 4: getOrderDetail - non-existing order
    @Test
    public void testGetOrderDetail2() {
        OrderDAO dao = new OrderDAO();
        UUID nonExistingId = UUID.randomUUID();
        Orders o = dao.getOrderDetail(nonExistingId);
        Assert.assertNull(o);
    }
}
```

### 2.4. JUnit test case code for update order status

```java
    // Test 5: checkOrderStatus - existing PENDING order
    @Test
    public void testCheckOrderStatus1() {
        OrderDAO dao = new OrderDAO();
        UUID pendingId = UUID.fromString("<UUID-1>"); // CS-0001
        String status = dao.checkOrderStatus(pendingId);
        Assert.assertNotNull(status);
        Assert.assertEquals("PENDING", status);
    }

    // Test 7: checkOrderStatus - non-existing orderId
    @Test
    public void testCheckOrderStatus3() {
        OrderDAO dao = new OrderDAO();
        UUID fakeId = UUID.randomUUID();
        String status = dao.checkOrderStatus(fakeId);
        Assert.assertNull(status);
    }

    // Test 8: updateOrderStatus - PENDING → CONFIRMED
    @Test
    public void testUpdateOrderStatus1() {
        OrderDAO dao = new OrderDAO();
        UUID pendingId = UUID.fromString("<UUID-1>"); // CS-0001, PENDING
        boolean result = dao.updateOrderStatus(pendingId, "CONFIRMED");
        Assert.assertTrue(result);
        String newStatus = dao.checkOrderStatus(pendingId);
        Assert.assertEquals("CONFIRMED", newStatus);
    }

    // Test 9: updateOrderStatus - CONFIRMED → SHIPPING
    @Test
    public void testUpdateOrderStatus2() {
        OrderDAO dao = new OrderDAO();
        UUID confirmedId = UUID.fromString("<UUID-2>"); // CS-0002, CONFIRMED
        boolean result = dao.updateOrderStatus(confirmedId, "SHIPPING");
        Assert.assertTrue(result);
        String newStatus = dao.checkOrderStatus(confirmedId);
        Assert.assertEquals("SHIPPING", newStatus);
    }

    // Test 10: updateOrderStatus - SHIPPING → COMPLETED
    @Test
    public void testUpdateOrderStatus3() {
        OrderDAO dao = new OrderDAO();
        // Assume UUID-2 is now SHIPPING after test 9
        UUID shippingId = UUID.fromString("<UUID-2>");
        boolean result = dao.updateOrderStatus(shippingId, "COMPLETED");
        Assert.assertTrue(result);
        String newStatus = dao.checkOrderStatus(shippingId);
        Assert.assertEquals("COMPLETED", newStatus);
    }

    // Test 11: updateOrderStatus - PENDING → CANCELLED
    @Test
    public void testUpdateOrderStatus4_CancelPending() {
        OrderDAO dao = new OrderDAO();
        UUID pendingOrderId = UUID.fromString("<UUID-1>"); 
        boolean result = dao.updateOrderStatus(pendingOrderId, "CANCELLED");
        Assert.assertTrue(result);
        String status = dao.checkOrderStatus(pendingOrderId);
        Assert.assertEquals("CANCELLED", status);
    }

    // Test 12: updateOrderStatus - Fake ID
    @Test
    public void testUpdateOrderStatus5_CancelFakeId() {
        OrderDAO dao = new OrderDAO();
        UUID fakeId = UUID.randomUUID();
        boolean result = dao.updateOrderStatus(fakeId, "CANCELLED");
        Assert.assertFalse(result);
    }
```

# V. TESTING

## 1. Black-box testcase

### 1.1. Black-box test case list

| No. | Module | Test case |
|---|---|---|
| 1 | View order detail | Admin views detail of an existing PENDING order. |
| 2 | View order detail | Admin searches for a non-existing tracking code. |
| 3 | Update order status | Admin updates status of a PENDING order to CANCELLED. |
| 4 | Update order status | Admin attempts to change status of an already CANCELLED order. |
| 5 | Update order status | Admin attempts to change status of a COMPLETED order. |
| 6 | Update order status | Admin updates status from PENDING to CONFIRMED. |
| 7 | Update order status | Admin updates status from CONFIRMED to SHIPPING. |
| 8 | Update order status | Admin updates status from SHIPPING to COMPLETED. |
| 9 | Update order status | Admin selects the same status as the current one → no update. |
| 10 | Cancel order | Admin cancels a CONFIRMED order. |

### 1.2. Test case for view order detail

**Test case No. 1 – standard: admin views detail of an existing PENDING order.**

Pre-condition: Admin is logged in. Order CS-0001 exists in the database with status PENDING.

Database before testing (table: orders):

| id (short) | tracking_code | customer_name | status |
|---|---|---|---|
| UUID-1 | CS-0001 | Nguyen Van A | PENDING |
| UUID-2 | CS-0002 | Tran Thi B | CONFIRMED |
| UUID-3 | CS-0003 | Le Van C | CANCELLED |

Testing scenario and expected results:

| Scenario | Expected results |
|---|---|
| 1. At AdminHomeView, click "Quản lý đơn hàng". | OrderManagementView appears with a table showing 3 orders (CS-0001, CS-0002, CS-0003) and their tracking codes, customer names, and statuses. |
| 2. Click on the row with tracking code "CS-0001". | OrderDetailView appears showing: Order Code: CS-0001; Customer: Nguyen Van A; Phone: 0912345678; Status: Chờ xác nhận (PENDING). Item table and status combo box visible. |
| 3. Click the "Quay lại" (Back) button. | OrderDetailView window closes and returns to OrderManagementView. |

Database after testing: No change.

**Test case No. 2 – exception: admin searches for a non-existing tracking code.**

Pre-condition: Admin is logged in. No order with tracking code "CS-9999" exists.

Testing scenario and expected results:

| Scenario | Expected results |
|---|---|
| 1. At OrderManagementView, type "CS-9999" in the search field. | The table filters to show 0 matching rows. No clickable order is available. |

Database after testing: No change.

### 1.3. Test case for cancel order

**Test case No. 3 – standard: admin cancels a PENDING order.**

Pre-condition: Admin is logged in. Order CS-0001 exists with status PENDING.

Database before testing (table: orders):

| id (short) | tracking_code | customer_name | status |
|---|---|---|---|
| UUID-1 | CS-0001 | Nguyen Van A | PENDING |

Testing scenario and expected results:

| Scenario | Expected results |
|---|---|
| 1. At AdminHomeView, click "Quản lý đơn hàng". | OrderManagementView appears with the list of orders. |
| 2. Click on order CS-0001. | OrderDetailView appears. Status combo box shows: PENDING, CONFIRMED, CANCELLED. Update button is enabled. |
| 3. In the combo box, select "CANCELLED". | Combo box shows CANCELLED as selected. |
| 4. Click the "Cập nhật" (Update) button. | A dialog appears: "Cập nhật thành công!". Status label updates to "Đã hủy" (CANCELLED). Combo box and Update button are disabled. |
| 5. Click OK on the dialog. | The dialog closes. The order detail reflects the CANCELLED status. |

Database after testing:

| id (short) | tracking_code | customer_name | status |
|---|---|---|---|
| UUID-1 | CS-0001 | Nguyen Van A | CANCELLED |

**Test case No. 4 – exception: admin attempts to change status of an already CANCELLED order.**

Pre-condition: Admin is logged in. Order CS-0003 has status CANCELLED.

Testing scenario and expected results:

| Scenario | Expected results |
|---|---|
| 1. At OrderManagementView, click on order CS-0003. | OrderDetailView appears. Status label shows "Đã hủy" (CANCELLED). The status combo box is disabled (shows only CANCELLED) and the Update button is also disabled. |
| 2. (No further action possible.) | Admin cannot modify the status of a terminated order. |

Database after testing: No change.

### 1.4. Test case for update order status

**Test case No. 6 – standard: admin updates status from PENDING to CONFIRMED.**

Pre-condition: Admin is logged in. Order CS-0001 exists with status PENDING.

Database before testing (table: orders):

| id (short) | tracking_code | customer_name | status |
|---|---|---|---|
| UUID-1 | CS-0001 | Nguyen Van A | PENDING |

Testing scenario and expected results:

| Scenario | Expected results |
|---|---|
| 1. At AdminHomeView, click "Quản lý đơn hàng". | OrderManagementView appears showing CS-0001 with status "Chờ xác nhận" (PENDING). |
| 2. Click on the CS-0001 row. | OrderDetailView appears. Status combo box lists: PENDING, CONFIRMED, CANCELLED. |
| 3. In the combo box, select "CONFIRMED". | Combo box shows CONFIRMED selected. Update button is enabled. |
| 4. Click the "Cập nhật" (Update) button. | A dialog: "Cập nhật thành công!". The status label updates to "Đã xác nhận" (CONFIRMED). |
| 5. Click OK. | Dialog closes. Combo box is repopulated with: CONFIRMED, SHIPPING, CANCELLED. |

Database after testing:

| id (short) | tracking_code | customer_name | status |
|---|---|---|---|
| UUID-1 | CS-0001 | Nguyen Van A | CONFIRMED |

**Test case No. 9 – exception: admin selects the same status as the current status → no update.**

Pre-condition: Admin is logged in. Order CS-0001 has status PENDING.

Testing scenario and expected results:

| Scenario | Expected results |
|---|---|
| 1. Click on CS-0001. | OrderDetailView appears. Combo box shows PENDING selected. |
| 2. In the combo box, select "PENDING" (same as current status). | PENDING is selected. |
| 3. Click the "Cập nhật" (Update) button. | No alert appears. No database change occurs. The condition (newStatus ≠ currentStatus) evaluates to false, so the DAO call is skipped. |

Database after testing: No change.