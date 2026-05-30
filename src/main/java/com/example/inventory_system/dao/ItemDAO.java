package com.example.inventory_system.dao;

import com.example.inventory_system.DatabaseConnection;
import com.example.inventory_system.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public List<Item> findAll() throws SQLException {
        String sql = "SELECT i.id, i.sku, i.name, i.category_id, "
                + "COALESCE(c.name, '') AS category_name, "
                + "i.supplier_id, COALESCE(s.name, '') AS supplier_name, "
                + "i.quantity, i.unit_price, i.created_at "
                + "FROM items i "
                + "LEFT JOIN categories c ON i.category_id = c.id "
                + "LEFT JOIN suppliers s ON i.supplier_id = s.id "
                + "ORDER BY i.id DESC";
        List<Item> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                items.add(mapRow(rs));
            }
        }
        return items;
    }

    public List<Item> search(String keyword) throws SQLException {
        String sql = "SELECT i.id, i.sku, i.name, i.category_id, "
                + "COALESCE(c.name, '') AS category_name, "
                + "i.supplier_id, COALESCE(s.name, '') AS supplier_name, "
                + "i.quantity, i.unit_price, i.created_at "
                + "FROM items i "
                + "LEFT JOIN categories c ON i.category_id = c.id "
                + "LEFT JOIN suppliers s ON i.supplier_id = s.id "
                + "WHERE LOWER(i.name) LIKE ? OR LOWER(i.sku) LIKE ? "
                + "ORDER BY i.id DESC";
        List<Item> items = new ArrayList<>();
        String pattern = "%" + keyword.toLowerCase() + "%";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRow(rs));
                }
            }
        }
        return items;
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM items";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    public int countLowStock(int threshold) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM items WHERE quantity <= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return 0;
            }
        }
    }

    public void insert(Item item) throws SQLException {
        String sql = "INSERT INTO items (sku, name, category_id, supplier_id, quantity, unit_price) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getSku());
            stmt.setString(2, item.getName());
            stmt.setInt(3, item.getCategoryId());
            stmt.setInt(4, item.getSupplierId());
            stmt.setInt(5, item.getQuantity());
            stmt.setBigDecimal(6, item.getUnitPrice());
            stmt.executeUpdate();
        }
    }

    public void update(Item item) throws SQLException {
        String sql = "UPDATE items SET sku = ?, name = ?, category_id = ?, supplier_id = ?, quantity = ?, unit_price = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getSku());
            stmt.setString(2, item.getName());
            stmt.setInt(3, item.getCategoryId());
            stmt.setInt(4, item.getSupplierId());
            stmt.setInt(5, item.getQuantity());
            stmt.setBigDecimal(6, item.getUnitPrice());
            stmt.setInt(7, item.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Item mapRow(ResultSet rs) throws SQLException {
        return new Item(
                rs.getInt("id"),
                rs.getString("sku"),
                rs.getString("name"),
                rs.getInt("category_id"),
                rs.getString("category_name"),
                rs.getInt("supplier_id"),
                rs.getString("supplier_name"),
                rs.getInt("quantity"),
                rs.getBigDecimal("unit_price"),
                rs.getTimestamp("created_at")
        );
    }
}
