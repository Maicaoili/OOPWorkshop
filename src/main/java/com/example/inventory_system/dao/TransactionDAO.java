package com.example.inventory_system.dao;

import com.example.inventory_system.DatabaseConnection;
import com.example.inventory_system.model.StockTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public List<StockTransaction> findAll() throws SQLException {
        String sql = "SELECT t.id, t.item_id, i.name AS item_name, t.transaction_type, "
                + "t.quantity, t.notes, t.created_at "
                + "FROM stock_transactions t "
                + "JOIN items i ON t.item_id = i.id "
                + "ORDER BY t.created_at DESC";
        return executeQuery(sql, null);
    }

    public List<StockTransaction> findByType(String type) throws SQLException {
        String sql = "SELECT t.id, t.item_id, i.name AS item_name, t.transaction_type, "
                + "t.quantity, t.notes, t.created_at "
                + "FROM stock_transactions t "
                + "JOIN items i ON t.item_id = i.id "
                + "WHERE t.transaction_type = ? "
                + "ORDER BY t.created_at DESC";
        return executeQuery(sql, type);
    }

    public void insert(StockTransaction transaction) throws SQLException {
        String sqlInsert = "INSERT INTO stock_transactions (item_id, transaction_type, quantity, notes) VALUES (?, ?, ?, ?)";
        String sqlUpdateQty;
        if ("IN".equals(transaction.getTransactionType())) {
            sqlUpdateQty = "UPDATE items SET quantity = quantity + ? WHERE id = ?";
        } else {
            sqlUpdateQty = "UPDATE items SET quantity = quantity - ? WHERE id = ?";
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                stmt.setInt(1, transaction.getItemId());
                stmt.setString(2, transaction.getTransactionType());
                stmt.setInt(3, transaction.getQuantity());
                stmt.setString(4, transaction.getNotes());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateQty)) {
                stmt.setInt(1, transaction.getQuantity());
                stmt.setInt(2, transaction.getItemId());
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    private List<StockTransaction> executeQuery(String sql, String typeFilter) throws SQLException {
        List<StockTransaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (typeFilter != null) {
                stmt.setString(1, typeFilter);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new StockTransaction(
                            rs.getInt("id"),
                            rs.getInt("item_id"),
                            rs.getString("item_name"),
                            rs.getString("transaction_type"),
                            rs.getInt("quantity"),
                            rs.getString("notes"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        }
        return transactions;
    }
}
