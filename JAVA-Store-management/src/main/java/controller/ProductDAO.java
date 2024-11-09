package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.MySQLConnector;
import model.Product;

public class ProductDAO {
    private final Connection connection;
    private String query;
    private PreparedStatement statement;
    private int insertedLines = 0;

    public ProductDAO() throws SQLException {
        connection = MySQLConnector.getConnection();
    }

   
public String[][] readProductsTableData() throws SQLException {
    // Number of rows and columns to display
    int rows = 0;
    int columns = 5; // We will display 4 columns: name, category, pricesell, priceby

    // Get the total count of products
    ResultSet resultRows = connection.prepareStatement("SELECT COUNT(*) FROM Product").executeQuery();
    if (resultRows.next()) {
        rows = resultRows.getInt(1);
    }

    // Select specific columns, including ProductID
    String query = "SELECT id, name, category, price_sell, price_buy FROM Product"; 
    PreparedStatement statement = connection.prepareStatement(query);
    ResultSet result = statement.executeQuery();

    // Lists to hold product IDs and details
    List<String[]> productsList = new ArrayList<>();

    // Store product information in a list
    while (result.next()) {
        // Store product details in an array
        String[] productDetails = new String[columns];
 
                productDetails[0] = result.getString("id");      // Name

        productDetails[1] = result.getString("Name");      // Name
        productDetails[2] = result.getString("Category");   // Category
        productDetails[3] = result.getString("price_sell");  // PriceSell
        productDetails[4] = result.getString("price_buy");    // PriceBy
        
        productsList.add(productDetails); // Add details to the list
    }

    // Convert the list to a 2D array
    String[][] products = new String[productsList.size()][columns];
    for (int i = 0; i < productsList.size(); i++) {
        products[i] = productsList.get(i);
    }

    // You can use productIds for any further operations
    // For example: processProductIds(productIds);

    return products;
}



   public boolean createProduct(Product product) throws SQLException {
    query = "INSERT INTO Product (name, category, price_buy, price_sell) VALUES(?, ?, ?, ?)";
    statement = connection.prepareStatement(query);
    // Set product parameters, assuming the ID is auto-incremented.
    statement.setString(1, product.getName());
    statement.setString(2, product.getCategory());
    statement.setString(3, product.getPriceBuy());
    statement.setString(4, product.getPriceSell());

    insertedLines = statement.executeUpdate();
    return (insertedLines != 0);
}

public boolean updateProduct(Product product) throws SQLException {
    query = "UPDATE Product SET name = ?, category = ?, price_buy = ?, price_sell = ? WHERE id = ?";
    statement = connection.prepareStatement(query);
    statement.setString(1, product.getName());
    statement.setString(2, product.getCategory());
    statement.setString(3, product.getPriceBuy());
    statement.setString(4, product.getPriceSell());
    statement.setInt(5, product.getId());

    insertedLines = statement.executeUpdate();
    return (insertedLines != 0);
}


    public boolean deleteProduct(int id) throws SQLException {
        query = "DELETE FROM Product WHERE id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        insertedLines = statement.executeUpdate();
        return(insertedLines != 0);
    }

    public void close() throws SQLException {
        connection.close();
    }
}
