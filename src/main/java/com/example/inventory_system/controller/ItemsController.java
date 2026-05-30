package com.example.inventory_system.controller;

import com.example.inventory_system.SceneManager;
import com.example.inventory_system.dao.CategoryDAO;
import com.example.inventory_system.dao.ItemDAO;
import com.example.inventory_system.dao.SupplierDAO;
import com.example.inventory_system.model.Category;
import com.example.inventory_system.model.Item;
import com.example.inventory_system.model.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ItemsController {
    @FXML private TextField searchField;
    @FXML private TableView<Item> itemsTable;
    @FXML private TableColumn<Item, Integer> idColumn;
    @FXML private TableColumn<Item, String> skuColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> categoryColumn;
    @FXML private TableColumn<Item, String> supplierColumn;
    @FXML private TableColumn<Item, Integer> quantityColumn;
    @FXML private TableColumn<Item, BigDecimal> priceColumn;
    @FXML private TableColumn<Item, Timestamp> createdColumn;

    @FXML private VBox formPanel;
    @FXML private Label formTitle;
    @FXML private TextField formSku;
    @FXML private TextField formName;
    @FXML private ComboBox<Category> formCategory;
    @FXML private ComboBox<Supplier> formSupplier;
    @FXML private TextField formQuantity;
    @FXML private TextField formPrice;

    private final ItemDAO itemDAO = new ItemDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();

    private final ObservableList<Item> itemsList = FXCollections.observableArrayList();
    private Item currentItem = null;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        itemsTable.setItems(itemsList);
        loadData();
    }

    private void loadData() {
        try {
            itemsList.setAll(itemDAO.findAll());
            formCategory.setItems(FXCollections.observableArrayList(categoryDAO.findAllSimple()));
            formSupplier.setItems(FXCollections.observableArrayList(supplierDAO.findAll()));
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
        } else {
            try {
                itemsList.setAll(itemDAO.search(keyword));
            } catch (SQLException e) {
                showAlert("Search Error", e.getMessage());
            }
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        searchField.clear();
        loadData();
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        currentItem = null;
        formTitle.setText("Add New Item");
        clearForm();
        formPanel.setManaged(true);
        formPanel.setVisible(true);
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        Item selected = itemsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an item to edit.");
            return;
        }
        currentItem = selected;
        formTitle.setText("Edit Item");
        formSku.setText(selected.getSku());
        formName.setText(selected.getName());
        formQuantity.setText(String.valueOf(selected.getQuantity()));
        formPrice.setText(selected.getUnitPrice().toString());

        for (Category cat : formCategory.getItems()) {
            if (cat.getId() == selected.getCategoryId()) {
                formCategory.setValue(cat);
                break;
            }
        }
        for (Supplier sup : formSupplier.getItems()) {
            if (sup.getId() == selected.getSupplierId()) {
                formSupplier.setValue(sup);
                break;
            }
        }

        formPanel.setManaged(true);
        formPanel.setVisible(true);
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Item selected = itemsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an item to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete item " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                itemDAO.delete(selected.getId());
                loadData();
            } catch (SQLException e) {
                showAlert("Delete Error", e.getMessage());
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            String sku = formSku.getText().trim();
            String name = formName.getText().trim();
            Category category = formCategory.getValue();
            Supplier supplier = formSupplier.getValue();
            String qtyText = formQuantity.getText().trim();
            String priceText = formPrice.getText().trim();

            if (sku.isEmpty() || name.isEmpty() || category == null || supplier == null || qtyText.isEmpty() || priceText.isEmpty()) {
                showAlert("Validation Error", "All fields are required.");
                return;
            }

            int quantity = Integer.parseInt(qtyText);
            BigDecimal price = new BigDecimal(priceText);

            Item item = new Item();
            item.setSku(sku);
            item.setName(name);
            item.setCategoryId(category.getId());
            item.setSupplierId(supplier.getId());
            item.setQuantity(quantity);
            item.setUnitPrice(price);

            if (currentItem == null) {
                itemDAO.insert(item);
            } else {
                item.setId(currentItem.getId());
                itemDAO.update(item);
            }

            handleCancel(null);
            loadData();
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Quantity must be an integer and price must be a valid number.");
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
        formSku.clear();
        formName.clear();
        formCategory.setValue(null);
        formSupplier.setValue(null);
        formQuantity.clear();
        formPrice.clear();
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
