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
 * OrderView - Hiển thị mã tracking sau đặt hàng thành công.
 */
public class OrderView extends StackPane {

    public OrderView(Users user, String trackingCode) {
        setStyle("-fx-background-color: linear-gradient(to bottom right, #24C6DC15, #51E89815, #F2F4FA);");
        setPadding(new Insets(20));

        VBox card = new VBox(0);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(40, 44, 40, 44));
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(400);

        Label successIcon = new Label("🎉");
        successIcon.setStyle("-fx-font-size: 52px;");

        Label title = new Label("Đặt hàng thành công!");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #059669;");

        Label codeLabel = ThemeFX.captionLabel("Mã đơn hàng của bạn:");
        codeLabel.setAlignment(Pos.CENTER);
        codeLabel.setMaxWidth(Double.MAX_VALUE);

        Label code = new Label(trackingCode);
        code.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #6C7DF5;");

        Label note = ThemeFX.captionLabel("Vui lòng lưu lại mã để theo dõi đơn hàng");
        note.setAlignment(Pos.CENTER);
        note.setMaxWidth(Double.MAX_VALUE);

        Button btnTrack = ThemeFX.primaryBtn("📋 Theo dõi đơn hàng");
        btnTrack.setMaxWidth(Double.MAX_VALUE);
        btnTrack.setMinHeight(44);

        Button btnHome = ThemeFX.ghostBtn("🏠 Về trang chủ");

        card.setSpacing(14);
        card.getChildren().addAll(successIcon, title, codeLabel, code, note, btnTrack, btnHome);
        getChildren().add(card);

        btnTrack.setOnAction(e -> {
            ((Stage) getScene().getWindow()).close();
            SceneManager.getInstance().switchContent(new OrderListView(user));
        });
        btnHome.setOnAction(e -> {
            ((Stage) getScene().getWindow()).close();
            SceneManager.getInstance().switchContent(new HomeView(user));
        });
    }
}
