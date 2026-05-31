package com.coffeeshop.view.auth;

import com.coffeeshop.dao.UserDAO;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * LoginView - Màn hình đăng nhập hiện đại.
 */
public class LoginView extends StackPane {

    private final UserDAO userDAO = new UserDAO();

    public LoginView() {
        setStyle("-fx-background-color: linear-gradient(to bottom right, #6C7DF522, #24C6DC22, #F2F4FA);");
        setPadding(new Insets(20));

        VBox card = new VBox(0);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(40));
        card.setPrefWidth(400);
        card.setMaxWidth(400);
        card.setAlignment(Pos.TOP_CENTER);

        Label logo = new Label("☕");
        logo.setStyle("-fx-font-size: 52px;");
        logo.setAlignment(Pos.CENTER);
        logo.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("COFFEE SHOP");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #6C7DF5;");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        Label subtitle = new Label("Đăng nhập để tiếp tục");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #9CA3AF;");
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setMaxWidth(Double.MAX_VALUE);

        VBox topSection = new VBox(4, logo, title, subtitle);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(0, 0, 28, 0));

        TextField txtUsername = ThemeFX.textField("Tên đăng nhập");
        PasswordField txtPassword = ThemeFX.passwordField("Mật khẩu");

        VBox formBox = new VBox(16,
            ThemeFX.formField("Tên đăng nhập", txtUsername),
            ThemeFX.formField("Mật khẩu", txtPassword)
        );

        Label lblError = new Label(" ");
        lblError.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 12px;");
        lblError.setMinHeight(20);

        Button btnLogin = ThemeFX.primaryBtn("Đăng nhập");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setMinHeight(44);

        Button btnRegister = ThemeFX.ghostBtn("Chưa có tài khoản? Đăng ký ngay");

        VBox bottomSection = new VBox(10, btnLogin, btnRegister);
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setPadding(new Insets(20, 0, 0, 0));

        card.getChildren().addAll(topSection, formBox, lblError, bottomSection);
        getChildren().add(card);

        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                lblError.setText("Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            Users user = userDAO.checkLogin(username, password);
            if (user != null) {
                SceneManager.getInstance().showDashboard(user);
            } else {
                lblError.setText("Tên đăng nhập hoặc mật khẩu không đúng!");
                txtPassword.clear();
            }
        });

        btnRegister.setOnAction(e -> {
            SceneManager.getInstance().getPrimaryStage().getScene().setRoot(new RegisterView());
        });
    }
}
