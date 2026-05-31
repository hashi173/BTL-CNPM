package com.coffeeshop.view.client;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Orders;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.UUID;

/**
 * CancelConfirmView - Xác nhận hủy đơn hàng.
 */
public class CancelConfirmView extends StackPane {

    private final Users currentUser;
    private final OrderDAO orderDAO = new OrderDAO();

    public CancelConfirmView(Users user, UUID orderId) {
        this.currentUser = user;
        setStyle("-fx-background-color: linear-gradient(to bottom right, #F5576C15, #FF6B6B15, #F2F4FA);");
        setPadding(new Insets(20));

        VBox card = new VBox(0);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(36, 40, 36, 40));
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(420);

        Label warningIcon = new Label("⚠️");
        warningIcon.setStyle("-fx-font-size: 40px;");

        Label title = new Label("Xác nhận hủy đơn hàng");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #DC2626;");

        Orders order = orderDAO.getOrderDetail(orderId);
        Label lblCode = ThemeFX.bodyLabel("Mã: " + (order != null ? order.getTrackingCode() : ""));
        lblCode.setStyle("-fx-font-weight: bold;");

        Label lblStatus = ThemeFX.bodyLabel("Trạng thái: " + (order != null ? ThemeFX.translateStatus(order.getStatus()) : ""));
        Label lblTotal = new Label(order != null ? String.format("Tổng tiền: %,.0f VND", order.getTotalAmount()) : "");
        lblTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: #DC2626;");

        ComboBox<String> cmbReason = new ComboBox<>(FXCollections.observableArrayList(
            "Đổi ý không muốn mua nữa", "Đặt nhầm sản phẩm", "Thay đổi địa chỉ giao hàng",
            "Thời gian giao hàng quá lâu", "Lý do khác"
        ));
        cmbReason.setValue("Đổi ý không muốn mua nữa");
        cmbReason.setPrefWidth(320);

        Button btnBack = ThemeFX.outlineBtn("← Quay lại");
        Button btnConfirm = ThemeFX.dangerBtn("❌ Xác nhận hủy");
        btnConfirm.setPrefWidth(160);
        HBox buttons = ThemeFX.centeredButtonBar(btnBack, btnConfirm);

        card.setSpacing(12);
        card.getChildren().addAll(warningIcon, title, lblCode, lblStatus, lblTotal,
            ThemeFX.formField("Lý do hủy:", cmbReason), buttons);

        getChildren().add(card);

        btnBack.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        btnConfirm.setOnAction(e -> {
            String status = orderDAO.checkOrderStatus(orderId);
            if (status == null) {
                new Alert(Alert.AlertType.ERROR, "Không tìm thấy đơn hàng!").showAndWait();
                return;
            }
            if ("COMPLETED".equals(status) || "SHIPPING".equals(status) || "CANCELLED".equals(status)) {
                new Alert(Alert.AlertType.WARNING, "Không thể hủy đơn ở trạng thái: " + ThemeFX.translateStatus(status)).showAndWait();
                return;
            }
            if (orderDAO.cancelOrder(orderId)) {
                new Alert(Alert.AlertType.INFORMATION, "Đã hủy đơn hàng thành công!").showAndWait();
                ((Stage) getScene().getWindow()).close();
                SceneManager.getInstance().openPopup(
                    new OrderCancelledView(currentUser, order.getTrackingCode()),
                    "Đã hủy đơn hàng", 480, 420);
            } else {
                new Alert(Alert.AlertType.ERROR, "Hủy đơn hàng thất bại!").showAndWait();
            }
        });
    }
}
