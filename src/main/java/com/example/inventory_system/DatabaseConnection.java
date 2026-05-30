package com.example.inventory_system;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;
    private static final Dotenv dotenv = Dotenv.load();

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:postgresql://"
                    + dotenv.get("DB_HOST") + ":"
                    + dotenv.get("DB_PORT") + "/"
                    + dotenv.get("DB_NAME") + "?sslmode=require";
            connection = DriverManager.getConnection(
                    url,
                    dotenv.get("DB_USER"),
                    dotenv.get("DB_PASSWORD")
            );
        }
        return connection;
    }
}
