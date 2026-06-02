package com.coffeeshop.view.admin;

import com.coffeeshop.dao.CategoryDAO;
import com.coffeeshop.model.Categories;
import com.coffeeshop.model.Users;
import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.ThemeFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

/**
 * ManageCategoryView - Quản lý danh mục (Admin).
 * 
 * [CNPM] Use Case: Quản lý danh mục
 * Phụ trách: Quỳnh
 * Mô tả: Màn hình hiển thị và quản lý các danh mục sản phẩm (thêm, sửa, xóa).
 */
public class ManageCategoryView extends VBox {

    private final Users currentAdmin;
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private TableView<Categories> table;
    private List<Categories> categoryList;

    public ManageCategoryView(Users admin) {
        this.currentAdmin = admin;
        setStyle("-fx-background-color: #F2F4FA;");
        setPadding(new Insets(24, 28, 24, 28));
        setSpacing(0);

        TextField txtSearch = ThemeFX.textField("Tìm kiếm...");
        txtSearch.setPrefWidth(200);
        Button btnSearch = ThemeFX.primaryBtn("🔍 Tìm kiếm");
        btnSearch.setPrefWidth(130);
        HBox searchBar = ThemeFX.searchBar("Tên/ID:", txtSearch, btnSearch);

        getChildren().add(ThemeFX.pageHeader("📁 QUẢN LÝ DANH MỤC", searchBar));

        table = new TableView<>();
        table.setPlaceholder(new Label("Không có danh mục"));

        TableColumn<Categories, Integer> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
        colSTT.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        TableColumn<Categories, String> colName = new TableColumn<>("Tên danh mục");
        colName.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        colName.setPrefWidth(200);

        TableColumn<Categories, String> colDesc = new TableColumn<>("Mô tả");
        colDesc.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getDescription()));
        colDesc.setPrefWidth(300);

        table.getColumns().addAll(colSTT, colName, colDesc);
        VBox.setVgrow(table, Priority.ALWAYS);
        getChildren().add(table);

        Button btnAdd = ThemeFX.successBtn("+ Thêm danh mục");
        btnAdd.setPrefWidth(160);
        Button btnEdit = ThemeFX.accentBtn("📝 Chỉnh sửa");
        btnEdit.setPrefWidth(130);
        Button btnDelete = ThemeFX.dangerBtn("❌ Xóa");
        btnDelete.setPrefWidth(90);
        HBox buttons = ThemeFX.buttonBar(btnAdd, btnEdit, btnDelete);
        buttons.setPadding(new Insets(16, 0, 0, 0));
        getChildren().add(buttons);

        loadAll();

        btnSearch.setOnAction(e -> {
            String kw = txtSearch.getText().trim();
            if (kw.isEmpty()) loadAll();
            else {
                categoryList = categoryDAO.searchCategories(kw);
                table.setItems(FXCollections.observableArrayList(categoryList));
            }
        });

        btnAdd.setOnAction(e -> {
            SceneManager.getInstance().openPopup(
                new AddCategoryView(currentAdmin, null), "Thêm danh mục", 480, 560);
            btnSearch.fire();
        });

        btnEdit.setOnAction(e -> {
            Categories sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Chọn danh mục cần sửa!").showAndWait();
                return;
            }
            SceneManager.getInstance().openPopup(
                new AddCategoryView(currentAdmin, sel.getId()), "Chỉnh sửa danh mục", 480, 560);
            btnSearch.fire();
        });

        btnDelete.setOnAction(e -> {
            Categories sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Chọn danh mục cần xóa!").showAndWait();
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận xóa danh mục?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (categoryDAO.deleteCategory(sel.getId())) {
                        new Alert(Alert.AlertType.INFORMATION, "Xóa thành công!").showAndWait();
                        loadAll();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Không thể xóa danh mục đang có sản phẩm!").showAndWait();
                    }
                }
            });
        });
    }

    private void loadAll() {
        categoryList = categoryDAO.getAllCategories();
        table.setItems(FXCollections.observableArrayList(categoryList));
    }
}
