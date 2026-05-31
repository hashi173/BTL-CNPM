package com.coffeeshop.view.client;

import com.coffeeshop.dao.*;
import com.coffeeshop.model.*;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * CheckoutView - Xác nhận đặt hàng.
 * 
 * [CNPM] Use Case: Đặt hàng
 * Phụ trách: Thi
 * Mô tả: Màn hình cho phép khách hàng nhập thông tin nhận hàng, tính tổng tiền,
 * và tiến hành thanh toán (Lưu vào Database thông qua OrderDAO và OrderItemDAO).
 */
public class CheckoutView extends VBox {

    private final Users currentUser;
    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();

    public CheckoutView(Users user) {
        this.currentUser = user;
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(24, 28, 24, 28));
        setSpacing(0);

        getChildren().add(ThemeFX.pageHeader("📦 XÁC NHẬN ĐẶT HÀNG"));

        VBox card = ThemeFX.card(24);
        card.setSpacing(14);

        TextField txtName = ThemeFX.textField("Tên người nhận");
        txtName.setText(user.getFullName());
        TextField txtPhone = ThemeFX.textField("Số điện thoại");
        txtPhone.setText(user.getPhone() != null ? user.getPhone() : "");
        TextField txtAddress = ThemeFX.textField("Địa chỉ nhận hàng");
        TextField txtNote = ThemeFX.textField("Ghi chú đơn hàng");

        card.getChildren().addAll(
            ThemeFX.formField("Tên người nhận:", txtName),
            ThemeFX.formField("Số điện thoại:", txtPhone),
            ThemeFX.formField("Địa chỉ nhận hàng *:", txtAddress),
            ThemeFX.formField("Ghi chú:", txtNote)
        );

        double total = cartDAO.getTotalAmount(currentUser.getId());
        Label lblTotal = new Label(String.format("💰 Tổng thanh toán: %,.0f VND", total));
        lblTotal.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #6C7DF5;");
        lblTotal.setPadding(new Insets(12, 0, 0, 0));
        card.getChildren().add(lblTotal);

        VBox cardWrapper = new VBox(card);
        cardWrapper.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(cardWrapper);

        Button btnBack = ThemeFX.outlineBtn("← Quay lại");
        Button btnConfirm = ThemeFX.successBtn("✅ Xác nhận đặt hàng");
        btnConfirm.setPrefWidth(200);

        HBox buttons = ThemeFX.buttonBar(btnBack, btnConfirm);
        buttons.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(buttons);

        btnBack.setOnAction(e -> SceneManager.getInstance().switchContent(new CartView(currentUser)));

        btnConfirm.setOnAction(e -> {
            String address = txtAddress.getText().trim();
            if (address.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Vui lòng nhập địa chỉ nhận hàng!").showAndWait();
                return;
            }
            List<CartItems> items = cartDAO.getAllCart(currentUser.getId());
            if (items.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Giỏ hàng trống!").showAndWait();
                return;
            }
            double totalAmount = cartDAO.getTotalAmount(currentUser.getId());
            Orders order = Orders.createOrder(currentUser.getId(), txtName.getText().trim(),
                txtPhone.getText().trim(), address, txtNote.getText().trim(), totalAmount);
            Orders created = orderDAO.createOrder(order);
            if (created != null) {
                for (CartItems ci : items) {
                    OrderItems oi = new OrderItems();
                    oi.setOrderId(created.getId());
                    oi.setProductId(ci.getProductId());
                    oi.setSnapshotProductName(ci.getProductName());
                    oi.setQuantity(ci.getQuantity());
                    oi.setSnapshotUnitPrice(BigDecimal.valueOf(ci.getUnitPrice()));
                    oi.setSnapshotOptions(ci.getOptions() != null ? ci.getOptions() : "");
                    oi.setSubTotal(BigDecimal.valueOf(ci.getTotal()));
                    orderItemDAO.addOrderItem(oi);
                }
                cartDAO.clearCart(currentUser.getId());
                SceneManager.getInstance().openPopup(
                    new OrderView(currentUser, created.getTrackingCode()),
                    "Đặt hàng thành công", 480, 420);
                SceneManager.getInstance().switchContent(new OrderListView(currentUser));
            } else {
                new Alert(Alert.AlertType.ERROR, "Đặt hàng thất bại!").showAndWait();
            }
        });
    }
}
