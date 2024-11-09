package model;

public class Product {
    private int id; // Internal use only, not displayed in the table
    private String name;
    private String category;
    private String priceSell;
    private String priceBuy;

    // Constructor without ID for new products (ID is set by the database)
    public Product(String name, String category, String priceSell, String priceBuy) {
        this.name = name;
        this.category = category;
        this.priceSell = priceSell;
        this.priceBuy = priceBuy;
    }

    // Constructor with ID for existing products (for update/delete operations)
    public Product(int id, String name, String category, String priceSell, String priceBuy) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.priceSell = priceSell;
        this.priceBuy = priceBuy;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriceSell() {
        return priceSell;
    }

    public void setPriceSell(String priceSell) {
        this.priceSell = priceSell;
    }

    public String getPriceBuy() {
        return priceBuy;
    }

    public void setPriceBuy(String priceBuy) {
        this.priceBuy = priceBuy;
    }
}
