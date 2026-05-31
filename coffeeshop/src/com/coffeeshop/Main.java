package com.coffeeshop;

import com.coffeeshop.view.SceneManager;
import com.coffeeshop.view.auth.LoginView;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main - Entry point cho Coffee Shop (JavaFX + AtlantaFX).
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Apply AtlantaFX theme BEFORE creating any scene
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        SceneManager.getInstance().setPrimaryStage(primaryStage);

        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView, 520, 620);

        String css = getClass().getResource("/com/coffeeshop/resources/stylesheet.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("Coffee Shop");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
