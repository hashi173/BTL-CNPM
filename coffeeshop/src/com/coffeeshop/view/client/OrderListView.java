package com.coffeeshop.view.client;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Orders;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * OrderListView - Lịch sử đơn hàng của tôi.
 * 
 * [CNPM] Use Case: Theo dõi đơn hàng
 * Phụ trách: Long
 * Mô tả: Khách hàng xem lại các đơn đã đặt, theo dõi trạng thái và có thể hủy đơn.
 */
public class OrderListView extends VBox {

    private final Users currentUser;
    private final OrderDAO orderDAO = new OrderDAO();
    private TableView<Orders> table;
    private List<Orders> orderList;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public OrderListView(Users user) {
        this.currentUser = user;
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(24, 28, 24, 28));
        setSpacing(0);

        TextField txtSearch = ThemeFX.textField("Tìm mã/ngày...");
        txtSearch.setPrefWidth(200);
        Button btnSearch = ThemeFX.primaryBtn("🔍 Tìm kiếm");
        btnSearch.setPrefWidth(130);
        HBox searchBar = ThemeFX.searchBar("Mã/ngày:", txtSearch, btnSearch);

        getChildren().add(ThemeFX.pageHeader("📋 ĐƠN HÀNG CỦA TÔI", searchBar));

        table = new TableView<>();
        table.setPlaceholder(new Label("Chưa có đơn hàng"));

        TableColumn<Orders, Integer> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
        colSTT.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        TableColumn<Orders, String> colCode = new TableColumn<>("Mã đơn");
        colCode.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTrackingCode()));
        colCode.setPrefWidth(120);

        TableColumn<Orders, String> colDate = new TableColumn<>("Ngày đặt");
        colDate.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getCreatedAt() != null ? sdf.format(cd.getValue().getCreatedAt()) : ""));
        colDate.setPrefWidth(150);

        TableColumn<Orders, String> colTotal = new TableColumn<>("Tổng tiền");
        colTotal.setCellValueFactory(cd -> new SimpleStringProperty(
            String.format("%,.0f VND", cd.getValue().getTotalAmount())));
        colTotal.setPrefWidth(120);

        TableColumn<Orders, String> colStatus = new TableColumn<>("Trạng thái");
        colStatus.setCellValueFactory(cd -> new SimpleStringProperty(ThemeFX.translateStatus(cd.getValue().getStatus())));
        colStatus.setPrefWidth(120);

        table.getColumns().addAll(colSTT, colCode, colDate, colTotal, colStatus);
        VBox.setVgrow(table, Priority.ALWAYS);
        getChildren().add(table);

        Button btnDetail = ThemeFX.accentBtn("👁 Xem chi tiết");
        btnDetail.setPrefWidth(140);
        Button btnCancel = ThemeFX.dangerBtn("❌ Hủy đơn hàng");
        btnCancel.setPrefWidth(150);
        HBox buttons = ThemeFX.buttonBar(btnDetail, btnCancel);
        buttons.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(buttons);

        loadOrders("");

        btnSearch.setOnAction(e -> loadOrders(txtSearch.getText().trim()));

        btnDetail.setOnAction(e -> {
            Orders sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng chọn đơn hàng cần xem!").showAndWait();
                return;
            }
            SceneManager.getInstance().openPopup(
                new ClientOrderDetailView(currentUser, sel.getId()),
                "Chi tiết đơn hàng", 860, 640);
            btnSearch.fire();
        });

        btnCancel.setOnAction(e -> {
            Orders sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng chọn đơn hàng cần hủy!").showAndWait();
                return;
            }
            SceneManager.getInstance().openPopup(
                new CancelConfirmView(currentUser, sel.getId()),
                "Xác nhận hủy đơn", 480, 480);
            btnSearch.fire();
        });
    }

    private void loadOrders(String keyword) {
        orderList = (keyword != null && !keyword.isEmpty())
            ? orderDAO.searchOrdersByUser(currentUser.getId(), keyword)
            : orderDAO.getOrdersByUser(currentUser.getId());
        table.setItems(FXCollections.observableArrayList(orderList));
    }
}
