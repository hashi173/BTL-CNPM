package com.coffeeshop.view.client;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;

/**
 * HomeView - Trang chủ khách hàng với stat cards, gợi ý AI, quick actions.
 */
public class HomeView extends ScrollPane {

    private final OrderDAO orderDAO = new OrderDAO();

    public HomeView(Users user) {
        setFitToWidth(true);
        setFitToHeight(false);
        setStyle("-fx-background-color: #F2F4FA; -fx-background: #F2F4FA;");

        VBox content = new VBox(0);
        content.setStyle("-fx-background-color: #F2F4FA;");
        content.setPadding(new Insets(24, 28, 24, 28));
        content.setSpacing(0);

        content.getChildren().add(ThemeFX.pageHeader("🏠 TRANG CHỦ"));

        // Welcome
        VBox welcomeBox = new VBox(4);
        welcomeBox.setPadding(new Insets(8, 0, 20, 0));
        Label welcome = new Label("Hello, " + user.getFullName() + " 👋");
        welcome.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");
        Label subtitle = new Label("Chào mừng bạn đến với Coffee Shop");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #9CA3AF;");
        welcomeBox.getChildren().addAll(welcome, subtitle);
        content.getChildren().add(welcomeBox);

        // Stat Cards
        int totalOrders = orderDAO.getOrdersByUserCount(user.getId());
        double monthlySpending = orderDAO.getUserMonthlySpending(user.getId());
        int todayOrders = orderDAO.getTodayOrdersByUser(user.getId());

        HBox statRow = new HBox(16);
        statRow.setPadding(new Insets(0, 0, 24, 0));
        statRow.getChildren().addAll(
            ThemeFX.statCard("Đơn hôm nay", String.valueOf(todayOrders), "stat-card-blue"),
            ThemeFX.statCard("Tổng đơn của bạn", String.valueOf(totalOrders), "stat-card-green"),
            ThemeFX.statCard("Chi tiêu tháng", String.format("%,.0fđ", monthlySpending), "stat-card-orange")
        );
        for (int i = 0; i < 3; i++) HBox.setHgrow(statRow.getChildren().get(i), Priority.ALWAYS);
        content.getChildren().add(statRow);
        // Quick Actions
        content.getChildren().add(createQuickActionsCard(user));

        setContent(content);

        // Speed up vertical scrolling for smoother UX
        content.setOnScroll(event -> {
            if (event.getDeltaY() != 0) {
                double deltaY = event.getDeltaY() * 2.5;
                double height = content.getBoundsInLocal().getHeight();
                double vvalue = getVvalue();
                setVvalue(vvalue - deltaY / height);
                event.consume();
            }
        });
    }

    private VBox createQuickActionsCard(Users user) {
        VBox actionsCard = ThemeFX.card(20);
        actionsCard.setSpacing(12);

        Label actionsTitle = new Label("⚡ Thao tác nhanh");
        actionsTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

        HBox actionsRow = new HBox(12);

        VBox btn1 = makeActionCard("☕", "Xem thực đơn");
        btn1.setOnMouseClicked(e -> SceneManager.getInstance().switchContent(new MenuView(user)));

        VBox btn2 = makeActionCard("🛒", "Giỏ hàng");
        btn2.setOnMouseClicked(e -> SceneManager.getInstance().switchContent(new CartView(user)));

        VBox btn3 = makeActionCard("📋", "Đơn hàng của tôi");
        btn3.setOnMouseClicked(e -> SceneManager.getInstance().switchContent(new OrderListView(user)));

        HBox.setHgrow(btn1, Priority.ALWAYS);
        HBox.setHgrow(btn2, Priority.ALWAYS);
        HBox.setHgrow(btn3, Priority.ALWAYS);

        actionsRow.getChildren().addAll(btn1, btn2, btn3);
        actionsCard.getChildren().addAll(actionsTitle, actionsRow);
        return actionsCard;
    }

    private VBox makeActionCard(String icon, String text) {
        VBox card = new VBox(6);
        card.getStyleClass().add("action-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16));

        Label lblIcon = new Label(icon);
        lblIcon.setStyle("-fx-font-size: 28px;");

        Label lblText = new Label(text);
        lblText.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");

        card.getChildren().addAll(lblIcon, lblText);
        return card;
    }
}
