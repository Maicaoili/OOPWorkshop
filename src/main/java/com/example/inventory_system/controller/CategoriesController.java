package com.example.inventory_system.controller;

import com.example.inventory_system.SceneManager;
import com.example.inventory_system.dao.CategoryDAO;
import com.example.inventory_system.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class CategoriesController {
    @FXML private TableView<Category> categoriesTable;
    @FXML private TableColumn<Category, Integer> idColumn;
    @FXML private TableColumn<Category, String> nameColumn;
    @FXML private TableColumn<Category, String> descColumn;
    @FXML private TableColumn<Category, Integer> countColumn;

    @FXML private VBox formPanel;
    @FXML private Label formTitle;
    @FXML private TextField formName;
    @FXML private TextArea formDesc;

    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ObservableList<Category> categoriesList = FXCollections.observableArrayList();
    private Category currentCategory = null;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));

        categoriesTable.setItems(categoriesList);
        loadData();
    }

    private void loadData() {
        try {
            categoriesList.setAll(categoryDAO.findAll());
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData();
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        currentCategory = null;
        formTitle.setText("Add Category");
        clearForm();
        formPanel.setManaged(true);
        formPanel.setVisible(true);
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        Category selected = categoriesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a category to edit.");
            return;
        }
        currentCategory = selected;
        formTitle.setText("Edit Category");
        formName.setText(selected.getName());
        formDesc.setText(selected.getDescription());

        formPanel.setManaged(true);
        formPanel.setVisible(true);
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Category selected = categoriesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a category to delete.");
            return;
        }
        if (selected.getItemCount() > 0) {
            showAlert("Cannot Delete", "Cannot delete category because it has items associated with it.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete category " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                categoryDAO.delete(selected.getId());
                loadData();
            } catch (SQLException e) {
                showAlert("Delete Error", e.getMessage());
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            String name = formName.getText().trim();
            String desc = formDesc.getText().trim();

            if (name.isEmpty()) {
                showAlert("Validation Error", "Name is required.");
                return;
            }

            Category category = new Category();
            category.setName(name);
            category.setDescription(desc);

            if (currentCategory == null) {
                categoryDAO.insert(category);
            } else {
                category.setId(currentCategory.getId());
                categoryDAO.update(category);
            }

            handleCancel(null);
            loadData();
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        clearForm();
        formPanel.setManaged(false);
        formPanel.setVisible(false);
    }

    private void clearForm() {
        formName.clear();
        formDesc.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void handleDashboardReturn() {
        try {
            SceneManager.switchScene("dashboard-view.fxml", "Inventory Dashboard", 900, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
