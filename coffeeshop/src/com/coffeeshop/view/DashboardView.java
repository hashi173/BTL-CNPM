package com.coffeeshop.view;

import com.coffeeshop.model.Users;
import com.coffeeshop.view.components.SidebarView;
import com.coffeeshop.view.client.HomeView;
import com.coffeeshop.view.admin.AdminHomeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * DashboardView - Main shell with sidebar + swappable content area.
 */
public class DashboardView extends BorderPane {

    private final StackPane contentArea;

    public DashboardView(Users user) {
        setStyle("-fx-background-color: #FAFAF9;");

        // Sidebar
        SidebarView sidebar = new SidebarView(user);
        setLeft(sidebar);

        // Content area
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #FAFAF9;");
        setCenter(contentArea);

        // Register content area with SceneManager
        SceneManager.getInstance().setContentArea(contentArea);

        // Load default view
        if ("ADMIN".equals(user.getRole())) {
            contentArea.getChildren().add(new AdminHomeView(user));
        } else {
            contentArea.getChildren().add(new HomeView(user));
        }
    }
}
