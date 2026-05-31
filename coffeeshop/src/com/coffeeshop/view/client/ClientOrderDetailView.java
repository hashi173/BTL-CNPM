package com.coffeeshop.view.client;

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
 * ClientOrderDetailView - Chi tiết đơn hàng (Khách hàng).
 */
public class ClientOrderDetailView extends VBox {

    public ClientOrderDetailView(Users user, UUID orderId) {
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(20, 24, 20, 24));
        setSpacing(0);

        getChildren().add(ThemeFX.pageHeader("📋 CHI TIẾT ĐƠN HÀNG"));

        OrderDAO orderDAO = new OrderDAO();
        Orders order = orderDAO.getOrderDetail(orderId);

        HBox infoCards = new HBox(12);
        infoCards.setPadding(new Insets(0, 0, 16, 0));

        if (order != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            VBox orderCard = ThemeFX.card(16);
            orderCard.setSpacing(8);
            orderCard.getChildren().addAll(
                ThemeFX.subtitleLabel("📦 Thông tin đơn hàng"),
                ThemeFX.bodyLabel("Mã: " + (order.getTrackingCode() != null ? order.getTrackingCode() : "")),
                ThemeFX.bodyLabel("Ngày đặt: " + (order.getCreatedAt() != null ? sdf.format(order.getCreatedAt()) : "")),
                ThemeFX.statusBadge(ThemeFX.translateStatus(order.getStatus()))
            );

            VBox cusCard = ThemeFX.card(16);
            cusCard.setSpacing(8);
            cusCard.getChildren().addAll(
                ThemeFX.subtitleLabel("👤 Thông tin nhận hàng"),
                ThemeFX.bodyLabel("Tên: " + order.getCustomerName()),
                ThemeFX.bodyLabel("SĐT: " + order.getPhone()),
                ThemeFX.bodyLabel("Địa chỉ: " + order.getAddressText()),
                ThemeFX.bodyLabel("Ghi chú: " + (order.getNote() != null ? order.getNote() : ""))
            );

            HBox.setHgrow(orderCard, Priority.ALWAYS);
            HBox.setHgrow(cusCard, Priority.ALWAYS);
            infoCards.getChildren().addAll(orderCard, cusCard);
        }

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

        OrderItemDAO itemDAO = new OrderItemDAO();
        List<OrderItems> items = itemDAO.getOrderItems(orderId);
        table.setItems(FXCollections.observableArrayList(items));

        VBox.setVgrow(table, Priority.ALWAYS);
        getChildren().add(table);

        Button btnClose = ThemeFX.outlineBtn("Đóng");
        HBox btnBar = ThemeFX.buttonBar(btnClose);
        btnBar.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(btnBar);

        btnClose.setOnAction(e -> ((Stage) getScene().getWindow()).close());
    }
}
