package com.coffeeshop.view.client;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.Users;
import com.coffeeshop.service.RecommendationService;
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
    private final RecommendationService recService = RecommendationService.getInstance();

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
        int todayOrders = orderDAO.getTodayOrders();

        HBox statRow = new HBox(16);
        statRow.setPadding(new Insets(0, 0, 24, 0));
        statRow.getChildren().addAll(
            ThemeFX.statCard("Đơn hôm nay", String.valueOf(todayOrders), "stat-card-blue"),
            ThemeFX.statCard("Tổng đơn của bạn", String.valueOf(totalOrders), "stat-card-green"),
            ThemeFX.statCard("Chi tiêu tháng", String.format("%,.0fđ", monthlySpending), "stat-card-orange")
        );
        for (int i = 0; i < 3; i++) HBox.setHgrow(statRow.getChildren().get(i), Priority.ALWAYS);
        content.getChildren().add(statRow);

        // ═══ GỢI Ý CHO BẠN (AI Recommendation) ═══
        content.getChildren().add(createRecommendationsCard(user));

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

    /**
     * Gợi ý sản phẩm cho bạn — sử dụng thuật toán Collaborative Filtering + Rule-based.
     */
    private VBox createRecommendationsCard(Users user) {
        VBox card = ThemeFX.card(20);
        card.setSpacing(12);

        Label title = new Label("✨ Gợi ý cho bạn");
        title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

        Label subtitle = new Label("Dựa trên sở thích và lịch sử đặt hàng của bạn");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #9CA3AF;");
        subtitle.setPadding(new Insets(0, 0, 4, 0));

        // Gọi thuật toán AI
        List<Products> recommendations = recService.getRecommendations(user.getId());

        if (recommendations.isEmpty()) {
            Label empty = new Label("Đang tải gợi ý...");
            empty.setStyle("-fx-text-fill: #9CA3AF;");
            card.getChildren().addAll(title, subtitle, empty);
            return card;
        }

        // Hiển thị dạng card ngang (scrollable)
        HBox productRow = new HBox(12);
        productRow.setPadding(new Insets(4, 0, 0, 0));

        for (Products p : recommendations) {
            VBox productCard = new VBox(6);
            productCard.getStyleClass().add("action-card");
            productCard.setAlignment(Pos.CENTER);
            productCard.setPadding(new Insets(12));
            productCard.setPrefWidth(140);
            productCard.setMaxWidth(140);
            productCard.setOnMouseClicked(e -> {
                SceneManager.getInstance().openPopup(
                    new ProductDetailView(user, p.getId()),
                    "Chi tiết sản phẩm", 540, 640);
            });

            // Ảnh sản phẩm
            ImageView img = new ImageView();
            img.setFitWidth(80);
            img.setFitHeight(80);
            img.setPreserveRatio(true);
            if (p.getImagePath() != null && !p.getImagePath().isEmpty()) {
                try {
                    String path = "/com/coffeeshop/resources/" + p.getImagePath();
                    java.net.URL url = getClass().getResource(path);
                    if (url != null) img.setImage(new Image(url.toExternalForm()));
                } catch (Exception ignored) {}
            }

            Label name = new Label(p.getName());
            name.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");
            name.setWrapText(true);
            name.setAlignment(Pos.CENTER);
            name.setMaxWidth(120);

            Label price = new Label(String.format("%,.0fđ", p.getBasePrice()));
            price.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #6C7DF5;");

            productCard.getChildren().addAll(img, name, price);
            productRow.getChildren().add(productCard);
        }

        ScrollPane scrollRow = new ScrollPane(productRow);
        scrollRow.setFitToWidth(true);
        scrollRow.setFitToHeight(true);
        scrollRow.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollRow.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollRow.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0;");
        scrollRow.setPrefHeight(160);

        card.getChildren().addAll(title, subtitle, scrollRow);
        return card;
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
