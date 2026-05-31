package com.coffeeshop.view.client;

import com.coffeeshop.dao.CartDAO;
import com.coffeeshop.model.CartItems;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

/**
 * CartView - Giỏ hàng.
 * 
 * [CNPM] Use Case: Quản lý giỏ hàng
 * Phụ trách: Thi
 * Mô tả: Hiển thị các món ăn khách hàng đã chọn, cho phép xóa món, xem tổng tiền
 * trước khi chuyển qua bước Xác nhận đặt hàng.
 */
public class CartView extends VBox {

    private final Users currentUser;
    private final CartDAO cartDAO = new CartDAO();
    private TableView<CartItems> table;
    private Label lblTotal;

    public CartView(Users user) {
        this.currentUser = user;
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(24, 28, 24, 28));
        setSpacing(0);

        getChildren().add(ThemeFX.pageHeader("🛒 GIỎ HÀNG"));

        table = new TableView<>();
        table.setPlaceholder(new Label("Giỏ hàng trống"));

        TableColumn<CartItems, Integer> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
        colSTT.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        TableColumn<CartItems, String> colName = new TableColumn<>("Tên sản phẩm");
        colName.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getProductName()));
        colName.setPrefWidth(180);

        TableColumn<CartItems, String> colOptions = new TableColumn<>("Tùy chọn");
        colOptions.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getOptions()));
        colOptions.setPrefWidth(200);

        TableColumn<CartItems, String> colQty = new TableColumn<>("SL");
        colQty.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getQuantity())));
        colQty.setPrefWidth(50);

        TableColumn<CartItems, String> colPrice = new TableColumn<>("Đơn giá");
        colPrice.setCellValueFactory(cd -> new SimpleStringProperty(String.format("%,.0f", cd.getValue().getUnitPrice())));
        colPrice.setPrefWidth(100);

        TableColumn<CartItems, String> colTotal = new TableColumn<>("Thành tiền");
        colTotal.setCellValueFactory(cd -> new SimpleStringProperty(String.format("%,.0f", cd.getValue().getTotal())));
        colTotal.setPrefWidth(100);

        table.getColumns().addAll(colSTT, colName, colOptions, colQty, colPrice, colTotal);
        VBox.setVgrow(table, Priority.ALWAYS);
        getChildren().add(table);

        lblTotal = new Label("Tổng: 0 VND");
        lblTotal.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #6C7DF5;");

        Button btnRemove = ThemeFX.outlineBtn("🗑 Xóa món");
        Button btnCheckout = ThemeFX.primaryBtn("💰 Thanh toán");
        btnCheckout.setPrefWidth(140);

        HBox buttons = ThemeFX.buttonBar(btnRemove, btnCheckout);
        VBox footer = new VBox(12, lblTotal, buttons);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(footer);

        loadCart();

        btnRemove.setOnAction(e -> {
            CartItems selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng chọn món cần xóa!").showAndWait();
                return;
            }
            cartDAO.removeCartItem(selected.getId());
            loadCart();
        });

        btnCheckout.setOnAction(e -> {
            List<CartItems> items = cartDAO.getAllCart(currentUser.getId());
            if (items.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Giỏ hàng trống!").showAndWait();
                return;
            }
            SceneManager.getInstance().switchContent(new CheckoutView(currentUser));
        });
    }

    private void loadCart() {
        List<CartItems> items = cartDAO.getAllCart(currentUser.getId());
        table.setItems(FXCollections.observableArrayList(items));
        double total = items.stream().mapToDouble(CartItems::getTotal).sum();
        lblTotal.setText(String.format("Tổng: %,.0f VND", total));
    }
}
