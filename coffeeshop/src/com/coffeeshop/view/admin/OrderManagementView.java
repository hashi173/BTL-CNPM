package com.coffeeshop.view.admin;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Orders;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * OrderManagementView - Quản lý đơn hàng (Admin).
 * 
 * [CNPM] Use Case: Quản lý đơn hàng
 * Phụ trách: Hà
 * Mô tả: Màn hình hiển thị danh sách tất cả đơn hàng của hệ thống.
 * Cho phép Admin lọc, tìm kiếm và chọn đơn hàng để xử lý chi tiết.
 */
public class OrderManagementView extends VBox {

    private final Users currentAdmin;
    private final OrderDAO orderDAO = new OrderDAO();
    private TableView<Orders> table;
    private List<Orders> orderList;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public OrderManagementView(Users admin) {
        this.currentAdmin = admin;
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(24, 28, 24, 28));
        setSpacing(0);

        TextField txtSearch = ThemeFX.textField("Tìm kiếm...");
        txtSearch.setPrefWidth(220);
        Button btnSearch = ThemeFX.primaryBtn("🔍 Tìm kiếm");
        btnSearch.setPrefWidth(130);
        HBox searchBar = ThemeFX.searchBar("Tên/SĐT/Mã:", txtSearch, btnSearch);

        getChildren().add(ThemeFX.pageHeader("🧾 QUẢN LÝ ĐƠN HÀNG", searchBar));

        table = new TableView<>();
        table.setPlaceholder(new Label("Không có đơn hàng"));

        TableColumn<Orders, Integer> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
        colSTT.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        TableColumn<Orders, String> colCode = new TableColumn<>("Mã đơn hàng");
        colCode.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTrackingCode()));
        colCode.setPrefWidth(120);

        TableColumn<Orders, String> colCustomer = new TableColumn<>("Khách hàng");
        colCustomer.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getCustomerName()));
        colCustomer.setPrefWidth(180);

        TableColumn<Orders, String> colDate = new TableColumn<>("Ngày đặt");
        colDate.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getCreatedAt() != null ? sdf.format(cd.getValue().getCreatedAt()) : ""));
        colDate.setPrefWidth(150);

        TableColumn<Orders, String> colTotal = new TableColumn<>("Tổng tiền");
        colTotal.setCellValueFactory(cd -> new SimpleStringProperty(
            String.format("%,.0f", cd.getValue().getTotalAmount())));
        colTotal.setPrefWidth(120);

        TableColumn<Orders, String> colStatus = new TableColumn<>("Trạng thái");
        colStatus.setCellValueFactory(cd -> new SimpleStringProperty(ThemeFX.translateStatus(cd.getValue().getStatus())));
        colStatus.setPrefWidth(120);

        table.getColumns().addAll(colSTT, colCode, colCustomer, colDate, colTotal, colStatus);
        VBox.setVgrow(table, Priority.ALWAYS);
        getChildren().add(table);

        Button btnDetail = ThemeFX.accentBtn("👁 Xem chi tiết / Xử lý");
        btnDetail.setPrefWidth(200);
        HBox buttons = ThemeFX.buttonBar(btnDetail);
        buttons.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(buttons);

        loadAllOrders();

        btnSearch.setOnAction(e -> {
            String kw = txtSearch.getText().trim();
            if (kw.isEmpty()) loadAllOrders();
            else {
                orderList = orderDAO.searchOrders(kw);
                table.setItems(FXCollections.observableArrayList(orderList));
            }
        });

        btnDetail.setOnAction(e -> {
            Orders sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng chọn đơn hàng để xem!").showAndWait();
                return;
            }
            SceneManager.getInstance().openPopup(
                new OrderDetailView(currentAdmin, sel.getId()),
                "Chi tiết đơn hàng", 860, 640);
        });
    }

    private void loadAllOrders() {
        orderList = orderDAO.getAllOrders();
        table.setItems(FXCollections.observableArrayList(orderList));
    }
}
