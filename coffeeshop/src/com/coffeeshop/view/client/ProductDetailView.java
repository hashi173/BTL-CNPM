package com.coffeeshop.view.client;

import com.coffeeshop.dao.CartDAO;
import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.model.CartItems;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.UUID;

/**
 * ProductDetailView - Chi tiết sản phẩm + Cross-selling "Bạn có thể cũng thích".
 */
public class ProductDetailView extends VBox {

    private final Users currentUser;
    private Products product;
    private final ProductDAO productDAO = new ProductDAO();
    private final CartDAO cartDAO = new CartDAO();

    public ProductDetailView(Users user, UUID productId) {
        this.currentUser = user;
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(16, 20, 16, 20));
        setSpacing(0);

        Button btnBack = ThemeFX.ghostBtn("← Quay lại");
        HBox header = new HBox(8, ThemeFX.titleLabel("☕ CHI TIẾT SẢN PHẨM"), btnBack);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 12, 0));
        getChildren().add(header);

        // ─── Main content (scrollable) ───────────────────────────
        VBox mainContent = new VBox(0);
        mainContent.setSpacing(0);

        // Product card
        VBox card = ThemeFX.card(20);
        card.setSpacing(10);

        // Product image
        ImageView imgProduct = new ImageView();
        imgProduct.setFitWidth(120);
        imgProduct.setFitHeight(120);
        imgProduct.setPreserveRatio(true);
        VBox.setMargin(imgProduct, new Insets(0, 0, 8, 0));
        card.getChildren().add(imgProduct);

        Label lblName = new Label();
        lblName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");
        Label lblCategory = ThemeFX.bodyLabel("");
        Label lblPrice = new Label();
        lblPrice.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #6C7DF5;");
        Label lblDesc = ThemeFX.bodyLabel("");

        card.getChildren().addAll(
            ThemeFX.formField("Tên:", lblName),
            ThemeFX.formField("Danh mục:", lblCategory),
            ThemeFX.formField("Giá:", lblPrice),
            ThemeFX.formField("Mô tả:", lblDesc)
        );

        Separator sep = new Separator();
        sep.setPadding(new Insets(8, 0, 8, 0));
        card.getChildren().add(sep);

        Spinner<Integer> spnQty = new Spinner<>(1, 99, 1);
        spnQty.setPrefWidth(100);
        ComboBox<String> cmbSugar = new ComboBox<>(FXCollections.observableArrayList(
            "100% đường", "70% đường", "50% đường", "30% đường", "Không đường"));
        cmbSugar.setValue("100% đường");
        ComboBox<String> cmbIce = new ComboBox<>(FXCollections.observableArrayList(
            "100% đá", "70% đá", "50% đá", "Ít đá", "Không đá"));
        cmbIce.setValue("100% đá");
        TextField txtNote = ThemeFX.textField("Ghi chú thêm...");

        card.getChildren().addAll(
            ThemeFX.formField("Số lượng:", spnQty),
            ThemeFX.formField("Mức đường:", cmbSugar),
            ThemeFX.formField("Mức đá:", cmbIce),
            ThemeFX.formField("Ghi chú:", txtNote)
        );

        mainContent.getChildren().add(card);

        ScrollPane scroll = new ScrollPane(mainContent);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        getChildren().add(scroll);

        // ─── Add to cart button ───────────────────────────────────
        Button btnAdd = ThemeFX.primaryBtn("🛒 Thêm vào giỏ hàng");
        btnAdd.setPrefWidth(200);
        HBox btnBar = ThemeFX.centeredButtonBar(btnAdd);
        btnBar.setPadding(new Insets(12, 0, 0, 0));
        getChildren().add(btnBar);

        // ─── Load product data ────────────────────────────────────
        product = productDAO.getProductDetail(productId);
        if (product != null) {
            lblName.setText(product.getName());
            lblCategory.setText(product.getCategoryName() != null ? product.getCategoryName() : "");
            lblPrice.setText(String.format("%,.0f VND", product.getBasePrice()));
            lblDesc.setText(product.getDescription() != null ? product.getDescription() : "");

            if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
                try {
                    String path = "/com/coffeeshop/resources/" + product.getImagePath();
                    java.net.URL url = getClass().getResource(path);
                    if (url != null) imgProduct.setImage(new Image(url.toExternalForm()));
                } catch (Exception ignored) {}
            }
        }

        btnBack.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        btnAdd.setOnAction(e -> {
            if (product == null) return;
            String options = "Đường: " + cmbSugar.getValue() + ", Đá: " + cmbIce.getValue();
            if (!txtNote.getText().trim().isEmpty()) options += ", Ghi chú: " + txtNote.getText().trim();

            CartItems item = new CartItems();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setUnitPrice(product.getBasePrice().doubleValue());
            item.setQuantity(spnQty.getValue());
            item.setOptions(options);

            cartDAO.addCartItem(item, currentUser.getId());
            new Alert(Alert.AlertType.INFORMATION, "Đã thêm \"" + product.getName() + "\" vào giỏ hàng!").showAndWait();
            ((Stage) getScene().getWindow()).close();
            SceneManager.getInstance().switchContent(new CartView(currentUser));
        });
    }
}
