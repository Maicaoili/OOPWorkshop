package com.example.inventory_system.model;

import java.sql.Timestamp;

public class StockTransaction {
    private int id;
    private int itemId;
    private String itemName;
    private String transactionType;
    private int quantity;
    private String notes;
    private Timestamp createdAt;

    public StockTransaction() {
    }

    public StockTransaction(int id, int itemId, String itemName, String transactionType,
                            int quantity, String notes, Timestamp createdAt) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
