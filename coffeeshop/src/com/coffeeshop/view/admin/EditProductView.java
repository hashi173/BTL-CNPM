package com.coffeeshop.view.admin;

import com.coffeeshop.dao.CategoryDAO;
import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.model.Categories;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.ThemeFX;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * EditProductView - Thêm/sửa sản phẩm.
 */
public class EditProductView extends VBox {

    private final Users currentAdmin;
    private final UUID productId;
    private final boolean isAddMode;
    private Products product;
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    private static class CategoryItem {
        UUID id;
        String name;

        CategoryItem(UUID id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public EditProductView(Users admin, UUID productId) {
        this.currentAdmin = admin;
        this.productId = productId;
        this.isAddMode = (productId == null);
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(20, 24, 20, 24));
        setSpacing(0);

        getChildren().add(ThemeFX.pageHeader(isAddMode ? "THÊM SẢN PHẨM" : "CHỈNH SỬA SẢN PHẨM"));

        VBox card = ThemeFX.card(20);
        card.setSpacing(12);

        TextField txtName = ThemeFX.textField("Tên sản phẩm");
        ComboBox<CategoryItem> cmbCategory = new ComboBox<>();
        cmbCategory.setPrefHeight(40);
        TextField txtPrice = ThemeFX.textField("Giá cơ bản");
        TextArea txtDesc = new TextArea();
        txtDesc.setPromptText("Mô tả sản phẩm");
        txtDesc.setPrefRowCount(3);
        txtDesc.setWrapText(true);
        TextField txtImagePath = ThemeFX.textField("Đường dẫn ảnh (VD: images/espresso.jpg)");
        CheckBox chkAvailable = new CheckBox("Đang hoạt động");
        chkAvailable.setSelected(true);

        ImageView imgPreview = new ImageView();
        imgPreview.setFitWidth(65);
        imgPreview.setFitHeight(65);
        imgPreview.setPreserveRatio(true);
        imgPreview.setStyle("-fx-border-color: #D1D5DB; -fx-border-width: 1; -fx-border-radius: 4;");

        HBox imageFieldBox = new HBox(12);
        imageFieldBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        VBox imgField = ThemeFX.formField("Ảnh sản phẩm:", txtImagePath);
        HBox.setHgrow(imgField, Priority.ALWAYS);
        imageFieldBox.getChildren().addAll(imgField, imgPreview);

        card.getChildren().addAll(
                ThemeFX.formField("Tên sản phẩm *:", txtName),
                ThemeFX.formField("Danh mục:", cmbCategory),
                ThemeFX.formField("Giá cơ bản *:", txtPrice),
                ThemeFX.formField("Mô tả:", txtDesc),
                imageFieldBox,
                chkAvailable);

        VBox cardWrapper = new VBox(card);
        cardWrapper.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(cardWrapper);

        Button btnBack = ThemeFX.outlineBtn("← Quay lại");
        Button btnSave = ThemeFX.primaryBtn("💾 Lưu thay đổi");
        btnSave.setPrefWidth(160);
        HBox buttons = ThemeFX.buttonBar(btnBack, btnSave);
        buttons.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(buttons);

        List<Categories> cats = categoryDAO.getAllCategories();
        for (Categories c : cats)
            cmbCategory.getItems().add(new CategoryItem(c.getId(), c.getName()));

        txtImagePath.textProperty().addListener((obs, oldVal, newVal) -> {
            updateImagePreview(newVal, imgPreview);
        });

        if (!isAddMode) {
            product = productDAO.getProductDetail(productId);
            if (product != null) {
                txtName.setText(product.getName());
                txtPrice.setText(product.getBasePrice() != null ? product.getBasePrice().toString() : "");
                txtDesc.setText(product.getDescription());
                txtImagePath.setText(product.getImagePath() != null ? product.getImagePath() : "");
                updateImagePreview(txtImagePath.getText(), imgPreview);
                chkAvailable.setSelected(product.isAvailable());
                if (product.getCategoryId() != null) {
                    for (CategoryItem ci : cmbCategory.getItems()) {
                        if (ci.id.equals(product.getCategoryId())) {
                            cmbCategory.setValue(ci);
                            break;
                        }
                    }
                }
            }
        }

        btnBack.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        btnSave.setOnAction(e -> {
            try {
                String name = txtName.getText().trim();
                String priceStr = txtPrice.getText().trim();
                if (name.isEmpty() || priceStr.isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Vui lòng nhập tên và giá sản phẩm!").showAndWait();
                    return;
                }
                BigDecimal price = new BigDecimal(priceStr);
                Products p = isAddMode ? new Products() : product;
                p.setName(name);
                p.setBasePrice(price);
                p.setDescription(txtDesc.getText().trim());
                p.setImagePath(txtImagePath.getText().trim().isEmpty() ? null : txtImagePath.getText().trim());
                p.setAvailable(chkAvailable.isSelected());
                CategoryItem selCat = cmbCategory.getValue();
                if (selCat != null)
                    p.setCategoryId(selCat.id);

                boolean ok = isAddMode ? productDAO.addProduct(p) : productDAO.updateProduct(p);
                if (ok) {
                    new Alert(Alert.AlertType.INFORMATION, isAddMode ? "Thêm thành công!" : "Cập nhật thành công!")
                            .showAndWait();
                    ((Stage) getScene().getWindow()).close();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Lưu dữ liệu thất bại!").showAndWait();
                }
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Giá phải là một số hợp lệ!").showAndWait();
            }
        });
    }

    private void updateImagePreview(String imagePath, ImageView imgPreview) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            imgPreview.setImage(null);
            return;
        }
        try {
            String path = "/com/coffeeshop/resources/" + imagePath.trim();
            java.net.URL url = getClass().getResource(path);
            if (url != null) {
                imgPreview.setImage(new Image(url.toExternalForm()));
            } else {
                imgPreview.setImage(null);
            }
        } catch (Exception e) {
            imgPreview.setImage(null);
        }
    }
}
