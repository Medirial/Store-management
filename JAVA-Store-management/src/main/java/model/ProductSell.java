/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;



public class ProductSell {
    private int id; // Internal use only, not displayed in the table
    private int foreignIDProduct; // Reference to the Product ID
    private int foreignIDSale; // Reference to the Sale ID
    private String date; // Date of the sale
    private String priceAtSale; // Price at the time of sale
    private String benefit; // Benefit from the sale

    // Constructor without ID for new product sales (ID is set by the database)
    public ProductSell(int foreignIDProduct, int foreignIDSale, String date, String priceAtSale, String benefit) {
        this.foreignIDProduct = foreignIDProduct;
        this.foreignIDSale = foreignIDSale;
        this.date = date;
        this.priceAtSale = priceAtSale;
        this.benefit = benefit;
    }

    // Constructor with ID for existing product sales (for update/delete operations)
    public ProductSell(int id, int foreignIDProduct, int foreignIDSale, String date, String priceAtSale, String benefit) {
        this.id = id;
        this.foreignIDProduct = foreignIDProduct;
        this.foreignIDSale = foreignIDSale;
        this.date = date;
        this.priceAtSale = priceAtSale;
        this.benefit = benefit;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getForeignIDProduct() {
        return foreignIDProduct;
    }

    public void setForeignIDProduct(int foreignIDProduct) {
        this.foreignIDProduct = foreignIDProduct;
    }

    public int getForeignIDSale() {
        return foreignIDSale;
    }

    public void setForeignIDSale(int foreignIDSale) {
        this.foreignIDSale = foreignIDSale;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPriceAtSale() {
        return priceAtSale;
    }

    public void setPriceAtSale(String priceAtSale) {
        this.priceAtSale = priceAtSale;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }
}
