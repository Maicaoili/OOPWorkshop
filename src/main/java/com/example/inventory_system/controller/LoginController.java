package com.example.inventory_system.controller;

import com.example.inventory_system.SceneManager;
import com.example.inventory_system.dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        try {
            if (userDAO.authenticate(username, password)) {
                errorLabel.setText("");
                SceneManager.switchScene("dashboard-view.fxml", "Inventory Dashboard", 900, 600);
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        } catch (Exception e) {
            errorLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
