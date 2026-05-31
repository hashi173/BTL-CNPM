package com.coffeeshop.view;

import com.coffeeshop.model.Users;
import com.coffeeshop.view.auth.LoginView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * SceneManager - Centralized navigation controller.
 */
public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;
    private StackPane contentArea;
    private Users currentUser;
    private Runnable onContentChange;

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    public void setPrimaryStage(Stage stage) { this.primaryStage = stage; }
    public Stage getPrimaryStage() { return primaryStage; }

    public void setContentArea(StackPane area) { this.contentArea = area; }

    public Users getCurrentUser() { return currentUser; }
    public void setCurrentUser(Users user) { this.currentUser = user; }

    /** Đăng ký callback khi nội dung thay đổi (dùng cho SidebarView). */
    public void setOnContentChange(Runnable callback) { this.onContentChange = callback; }

    /** Swap the center content. */
    public void switchContent(Node panel) {
        if (contentArea != null) {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(panel);
        }
        if (onContentChange != null) onContentChange.run();
    }

    /** Open a popup window (modal). */
    public void openPopup(Parent view, String title, double width, double height) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(primaryStage);
        popup.setTitle(title);

        String css = getClass().getResource("/com/coffeeshop/resources/stylesheet.css").toExternalForm();
        Scene scene = new Scene(view, width, height);
        scene.getStylesheets().add(css);
        popup.setScene(scene);
        popup.setResizable(false);
        popup.show();
    }

    /** Navigate to the dashboard (after login). */
    public void showDashboard(Users user) {
        this.currentUser = user;
        DashboardView dashboard = new DashboardView(user);
        Scene scene = new Scene(dashboard, 1100, 700);
        String css = getClass().getResource("/com/coffeeshop/resources/stylesheet.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.centerOnScreen();
    }

    /** Navigate back to login. */
    public void showLogin() {
        this.currentUser = null;
        this.contentArea = null;
        this.onContentChange = null;
        LoginView login = new LoginView();
        Scene scene = new Scene(login, 520, 620);
        String css = getClass().getResource("/com/coffeeshop/resources/stylesheet.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
    }
}
