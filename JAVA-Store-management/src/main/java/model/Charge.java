/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

public class Charge {
    private int id;
    private String description; // Description of the charge
    private String date; // Date of the charge
    private String type; // Type of charge (e.g., One-time, Monthly)
    private double amount; // Amount of the charge

    // Default constructor
    public Charge() {
    }

    // Parameterized constructor
    public Charge(int id, String desc, String date, String type, double amount) {
        this.id = id;
        this.description = desc;
        this.date = date;
        this.type = type;
        this.amount = amount;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Charge{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}
