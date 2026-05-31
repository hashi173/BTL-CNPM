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
 * RegisterView - Màn hình đăng ký tài khoản.
 */
public class RegisterView extends StackPane {

    private final UserDAO userDAO = new UserDAO();

    public RegisterView() {
        setStyle("-fx-background-color: linear-gradient(to bottom right, #24C6DC22, #51E89822, #F2F4FA);");
        setPadding(new Insets(16));

        VBox card = new VBox(0);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(32, 36, 32, 36));
        card.setPrefWidth(420);
        card.setMaxWidth(420);

        Label logo = new Label("✨");
        logo.setStyle("-fx-font-size: 44px;");
        logo.setAlignment(Pos.CENTER);
        logo.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("Tạo tài khoản mới");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #24C6DC;");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        Label subtitle = new Label("Điền thông tin để đăng ký");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #9CA3AF;");
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setMaxWidth(Double.MAX_VALUE);

        VBox header = new VBox(4, logo, title, subtitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        TextField txtUsername = ThemeFX.textField("Tên đăng nhập");
        PasswordField txtPassword = ThemeFX.passwordField("Mật khẩu");
        PasswordField txtConfirm = ThemeFX.passwordField("Nhập lại mật khẩu");
        TextField txtFullName = ThemeFX.textField("Họ và tên");
        TextField txtEmail = ThemeFX.textField("Email");
        TextField txtPhone = ThemeFX.textField("Số điện thoại");

        VBox form = new VBox(12,
            ThemeFX.formField("Tên đăng nhập *", txtUsername),
            ThemeFX.formField("Mật khẩu *", txtPassword),
            ThemeFX.formField("Nhập lại mật khẩu *", txtConfirm),
            ThemeFX.formField("Họ và tên *", txtFullName),
            ThemeFX.formField("Email", txtEmail),
            ThemeFX.formField("Số điện thoại", txtPhone)
        );

        Label lblError = new Label(" ");
        lblError.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 12px;");
        lblError.setMinHeight(20);

        Button btnRegister = ThemeFX.successBtn("Đăng ký");
        btnRegister.setMaxWidth(Double.MAX_VALUE);
        btnRegister.setMinHeight(44);

        Button btnLogin = ThemeFX.ghostBtn("Đã có tài khoản? Đăng nhập");

        VBox buttons = new VBox(10, btnRegister, btnLogin);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(16, 0, 0, 0));

        VBox formSection = new VBox(14, form, lblError, buttons);
        ScrollPane scroll = new ScrollPane(formSection);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");

        card.getChildren().addAll(header, scroll);
        getChildren().add(card);

        btnRegister.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText();
            String confirm = txtConfirm.getText();
            String fullName = txtFullName.getText().trim();

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                lblError.setText("Vui lòng nhập đầy đủ Tên đăng nhập, Mật khẩu và Họ tên!");
                return;
            }
            if (!password.equals(confirm)) {
                lblError.setText("Mật khẩu nhập lại không khớp!");
                return;
            }
            if (userDAO.checkUsernameExist(username)) {
                lblError.setText("Tên đăng nhập đã tồn tại!");
                return;
            }

            Users newUser = new Users();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setFullName(fullName);
            newUser.setEmail(txtEmail.getText().trim());
            newUser.setPhone(txtPhone.getText().trim());

            if (userDAO.registerUser(newUser)) {
                new Alert(Alert.AlertType.INFORMATION, "Đăng ký thành công! Vui lòng đăng nhập.").showAndWait();
                SceneManager.getInstance().getPrimaryStage().getScene().setRoot(new LoginView());
            } else {
                lblError.setText("Đăng ký thất bại, vui lòng thử lại!");
            }
        });

        btnLogin.setOnAction(e -> {
            SceneManager.getInstance().getPrimaryStage().getScene().setRoot(new LoginView());
        });
    }
}
