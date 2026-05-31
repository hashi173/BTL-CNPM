package com.coffeeshop.view.admin;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.ThemeFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.List;

/**
 * StatView - Thống kê doanh thu theo món.
 * 
 * [CNPM] Use Case: Xem thống kê
 * Phụ trách: Bách
 * Mô tả: Giao diện này hiển thị thống kê tổng doanh thu, lọc theo thời gian, 
 * và tìm kiếm món bán chạy. Tương tác trực tiếp với OrderDAO để lấy số liệu từ DB.
 */
public class StatView extends VBox {

    private final Users currentAdmin;
    private final OrderDAO orderDAO = new OrderDAO();
    private TableView<Object[]> table;
    private ComboBox<String> cmbTimeFilter;
    private DatePicker dpFrom, dpTo;
    private TextField txtProductSearch;
    private Label lblBestSeller, lblTotalRevenue;

    public StatView(Users admin) {
        this.currentAdmin = admin;
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(24, 28, 24, 28));
        setSpacing(0);

        getChildren().add(ThemeFX.pageHeader("📈 THỐNG KÊ DOANH THU THEO MÓN"));

        VBox filterCard = ThemeFX.card(16);
        filterCard.setSpacing(10);

        cmbTimeFilter = new ComboBox<>(FXCollections.observableArrayList(
            "Tất cả", "Hôm nay", "Tuần này", "Tháng này", "Tùy chỉnh"
        ));
        cmbTimeFilter.setValue("Tất cả");
        cmbTimeFilter.setPrefHeight(40);

        dpFrom = new DatePicker();
        dpFrom.setPrefHeight(40);
        dpFrom.setDisable(true);

        dpTo = new DatePicker();
        dpTo.setPrefHeight(40);
        dpTo.setDisable(true);

        HBox row1 = new HBox(10,
            ThemeFX.formLabel("Khoảng thời gian:"), cmbTimeFilter,
            ThemeFX.formLabel("Từ:"), dpFrom,
            ThemeFX.formLabel("Đến:"), dpTo
        );
        row1.setAlignment(Pos.CENTER_LEFT);

        txtProductSearch = ThemeFX.textField("Tên món...");
        txtProductSearch.setPrefWidth(160);
        Button btnFilter = ThemeFX.primaryBtn("🔍 Lọc thống kê");
        btnFilter.setPrefWidth(150);
        Button btnDetail = ThemeFX.outlineBtn("📋 Xem chi tiết");
        btnDetail.setPrefWidth(150);

        HBox row2 = new HBox(10,
            ThemeFX.formLabel("Tên món:"), txtProductSearch, btnFilter, btnDetail
        );
        row2.setAlignment(Pos.CENTER_LEFT);

        filterCard.getChildren().addAll(row1, row2);

        VBox filterWrapper = new VBox(filterCard);
        filterWrapper.setPadding(new Insets(0, 0, 16, 0));
        getChildren().add(filterWrapper);

        // ═══ Table với cell value factories ═══
        table = new TableView<>();
        table.setPlaceholder(new Label("Không có dữ liệu"));

