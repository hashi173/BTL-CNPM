package com.coffeeshop.view.components;

import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.admin.*;
import com.coffeeshop.view.client.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * SidebarView - Dark navy sidebar.
 * Auto-updates active state when content changes externally (quick actions, etc.)
 */
public class SidebarView extends VBox {

    private Button activeButton;
    private final List<Button> navButtons = new ArrayList<>();
    private final List<Class<?>> viewClasses = new ArrayList<>();

    public SidebarView(Users user) {
        getStyleClass().add("sidebar");
        setPrefWidth(250);
        setPadding(new Insets(0));

        // Logo
        Label logo = new Label("☕ COFFEE SHOP");
        logo.getStyleClass().add("sidebar-logo");
        logo.setAlignment(Pos.CENTER);
        logo.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(logo, new Insets(28, 16, 4, 16));

        // User name
        Label userName = new Label(user.getFullName());
        userName.getStyleClass().add("sidebar-user");
        userName.setAlignment(Pos.CENTER);
        userName.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(userName, new Insets(0, 16, 16, 16));

        // Divider
        Region divider1 = new Region();
        divider1.setStyle("-fx-background-color: rgba(255,255,255,0.12); -fx-pref-height: 1; -fx-max-width: 210;");
        VBox.setMargin(divider1, new Insets(0, 16, 12, 16));

        getChildren().addAll(logo, userName, divider1);

        if ("ADMIN".equals(user.getRole())) {
            addNavButton("📊 Tổng quan", () -> new AdminHomeView(user), AdminHomeView.class);
            addNavButton("📋 Quản lý Đơn hàng", () -> new OrderManagementView(user), OrderManagementView.class);
            addNavButton("📦 Quản lý Sản phẩm", () -> new ManageProductView(user), ManageProductView.class);
            addNavButton("📁 Quản lý Danh mục", () -> new ManageCategoryView(user), ManageCategoryView.class);
            addNavButton("📈 Thống kê", () -> new StatView(user), StatView.class);
        } else {
            addNavButton("🏠 Trang chủ", () -> new HomeView(user), HomeView.class);
            addNavButton("☕ Thực đơn", () -> new MenuView(user), MenuView.class);
            addNavButton("🛒 Giỏ hàng", () -> new CartView(user), CartView.class);
            addNavButton("📋 Lịch sử Đơn hàng", () -> new OrderListView(user), OrderListView.class);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);

        Region divider2 = new Region();
        divider2.setStyle("-fx-background-color: rgba(255,255,255,0.12); -fx-pref-height: 1; -fx-max-width: 210;");
        VBox.setMargin(divider2, new Insets(0, 16, 8, 16));
        getChildren().add(divider2);

        Button logout = new Button("🚪 Đăng xuất");
        logout.getStyleClass().addAll("sidebar-button", "logout");
        logout.setMaxWidth(Double.MAX_VALUE);
        logout.setOnAction(e -> SceneManager.getInstance().showLogin());
        getChildren().add(logout);

        // Đăng ký callback — khi switchContent từ bên ngoài, cập nhật active button
        SceneManager.getInstance().setOnContentChange(() -> {
            Node currentContent = null;
            // Lấy content hiện tại từ SceneManager
            try {
                var field = SceneManager.class.getDeclaredField("contentArea");
                field.setAccessible(true);
                StackPane area = (StackPane) field.get(SceneManager.getInstance());
                if (area != null && !area.getChildren().isEmpty()) {
                    currentContent = area.getChildren().get(0);
                }
            } catch (Exception ignored) {}

            if (currentContent != null) {
                updateActiveButton(currentContent.getClass());
            }
        });
    }

    private void addNavButton(String text, Supplier<Node> viewSupplier, Class<?> viewClass) {
        Button btn = new Button(text);
        btn.getStyleClass().add("sidebar-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);

        btn.setOnAction(e -> {
            setActiveButton(btn);
            SceneManager.getInstance().switchContent(viewSupplier.get());
        });

        navButtons.add(btn);
        viewClasses.add(viewClass);
        getChildren().add(btn);

        if (activeButton == null) {
            activeButton = btn;
            btn.getStyleClass().add("active");
        }
    }

    private void setActiveButton(Button btn) {
        if (activeButton != null) activeButton.getStyleClass().remove("active");
        btn.getStyleClass().add("active");
        activeButton = btn;
    }

    /** Cập nhật active button dựa trên class của view hiện tại. */
    private void updateActiveButton(Class<?> viewClass) {
        for (int i = 0; i < viewClasses.size(); i++) {
            if (viewClasses.get(i).equals(viewClass)) {
                setActiveButton(navButtons.get(i));
                return;
            }
        }
    }
}
