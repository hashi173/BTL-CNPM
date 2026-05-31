package com.coffeeshop.view.admin;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.ThemeFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * StatDetailView - Chi tiết đơn hàng theo sản phẩm.
 */
public class StatDetailView extends VBox {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public StatDetailView(Users admin, String productName, String fromDate, String toDate) {
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(20, 24, 20, 24));
        setSpacing(0);

        String timeStr = fromDate.isEmpty() && toDate.isEmpty() ? "Tất cả" : fromDate + " → " + toDate;
        Label lblTime = ThemeFX.captionLabel("📅 Khoảng thời gian: " + timeStr);
        HBox headerRight = new HBox(lblTime);
        headerRight.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        getChildren().add(ThemeFX.pageHeader("📋 ĐƠN HÀNG CÓ MÓN: " + productName.toUpperCase(), headerRight));

        TableView<StatRow> table = new TableView<>();
        table.setPlaceholder(new Label("Không có dữ liệu"));

        TableColumn<StatRow, Integer> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
        colSTT.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        TableColumn<StatRow, String> colCode = new TableColumn<>("Mã đơn");
        colCode.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().trackingCode));
        colCode.setPrefWidth(120);

        TableColumn<StatRow, String> colCustomer = new TableColumn<>("Khách hàng");
        colCustomer.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().customerName));
        colCustomer.setPrefWidth(150);

        TableColumn<StatRow, String> colPhone = new TableColumn<>("SĐT");
        colPhone.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().phone));
        colPhone.setPrefWidth(100);

        TableColumn<StatRow, String> colQty = new TableColumn<>("SL mua");
        colQty.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().quantity)));
        colQty.setPrefWidth(80);

        TableColumn<StatRow, String> colTotal = new TableColumn<>("Tổng tiền (VND)");
        colTotal.setCellValueFactory(cd -> new SimpleStringProperty(String.format("%,.0f", cd.getValue().totalAmount)));
        colTotal.setPrefWidth(120);

        TableColumn<StatRow, String> colStatus = new TableColumn<>("Trạng thái");
        colStatus.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().status));
        colStatus.setPrefWidth(100);

        TableColumn<StatRow, String> colDate = new TableColumn<>("Ngày tạo");
        colDate.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().createdAt));
        colDate.setPrefWidth(150);

        table.getColumns().addAll(colSTT, colCode, colCustomer, colPhone, colQty, colTotal, colStatus, colDate);
        VBox.setVgrow(table, Priority.ALWAYS);
        getChildren().add(table);

        OrderDAO orderDAO = new OrderDAO();
        List<Object[]> ordersList = orderDAO.getOrderDetailsByProductAndTime(productName, fromDate, toDate);

        List<StatRow> rows = new java.util.ArrayList<>();
        for (Object[] o : ordersList) {
            StatRow r = new StatRow();
            r.trackingCode = o[0] != null ? o[0].toString() : "";
            r.customerName = o[1] != null ? o[1].toString() : "";
            r.phone = o[2] != null ? o[2].toString() : "";
            r.totalAmount = o[3] instanceof Double ? (Double) o[3] : 0;
            r.status = ThemeFX.translateStatus(o[4] != null ? o[4].toString() : "");
            r.quantity = o[6] instanceof Integer ? (Integer) o[6] : 0;
            r.createdAt = o[5] instanceof java.util.Date ? sdf.format((java.util.Date) o[5]) : "";
            rows.add(r);
        }
        table.setItems(FXCollections.observableArrayList(rows));

        Button btnClose = ThemeFX.outlineBtn("Đóng");
        HBox btnBar = ThemeFX.buttonBar(btnClose);
        btnBar.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(btnBar);

        btnClose.setOnAction(e -> ((Stage) getScene().getWindow()).close());
    }

    public static class StatRow {
        public String trackingCode;
        public String customerName;
        public String phone;
        public double totalAmount;
        public String status;
        public int quantity;
        public String createdAt;
    }
}
