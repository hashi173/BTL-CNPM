package com.coffeeshop.view.client;

import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * MenuView - Thực đơn sản phẩm với hình ảnh (classpath loading).
 */
public class MenuView extends VBox {

    private final Users currentUser;
    private final ProductDAO productDAO = new ProductDAO();
    private TableView<Products> table;
    private static final java.util.Map<String, Image> IMAGE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    public MenuView(Users user) {
        this.currentUser = user;
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(24, 28, 24, 28));
        setSpacing(0);

        TextField txtSearch = ThemeFX.textField("Tìm sản phẩm...");
        txtSearch.setPrefWidth(220);
        Button btnSearch = ThemeFX.primaryBtn("🔍 Tìm kiếm");
        btnSearch.setPrefWidth(130);

        HBox searchBar = ThemeFX.searchBar("Tìm đồ:", txtSearch, btnSearch);
        getChildren().add(ThemeFX.pageHeader("☕ THỰC ĐƠN", searchBar));

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

        // Image column — load từ classpath
        TableColumn<Products, String> colImage = new TableColumn<>("Ảnh");
        colImage.setPrefWidth(60);
        colImage.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getImagePath()));
        colImage.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
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
        colCategory.setPrefWidth(130);

        TableColumn<Products, String> colPrice = new TableColumn<>("Giá (VND)");
        colPrice.setCellValueFactory(cd -> new SimpleStringProperty(String.format("%,.0f", cd.getValue().getBasePrice())));
        colPrice.setPrefWidth(120);

        TableColumn<Products, String> colStatus = new TableColumn<>("Trạng thái");
        colStatus.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().isAvailable() ? "✅ Còn hàng" : "❌ Hết hàng"));
        colStatus.setPrefWidth(100);

        table.getColumns().addAll(colSTT, colImage, colName, colCategory, colPrice, colStatus);

        table.setRowFactory(tv -> {
            TableRow<Products> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    Products p = row.getItem();
                    SceneManager.getInstance().openPopup(
                        new ProductDetailView(currentUser, p.getId()),
                        "Chi tiết sản phẩm", 540, 640);
                }
            });
            return row;
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        getChildren().add(table);

        Label hint = ThemeFX.captionLabel("💡 Double-click vào sản phẩm để xem chi tiết và thêm vào giỏ hàng");
        hint.setPadding(new Insets(12, 0, 0, 4));
        getChildren().add(hint);

        loadProducts("");
        btnSearch.setOnAction(e -> loadProducts(txtSearch.getText().trim()));
    }

    private void loadProducts(String keyword) {
        List<Products> list = (keyword != null && !keyword.isEmpty())
            ? productDAO.searchProduct(keyword)
            : productDAO.getAllProducts();
        table.setItems(FXCollections.observableArrayList(list));
    }
}
