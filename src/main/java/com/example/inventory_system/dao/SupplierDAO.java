package com.example.inventory_system.dao;

import com.example.inventory_system.DatabaseConnection;
import com.example.inventory_system.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    public List<Supplier> findAll() throws SQLException {
        String sql = "SELECT id, name, contact_person, email, phone, created_at FROM suppliers ORDER BY name";
        List<Supplier> suppliers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact_person"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getTimestamp("created_at")
                ));
            }
        }
        return suppliers;
    }

    public void insert(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO suppliers (name, contact_person, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContactPerson());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getPhone());
            stmt.executeUpdate();
        }
    }

    public void update(Supplier supplier) throws SQLException {
        String sql = "UPDATE suppliers SET name = ?, contact_person = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContactPerson());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getPhone());
            stmt.setInt(5, supplier.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM suppliers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
