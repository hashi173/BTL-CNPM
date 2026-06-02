package com.coffeeshop.view.admin;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.dao.OrderItemDAO;
import com.coffeeshop.model.*;
import com.coffeeshop.view.ThemeFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

/**
 * OrderDetailView - Chi tiết đơn hàng (Admin).
 * 
 * [CNPM] Use Case: Quản lý đơn hàng (xem chi tiết, cập nhật trạng thái)
 * Phụ trách: Hà
 * Mô tả: Admin xem chi tiết sản phẩm, thông tin khách hàng của một đơn,
 * và thay đổi trạng thái (VD: PENDING -> CONFIRMED -> SHIPPING -> COMPLETED).
 */
public class OrderDetailView extends VBox {

    private final Users currentAdmin;
    private final UUID orderId;
    private Orders order;
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO itemDAO = new OrderItemDAO();
    private Label lblStatus;
    private ComboBox<String> cmbStatus;
    private Button btnUpdate;

    public OrderDetailView(Users admin, UUID orderId) {
        this.currentAdmin = admin;
        this.orderId = orderId;
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(20, 24, 20, 24));
        setSpacing(0);

        getChildren().add(ThemeFX.pageHeader("📋 CHI TIẾT ĐƠN HÀNG"));

        HBox infoCards = new HBox(12);
        infoCards.setPadding(new Insets(0, 0, 16, 0));

        VBox orderCard = ThemeFX.card(16);
        orderCard.setSpacing(8);
        Label lblCode = ThemeFX.bodyLabel("Mã: ");
        lblCode.setStyle("-fx-font-weight: bold;");
        Label lblDate = ThemeFX.bodyLabel("Ngày đặt: ");
        lblStatus = ThemeFX.bodyLabel("Trạng thái: ");
        orderCard.getChildren().addAll(ThemeFX.subtitleLabel("📦 Thông tin đơn hàng"), lblCode, lblDate, lblStatus);

        VBox cusCard = ThemeFX.card(16);
        cusCard.setSpacing(8);
        Label lblName = ThemeFX.bodyLabel("Tên: ");
        Label lblPhone = ThemeFX.bodyLabel("SĐT: ");
        Label lblAddress = ThemeFX.bodyLabel("Địa chỉ: ");
        Label lblNote = ThemeFX.bodyLabel("Ghi chú: ");
        cusCard.getChildren().addAll(ThemeFX.subtitleLabel("👤 Thông tin khách hàng"), lblName, lblPhone, lblAddress, lblNote);

        HBox.setHgrow(orderCard, Priority.ALWAYS);
        HBox.setHgrow(cusCard, Priority.ALWAYS);
        infoCards.getChildren().addAll(orderCard, cusCard);
        getChildren().add(infoCards);

        TableView<OrderItems> table = new TableView<>();
        table.setPlaceholder(new Label("Không có sản phẩm"));