        TableColumn<Object[], String> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
        colSTT.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue()[0])));
        colSTT.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        TableColumn<Object[], String> colName = new TableColumn<>("Tên món");
        colName.setPrefWidth(200);
        colName.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue()[0])));

        TableColumn<Object[], String> colQty = new TableColumn<>("Số lượng bán");
        colQty.setPrefWidth(120);
        colQty.setCellValueFactory(cd -> {
            Object val = cd.getValue()[1];
            return new SimpleStringProperty(val != null ? String.valueOf(val) : "0");
        });

        TableColumn<Object[], String> colRevenue = new TableColumn<>("Doanh thu (VND)");
        colRevenue.setPrefWidth(150);
        colRevenue.setCellValueFactory(cd -> {
            Object val = cd.getValue()[2];
            if (val instanceof Number) return new SimpleStringProperty(String.format("%,.0f", ((Number) val).doubleValue()));
            return new SimpleStringProperty(val != null ? val.toString() : "0");
        });

        table.getColumns().addAll(colSTT, colName, colQty, colRevenue);

        // Double-click row to view detail
        table.setRowFactory(tv -> {
            TableRow<Object[]> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    openDetail(row.getItem());
                }
            });
            return row;
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        getChildren().add(table);

        lblBestSeller = ThemeFX.bodyLabel("🏆 Sản phẩm bán chạy nhất: Chưa có dữ liệu");
        lblBestSeller.setStyle("-fx-text-fill: #6C7DF5;");

        lblTotalRevenue = new Label("💰 Tổng doanh thu: 0 VND");
        lblTotalRevenue.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #6C7DF5;");

        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(lblBestSeller, Priority.ALWAYS);
        footer.getChildren().addAll(lblBestSeller, lblTotalRevenue);
        footer.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(footer);

        // Load data mặc định
        loadStatData("", "", "");

        cmbTimeFilter.setOnAction(e -> {
            boolean custom = "Tùy chỉnh".equals(cmbTimeFilter.getValue());
            dpFrom.setDisable(!custom);
            dpTo.setDisable(!custom);
            btnFilter.fire();
        });

        btnFilter.setOnAction(e -> {
            String filter = cmbTimeFilter.getValue();
            String from = "", to = "";
            LocalDate today = orderDAO.getLatestOrderDate();

            if ("Hôm nay".equals(filter)) { from = to = today.toString(); }
            else if ("Tuần này".equals(filter)) {
                from = today.minusDays(today.getDayOfWeek().getValue() - 1).toString();
                to = today.plusDays(7 - today.getDayOfWeek().getValue()).toString();
            } else if ("Tháng này".equals(filter)) {
                from = today.withDayOfMonth(1).toString();
                to = today.withDayOfMonth(today.lengthOfMonth()).toString();
            } else if ("Tùy chỉnh".equals(filter)) {
                if (dpFrom.getValue() != null) from = dpFrom.getValue().toString();
                if (dpTo.getValue() != null) to = dpTo.getValue().toString();
            }

            loadStatData(txtProductSearch.getText().trim(), from, to);
        });

        btnDetail.setOnAction(e -> {
            Object[] selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng chọn một dòng món ăn để xem chi tiết!").showAndWait();
                return;
            }
            openDetail(selected);
        });
    }

    private void openDetail(Object[] selected) {
        String productName = String.valueOf(selected[0]);
        String filter = cmbTimeFilter.getValue();
        String from = "", to = "";
        LocalDate today = orderDAO.getLatestOrderDate();

        if ("Hôm nay".equals(filter)) { from = to = today.toString(); }
        else if ("Tuần này".equals(filter)) {
            from = today.minusDays(today.getDayOfWeek().getValue() - 1).toString();
            to = today.plusDays(7 - today.getDayOfWeek().getValue()).toString();
        } else if ("Tháng này".equals(filter)) {
            from = today.withDayOfMonth(1).toString();
            to = today.withDayOfMonth(today.lengthOfMonth()).toString();
        } else if ("Tùy chỉnh".equals(filter)) {
            if (dpFrom.getValue() != null) from = dpFrom.getValue().toString();
            if (dpTo.getValue() != null) to = dpTo.getValue().toString();
        }

        com.coffeeshop.view.SceneManager.getInstance().openPopup(
            new StatDetailView(currentAdmin, productName, from, to),
            "Chi tiết đơn hàng có món: " + productName, 850, 500
        );
    }

    private void loadStatData(String productName, String fromDate, String toDate) {
        List<Object[]> stats = orderDAO.getStatByProductAndTime(productName, fromDate, toDate);
        table.setItems(FXCollections.observableArrayList(stats));

        double totalRev = 0;
        for (Object[] row : stats) {
            Object val = row[2];
            if (val instanceof Number) totalRev += ((Number) val).doubleValue();
        }

        if (!stats.isEmpty()) {
            Object[] best = stats.get(0);
            lblBestSeller.setText("🏆 Sản phẩm bán chạy nhất: " + best[0] + " (" + best[1] + " ly)");
        } else {
            lblBestSeller.setText("🏆 Sản phẩm bán chạy nhất: Chưa có dữ liệu");
        }

        lblTotalRevenue.setText(String.format("💰 Tổng doanh thu: %,.0f VND", totalRev));
    }
}
