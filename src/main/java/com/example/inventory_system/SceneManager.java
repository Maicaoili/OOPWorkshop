package com.example.inventory_system;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;

    private SceneManager() {
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void switchScene(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                SceneManager.class.getResource("/com/example/inventory_system/" + fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.sizeToScene();
    }

    public static void switchScene(String fxmlFile, String title, double width, double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                SceneManager.class.getResource("/com/example/inventory_system/" + fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
    }
}
