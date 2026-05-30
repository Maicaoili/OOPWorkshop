package com.example.inventory_system.controller;

import com.example.inventory_system.SceneManager;
import com.example.inventory_system.dao.CategoryDAO;
import com.example.inventory_system.dao.ItemDAO;
import com.example.inventory_system.dao.SupplierDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label totalItemsLabel;
    @FXML private Label lowStockLabel;
    @FXML private Label totalSuppliersLabel;
    @FXML private Label totalCategoriesLabel;

    private final ItemDAO itemDAO = new ItemDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();

    @FXML
    public void initialize() {
        loadSummaryData();
    }

    private void loadSummaryData() {
        try {
            totalItemsLabel.setText(String.valueOf(itemDAO.countAll()));
            lowStockLabel.setText(String.valueOf(itemDAO.countLowStock(10)));
            totalSuppliersLabel.setText(String.valueOf(supplierDAO.findAll().size()));
            totalCategoriesLabel.setText(String.valueOf(categoryDAO.findAll().size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowItems(ActionEvent event) {
        try {
            SceneManager.switchScene("items-view.fxml", "Manage Items", 1000, 650);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowCategories(ActionEvent event) {
        try {
            SceneManager.switchScene("categories-view.fxml", "Manage Categories", 800, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowSuppliers(ActionEvent event) {
        try {
            SceneManager.switchScene("suppliers-view.fxml", "Manage Suppliers", 900, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowTransactions(ActionEvent event) {
        try {
            SceneManager.switchScene("transactions-view.fxml", "Stock Transactions", 900, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            SceneManager.switchScene("login-view.fxml", "Inventory System — Login", 450, 350);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
