package com.coffeeshop.view.client;

import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * OrderCancelledView - Hiển thị kết quả hủy đơn.
 */
public class OrderCancelledView extends StackPane {

    public OrderCancelledView(Users user, String trackingCode) {
        setStyle("-fx-background-color: linear-gradient(to bottom right, #F5576C15, #FF6B6B15, #F2F4FA);");
        setPadding(new Insets(20));

        VBox card = new VBox(0);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(40, 44, 40, 44));
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(400);

        Label cancelIcon = new Label("🚫");
        cancelIcon.setStyle("-fx-font-size: 52px;");

        Label title = new Label("Đã hủy đơn hàng");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #DC2626;");

        Label codeLabel = ThemeFX.captionLabel("Mã đơn hàng:");
        codeLabel.setAlignment(Pos.CENTER);
        codeLabel.setMaxWidth(Double.MAX_VALUE);

        Label code = new Label(trackingCode);
        code.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #DC2626;");

        Label status = new Label("Trạng thái: ĐÃ HỦY");
        status.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #DC2626;");

        Button btnOrders = ThemeFX.primaryBtn("📋 Xem đơn hàng của tôi");
        btnOrders.setMaxWidth(Double.MAX_VALUE);
        btnOrders.setMinHeight(44);

        Button btnHome = ThemeFX.ghostBtn("🏠 Về trang chủ");

        card.setSpacing(14);
        card.getChildren().addAll(cancelIcon, title, codeLabel, code, status, btnOrders, btnHome);

        getChildren().add(card);

        btnOrders.setOnAction(e -> {
            ((Stage) getScene().getWindow()).close();
            SceneManager.getInstance().switchContent(new OrderListView(user));
        });
        btnHome.setOnAction(e -> {
            ((Stage) getScene().getWindow()).close();
            SceneManager.getInstance().switchContent(new HomeView(user));
        });
    }
}
