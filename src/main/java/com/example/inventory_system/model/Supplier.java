package com.example.inventory_system.model;

import java.sql.Timestamp;

public class Supplier {
    private int id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private Timestamp createdAt;

    public Supplier() {
    }

    public Supplier(int id, String name, String contactPerson, String email, String phone, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return name;
    }
}
