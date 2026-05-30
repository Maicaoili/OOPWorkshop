package com.example.inventory_system.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Item {
    private int id;
    private String sku;
    private String name;
    private int categoryId;
    private String categoryName;
    private int supplierId;
    private String supplierName;
    private int quantity;
    private BigDecimal unitPrice;
    private Timestamp createdAt;

    public Item() {
    }

    public Item(int id, String sku, String name, int categoryId, String categoryName,
                int supplierId, String supplierName, int quantity, BigDecimal unitPrice, Timestamp createdAt) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
