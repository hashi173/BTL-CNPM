package com.coffeeshop.view.admin;

import com.coffeeshop.dao.CategoryDAO;
import com.coffeeshop.model.Categories;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.ThemeFX;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.UUID;

/**
 * AddCategoryView - Thêm/sửa danh mục.
 */
public class AddCategoryView extends VBox {

    private final Users currentAdmin;
    private final UUID categoryId;
    private final boolean isAddMode;
    private Categories category;
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public AddCategoryView(Users admin, UUID categoryId) {
        this.currentAdmin = admin;
        this.categoryId = categoryId;
        this.isAddMode = (categoryId == null);
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(20, 24, 20, 24));
        setSpacing(0);

        getChildren().add(ThemeFX.pageHeader(isAddMode ? "THÊM DANH MỤC" : "CHỈNH SỬA DANH MỤC"));

        VBox card = ThemeFX.card(20);
        card.setSpacing(12);

        TextField txtName = ThemeFX.textField("Tên danh mục");
        TextArea txtDesc = new TextArea();
        txtDesc.setPromptText("Mô tả danh mục");
        txtDesc.setPrefRowCount(4);
        txtDesc.setWrapText(true);

        card.getChildren().addAll(
                ThemeFX.formField("Tên danh mục *:", txtName),
                ThemeFX.formField("Mô tả:", txtDesc));

        VBox cardWrapper = new VBox(card);
        cardWrapper.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(cardWrapper);

        Button btnBack = ThemeFX.outlineBtn("← Quay lại");
        Button btnSave = ThemeFX.primaryBtn("💾 Lưu thay đổi");
        btnSave.setPrefWidth(160);
        HBox buttons = ThemeFX.buttonBar(btnBack, btnSave);
        buttons.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(buttons);

        if (!isAddMode) {
            category = categoryDAO.getCategoryById(categoryId);
            if (category != null) {
                txtName.setText(category.getName());
                txtDesc.setText(category.getDescription());
            }
        }

        btnBack.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        btnSave.setOnAction(e -> {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Tên danh mục không được để trống!").showAndWait();
                return;
            }
            Categories c = isAddMode ? new Categories() : category;
            c.setName(name);
            c.setDescription(txtDesc.getText().trim());

            boolean ok = isAddMode ? categoryDAO.addCategory(c) : categoryDAO.updateCategory(c);
            if (ok) {
                new Alert(Alert.AlertType.INFORMATION, isAddMode ? "Thêm thành công!" : "Cập nhật thành công!")
                        .showAndWait();
                ((Stage) getScene().getWindow()).close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Lưu danh mục thất bại!").showAndWait();
            }
        });
    }
}
