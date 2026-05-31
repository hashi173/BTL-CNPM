package com.coffeeshop.view.admin;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Orders;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.ThemeFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;

/**
 * AdminHomeView - Dashboard admin với stat cards, chart, top selling, recent orders.
 * Dữ liệu lấy từ database thực.
 */
public class AdminHomeView extends ScrollPane {

    private final OrderDAO orderDAO = new OrderDAO();

    public AdminHomeView(Users user) {
        setFitToWidth(true);
        setFitToHeight(false);
        setStyle("-fx-background-color: #F2F4FA; -fx-background: #F2F4FA;");

        VBox content = new VBox(0);
        content.setStyle("-fx-background-color: #F2F4FA;");
        content.setPadding(new Insets(24, 28, 24, 28));
        content.setSpacing(0);

        content.getChildren().add(ThemeFX.pageHeader("📊 BẢNG ĐIỀU KHIỂN"));

        // Welcome
        VBox welcomeBox = new VBox(4);
        welcomeBox.setPadding(new Insets(8, 0, 20, 0));
        Label welcome = new Label("Xin chào, " + user.getFullName() + " 👋");
        welcome.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");
        Label subtitle = new Label("Chào mừng bạn đến với Coffee Shop Dashboard");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #9CA3AF;");
        welcomeBox.getChildren().addAll(welcome, subtitle);
        content.getChildren().add(welcomeBox);

        // Stat Cards (real data)
        int totalOrders = orderDAO.getTotalOrders();
        double totalRevenue = orderDAO.getTotalRevenue();
        double todayRevenue = orderDAO.getTodayRevenue();
        int todayOrders = orderDAO.getTodayOrders();

        HBox statRow = new HBox(16);
        statRow.setPadding(new Insets(0, 0, 24, 0));
        statRow.getChildren().addAll(
            ThemeFX.statCard("Doanh thu hôm nay", String.format("%,.0fđ", todayRevenue), "stat-card-blue"),
            ThemeFX.statCard("Doanh thu tổng", String.format("%,.0fđ", totalRevenue), "stat-card-green"),
            ThemeFX.statCard("Tổng đơn hàng", String.valueOf(totalOrders), "stat-card-pink"),
            ThemeFX.statCard("Đơn hôm nay", String.valueOf(todayOrders), "stat-card-orange")
        );
        for (int i = 0; i < 4; i++) HBox.setHgrow(statRow.getChildren().get(i), Priority.ALWAYS);
        content.getChildren().add(statRow);

        // Content Row: Chart + Top Selling
        HBox contentRow = new HBox(16);
        contentRow.setPadding(new Insets(0, 0, 24, 0));

        VBox chartCard = createChartCard();
        HBox.setHgrow(chartCard, Priority.ALWAYS);

        VBox topSellingCard = createTopSellingCard();
        HBox.setHgrow(topSellingCard, Priority.ALWAYS);

        contentRow.getChildren().addAll(chartCard, topSellingCard);
        content.getChildren().add(contentRow);

        // Recent Orders
        content.getChildren().add(createRecentOrdersCard());

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

    private VBox createChartCard() {
        VBox card = ThemeFX.card(20);
        card.setSpacing(12);

        Label title = new Label("📈 Doanh thu theo tháng");
        title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tháng");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Triệu VND");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setCreateSymbols(true);
        chart.setLegendVisible(false);
        chart.setAnimated(true);
        chart.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        chart.setPrefHeight(240);

        // Lấy data từ database
        List<Object[]> monthlyData = orderDAO.getMonthlyRevenue();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        double[] monthlyRev = new double[12];
        for (Object[] row : monthlyData) {
            int month = ((Number) row[0]).intValue() - 1;
            double rev = ((Number) row[1]).doubleValue() / 1_000_000;
            if (month >= 0 && month < 12) monthlyRev[month] = rev;
        }
        for (int i = 0; i < 12; i++) {
            series.getData().add(new XYChart.Data<>(months[i], monthlyRev[i]));
        }

        chart.getData().add(series);
        card.getChildren().addAll(title, chart);
        return card;
    }

    private VBox createTopSellingCard() {
        VBox card = ThemeFX.card(20);
        card.setSpacing(10);

        Label title = new Label("🏆 Sản phẩm bán chạy");
        title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

        List<Object[]> topProducts = orderDAO.getTopSellingProducts(5);

        VBox list = new VBox(8);
        int rank = 1;
        for (Object[] item : topProducts) {
            String name = (String) item[0];
            int qty = ((Number) item[1]).intValue();
            String imagePath = (String) item[3];

            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("action-card");

            Label rankLabel = new Label(String.valueOf(rank));
            rankLabel.getStyleClass().add("rank-badge");
            if (rank == 2) rankLabel.getStyleClass().add("rank-badge-silver");
            if (rank >= 3) rankLabel.getStyleClass().add("rank-badge-bronze");

            ImageView img = new ImageView();
            img.setFitWidth(32);
            img.setFitHeight(32);
            img.setPreserveRatio(true);
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    String path = "/com/coffeeshop/resources/" + imagePath;
                    java.net.URL url = getClass().getResource(path);
                    if (url != null) img.setImage(new Image(url.toExternalForm()));
                } catch (Exception ignored) {}
            }

            Label nameLabel = new Label(name);
            nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");
            nameLabel.setMinWidth(Region.USE_PREF_SIZE);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label qtyLabel = new Label(qty + " ly");
            qtyLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #6C7DF5;");
            qtyLabel.setMinWidth(Region.USE_PREF_SIZE);

            row.getChildren().addAll(rankLabel, img, nameLabel, spacer, qtyLabel);
            list.getChildren().add(row);
            rank++;
        }

        if (topProducts.isEmpty()) {
            Label empty = new Label("Chưa có dữ liệu");
            empty.setStyle("-fx-text-fill: #9CA3AF;");
            list.getChildren().add(empty);
        }

        card.getChildren().addAll(title, list);
        return card;
    }

    private VBox createRecentOrdersCard() {
        VBox card = ThemeFX.card(20);
        card.setSpacing(12);

        Label title = new Label("📋 Đơn hàng gần đây");
        title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

        TableView<Orders> table = new TableView<>();
        table.setPrefHeight(200);
        table.setPlaceholder(new Label("Chưa có đơn hàng"));

        TableColumn<Orders, String> colCode = new TableColumn<>("Mã đơn");
        colCode.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTrackingCode()));
        colCode.setPrefWidth(100);

        TableColumn<Orders, String> colCustomer = new TableColumn<>("Khách hàng");
        colCustomer.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getCustomerName()));
        colCustomer.setPrefWidth(150);

        TableColumn<Orders, String> colTotal = new TableColumn<>("Tổng tiền");
        colTotal.setCellValueFactory(cd -> new SimpleStringProperty(String.format("%,.0fđ", cd.getValue().getTotalAmount())));
        colTotal.setPrefWidth(120);

        TableColumn<Orders, String> colStatus = new TableColumn<>("Trạng thái");
        colStatus.setCellValueFactory(cd -> new SimpleStringProperty(ThemeFX.translateStatus(cd.getValue().getStatus())));
        colStatus.setPrefWidth(120);

        table.getColumns().addAll(colCode, colCustomer, colTotal, colStatus);

        List<Orders> recentOrders = orderDAO.getRecentOrders(8);
        table.setItems(FXCollections.observableArrayList(recentOrders));

        card.getChildren().addAll(title, table);
        return card;
    }
}
