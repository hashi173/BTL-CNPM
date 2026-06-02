package com.coffeeshop.view.admin;

import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

/**
 * ManageProductView - Quản lý sản phẩm (Admin).
 * 
 * [CNPM] Use Case: Quản lý sản phẩm
 * Phụ trách: Quỳnh
 * Mô tả: Màn hình hiển thị danh sách sản phẩm, cho phép thêm, sửa, xóa, tìm kiếm sản phẩm.
 */
public class ManageProductView extends VBox {

    private final Users currentAdmin;
    private final ProductDAO productDAO = new ProductDAO();
    private TableView<Products> table;
    private List<Products> productList;
    private static final java.util.Map<String, Image> IMAGE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    public ManageProductView(Users admin) {
        this.currentAdmin = admin;
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(24, 28, 24, 28));
        setSpacing(0);

        TextField txtSearch = ThemeFX.textField("Tìm kiếm...");
        txtSearch.setPrefWidth(220);
        Button btnSearch = ThemeFX.primaryBtn("🔍 Tìm kiếm");
        btnSearch.setPrefWidth(130);
        HBox searchBar = ThemeFX.searchBar("Tên/ID:", txtSearch, btnSearch);

        getChildren().add(ThemeFX.pageHeader("📦 QUẢN LÝ SẢN PHẨM", searchBar));

        table = new TableView<>();
        table.setPlaceholder(new Label("Không có sản phẩm"));

        TableColumn<Products, Integer> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
        colSTT.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        // Image column
        TableColumn<Products, String> colImage = new TableColumn<>("Ảnh");
        colImage.setPrefWidth(60);
        colImage.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getImagePath()));
        colImage.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(36);
                imageView.setFitHeight(36);
                imageView.setPreserveRatio(true);
            }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Image img = IMAGE_CACHE.get(item);
                        if (img == null) {
                            String path = "/com/coffeeshop/resources/" + item;
                            java.net.URL url = getClass().getResource(path);
                            if (url != null) {
                                img = new Image(url.toExternalForm(), true); // load in background
                                IMAGE_CACHE.put(item, img);
                            }
                        }
                        imageView.setImage(img);
                    } catch (Exception e) {
                        imageView.setImage(null);
                    }
                    setGraphic(imageView);
                }
            }
        });

        TableColumn<Products, String> colName = new TableColumn<>("Tên sản phẩm");
        colName.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        colName.setPrefWidth(200);

        TableColumn<Products, String> colCategory = new TableColumn<>("Danh mục");
        colCategory.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getCategoryName()));
        colCategory.setPrefWidth(120);

        TableColumn<Products, String> colPrice = new TableColumn<>("Giá");
        colPrice.setCellValueFactory(cd -> new SimpleStringProperty(String.format("%,.0f", cd.getValue().getBasePrice())));
        colPrice.setPrefWidth(120);

        TableColumn<Products, String> colStatus = new TableColumn<>("Trạng thái");
        colStatus.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().isAvailable() ? "✅ Hoạt động" : "🔒 Bị khóa"));
        colStatus.setPrefWidth(120);

        table.getColumns().addAll(colSTT, colImage, colName, colCategory, colPrice, colStatus);
        VBox.setVgrow(table, Priority.ALWAYS);
        getChildren().add(table);

        Button btnAdd = ThemeFX.successBtn("+ Thêm sản phẩm");
        btnAdd.setPrefWidth(160);
        Button btnEdit = ThemeFX.accentBtn("📝 Chỉnh sửa");
        btnEdit.setPrefWidth(130);
        Button btnDelete = ThemeFX.dangerBtn("❌ Xóa / Khóa");
        btnDelete.setPrefWidth(140);
        HBox buttons = ThemeFX.buttonBar(btnAdd, btnEdit, btnDelete);
        buttons.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(buttons);

        loadAllProducts();

        btnSearch.setOnAction(e -> {
            String kw = txtSearch.getText().trim();
            if (kw.isEmpty()) loadAllProducts();
            else {
                productList = productDAO.searchProduct(kw);
                table.setItems(FXCollections.observableArrayList(productList));
            }
        });

        btnAdd.setOnAction(e -> {
            SceneManager.getInstance().openPopup(
                new EditProductView(currentAdmin, null), "Thêm sản phẩm", 520, 720);
            btnSearch.fire();
        });

        btnEdit.setOnAction(e -> {
            Products sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng chọn sản phẩm để chỉnh sửa!").showAndWait();
                return;
            }
            SceneManager.getInstance().openPopup(
                new EditProductView(currentAdmin, sel.getId()), "Chỉnh sửa sản phẩm", 520, 720);
            btnSearch.fire();
        });

        btnDelete.setOnAction(e -> {
            Products sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng chọn sản phẩm để xóa/kóa!").showAndWait();
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa/kóa sản phẩm: " + sel.getName() + "?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (productDAO.deleteProduct(sel.getId())) {
                        new Alert(Alert.AlertType.INFORMATION, "Đã xử lý thành công!").showAndWait();
                        loadAllProducts();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Có lỗi xảy ra!").showAndWait();
                    }
                }
            });
        });
    }

    private void loadAllProducts() {
        productList = productDAO.getAllProductsAdmin();
        table.setItems(FXCollections.observableArrayList(productList));
    }
}
