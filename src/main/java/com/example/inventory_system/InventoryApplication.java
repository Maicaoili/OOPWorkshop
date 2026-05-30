package com.example.inventory_system;

import javafx.application.Application;
import javafx.stage.Stage;

public class InventoryApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setPrimaryStage(stage);
        SceneManager.switchScene("login-view.fxml", "Inventory System — Login", 450, 350);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
