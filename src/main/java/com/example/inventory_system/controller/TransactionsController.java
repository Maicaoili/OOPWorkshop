package com.example.inventory_system.controller;

import com.example.inventory_system.SceneManager;
import com.example.inventory_system.dao.ItemDAO;
import com.example.inventory_system.dao.TransactionDAO;
import com.example.inventory_system.model.Item;
import com.example.inventory_system.model.StockTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionsController {
    @FXML private ComboBox<String> typeFilter;
    @FXML private TableView<StockTransaction> transactionsTable;
    @FXML private TableColumn<StockTransaction, Integer> idColumn;
    @FXML private TableColumn<StockTransaction, String> itemColumn;
    @FXML private TableColumn<StockTransaction, String> typeColumn;
    @FXML private TableColumn<StockTransaction, Integer> qtyColumn;
    @FXML private TableColumn<StockTransaction, String> notesColumn;
    @FXML private TableColumn<StockTransaction, Timestamp> dateColumn;

    @FXML private VBox formPanel;
    @FXML private ComboBox<Item> formItem;
    @FXML private ComboBox<String> formType;
    @FXML private TextField formQuantity;
    @FXML private TextArea formNotes;

    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final ItemDAO itemDAO = new ItemDAO();
    private final ObservableList<StockTransaction> transactionsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        transactionsTable.setItems(transactionsList);

        typeFilter.setItems(FXCollections.observableArrayList("ALL", "IN", "OUT"));
        typeFilter.setValue("ALL");
        typeFilter.setOnAction(e -> loadData());

        formType.setItems(FXCollections.observableArrayList("IN", "OUT"));

        loadData();
    }

    private void loadData() {
        try {
            String filter = typeFilter.getValue();
            if ("ALL".equals(filter)) {
                transactionsList.setAll(transactionDAO.findAll());
            } else {
                transactionsList.setAll(transactionDAO.findByType(filter));
            }
            formItem.setItems(FXCollections.observableArrayList(itemDAO.findAll()));
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
        clearForm();
        formPanel.setManaged(true);
        formPanel.setVisible(true);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            Item item = formItem.getValue();
            String type = formType.getValue();
            String qtyText = formQuantity.getText().trim();
            String notes = formNotes.getText().trim();

            if (item == null || type == null || qtyText.isEmpty()) {
                showAlert("Validation Error", "Item, Type, and Quantity are required.");
                return;
            }

            int quantity = Integer.parseInt(qtyText);
            if (quantity <= 0) {
                showAlert("Validation Error", "Quantity must be greater than 0.");
                return;
            }
            
            if ("OUT".equals(type) && item.getQuantity() < quantity) {
                showAlert("Validation Error", "Not enough stock. Current stock: " + item.getQuantity());
                return;
            }

            StockTransaction transaction = new StockTransaction();
            transaction.setItemId(item.getId());
            transaction.setTransactionType(type);
            transaction.setQuantity(quantity);
            transaction.setNotes(notes);

            transactionDAO.insert(transaction);

            handleCancel(null);
            loadData();
            showAlertInfo("Success", "Transaction recorded successfully.");
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Quantity must be a valid integer.");
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
        formItem.setValue(null);
        formType.setValue(null);
        formQuantity.clear();
        formNotes.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    
    private void showAlertInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
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
