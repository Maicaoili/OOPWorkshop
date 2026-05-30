package com.example.inventory_system.controller;

import com.example.inventory_system.SceneManager;
import com.example.inventory_system.dao.SupplierDAO;
import com.example.inventory_system.model.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.sql.Timestamp;

public class SuppliersController {
    @FXML private TableView<Supplier> suppliersTable;
    @FXML private TableColumn<Supplier, Integer> idColumn;
    @FXML private TableColumn<Supplier, String> nameColumn;
    @FXML private TableColumn<Supplier, String> contactColumn;
    @FXML private TableColumn<Supplier, String> emailColumn;
    @FXML private TableColumn<Supplier, String> phoneColumn;
    @FXML private TableColumn<Supplier, Timestamp> createdColumn;

    @FXML private VBox formPanel;
    @FXML private Label formTitle;
    @FXML private TextField formName;
    @FXML private TextField formContact;
    @FXML private TextField formEmail;
    @FXML private TextField formPhone;

    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();
    private Supplier currentSupplier = null;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        suppliersTable.setItems(suppliersList);
        loadData();
    }

    private void loadData() {
        try {
            suppliersList.setAll(supplierDAO.findAll());
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
        currentSupplier = null;
        formTitle.setText("Add Supplier");
        clearForm();
        formPanel.setManaged(true);
        formPanel.setVisible(true);
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        Supplier selected = suppliersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a supplier to edit.");
            return;
        }
        currentSupplier = selected;
        formTitle.setText("Edit Supplier");
        formName.setText(selected.getName());
        formContact.setText(selected.getContactPerson());
        formEmail.setText(selected.getEmail());
        formPhone.setText(selected.getPhone());

        formPanel.setManaged(true);
        formPanel.setVisible(true);
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Supplier selected = suppliersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a supplier to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete supplier " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                supplierDAO.delete(selected.getId());
                loadData();
            } catch (SQLException e) {
                showAlert("Delete Error", "Could not delete. Make sure no items depend on this supplier. Error: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            String name = formName.getText().trim();
            String contact = formContact.getText().trim();
            String email = formEmail.getText().trim();
            String phone = formPhone.getText().trim();

            if (name.isEmpty()) {
                showAlert("Validation Error", "Company name is required.");
                return;
            }

            Supplier supplier = new Supplier();
            supplier.setName(name);
            supplier.setContactPerson(contact);
            supplier.setEmail(email);
            supplier.setPhone(phone);

            if (currentSupplier == null) {
                supplierDAO.insert(supplier);
            } else {
                supplier.setId(currentSupplier.getId());
                supplierDAO.update(supplier);
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
        formContact.clear();
        formEmail.clear();
        formPhone.clear();
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
