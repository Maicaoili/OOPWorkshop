module com.example.inventory_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    opens com.example.inventory_system.controller to javafx.fxml;
    opens com.example.inventory_system.model to javafx.base;
    exports com.example.inventory_system;
}