        TableColumn<OrderItems, Integer> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
        colSTT.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        TableColumn<OrderItems, String> colName = new TableColumn<>("Tên sản phẩm");
        colName.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSnapshotProductName()));
        colName.setPrefWidth(180);

        TableColumn<OrderItems, String> colOptions = new TableColumn<>("Tùy chọn");
        colOptions.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSnapshotOptions()));
        colOptions.setPrefWidth(200);
        colOptions.setCellFactory(col -> new TableCell<>() {
            private final Label label = new Label();
            {
                label.setWrapText(true);
                label.prefWidthProperty().bind(col.widthProperty().subtract(10));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(label);
                }
            }
        });

        TableColumn<OrderItems, String> colQty = new TableColumn<>("SL");
        colQty.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getQuantity())));
        colQty.setPrefWidth(60);

        TableColumn<OrderItems, String> colPrice = new TableColumn<>("Đơn giá");
        colPrice.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSnapshotUnitPrice().toString()));
        colPrice.setPrefWidth(100);

        TableColumn<OrderItems, String> colSubtotal = new TableColumn<>("Thành tiền");
        colSubtotal.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSubTotal().toString()));
        colSubtotal.setPrefWidth(100);

        table.getColumns().addAll(colSTT, colName, colOptions, colQty, colPrice, colSubtotal);
        VBox.setVgrow(table, Priority.ALWAYS);
        getChildren().add(table);

        HBox footer = new HBox(12);
        footer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(16, 0, 0, 0));

        Label lblUpdate = ThemeFX.formLabel("Cập nhật trạng thái:");
        cmbStatus = new ComboBox<>();
        cmbStatus.setPrefWidth(160);
        cmbStatus.setPrefHeight(40);

        btnUpdate = ThemeFX.successBtn("💾 Cập nhật");
        btnUpdate.setPrefWidth(130);

        Button btnBack = ThemeFX.outlineBtn("← Quay lại");

        footer.getChildren().addAll(lblUpdate, cmbStatus, btnUpdate, btnBack);
        getChildren().add(footer);

        loadOrderDetail();

        btnBack.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        btnUpdate.setOnAction(e -> {
            String newStatus = cmbStatus.getValue();
            if (newStatus != null && !newStatus.equals(order.getStatus())) {
                if (orderDAO.updateOrderStatus(orderId, newStatus)) {
                    new Alert(Alert.AlertType.INFORMATION, "Cập nhật thành công!").showAndWait();
                    lblStatus.setText("Trạng thái: " + ThemeFX.translateStatus(newStatus));
                    order.setStatus(newStatus);
                    setupStatusCombo(newStatus);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Cập nhật thất bại!").showAndWait();
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void loadOrderDetail() {
        order = orderDAO.getOrderDetail(orderId);
        if (order != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            VBox orderCard = (VBox) ((HBox) getChildren().get(1)).getChildren().get(0);
            ((Label) orderCard.getChildren().get(1)).setText("Mã: " + order.getTrackingCode());
            ((Label) orderCard.getChildren().get(2)).setText("Ngày đặt: " + (order.getCreatedAt() != null ? sdf.format(order.getCreatedAt()) : ""));
            lblStatus.setText("Trạng thái: " + ThemeFX.translateStatus(order.getStatus()));

            VBox cusCard = (VBox) ((HBox) getChildren().get(1)).getChildren().get(1);
            ((Label) cusCard.getChildren().get(1)).setText("Tên: " + order.getCustomerName());
            ((Label) cusCard.getChildren().get(2)).setText("SĐT: " + order.getPhone());
            ((Label) cusCard.getChildren().get(3)).setText("Địa chỉ: " + order.getAddressText());
            ((Label) cusCard.getChildren().get(4)).setText("Ghi chú: " + (order.getNote() != null ? order.getNote() : ""));

            setupStatusCombo(order.getStatus());

            List<OrderItems> items = itemDAO.getOrderItems(orderId);
            TableView<OrderItems> table = (TableView<OrderItems>) getChildren().get(2);
            table.setItems(FXCollections.observableArrayList(items));
        }
    }

    private void setupStatusCombo(String currentStatus) {
        cmbStatus.getItems().clear();
        if ("PENDING".equals(currentStatus)) {
            cmbStatus.getItems().addAll("PENDING", "CONFIRMED", "CANCELLED");
        } else if ("CONFIRMED".equals(currentStatus)) {
            cmbStatus.getItems().addAll("CONFIRMED", "SHIPPING", "CANCELLED");
        } else if ("SHIPPING".equals(currentStatus)) {
            cmbStatus.getItems().addAll("SHIPPING", "COMPLETED", "CANCELLED");
        } else {
            cmbStatus.getItems().add(currentStatus);
            cmbStatus.setDisable(true);
            btnUpdate.setDisable(true);
        }
        cmbStatus.setValue(currentStatus);
    }
}
