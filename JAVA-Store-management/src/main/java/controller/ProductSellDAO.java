/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.MySQLConnector;
import model.ProductSell;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductSellDAO {
    private final Connection connection;
    private String query;
    private PreparedStatement statement;

    public ProductSellDAO() throws SQLException {
        connection = MySQLConnector.getConnection();
    }

    public boolean createProductSell(ProductSell productSell) throws SQLException {
        query = "INSERT INTO saleproduct (foreignID_product, foreignID_sale, date, price_at_sale, benefit) VALUES (?, ?, ?, ?, ?)";
        statement = connection.prepareStatement(query);
        statement.setInt(1, productSell.getForeignIDProduct());
        statement.setInt(2, productSell.getForeignIDSale());
        statement.setString(3, productSell.getDate()); // Converting Date to Timestamp
        statement.setString(4, productSell.getPriceAtSale());
        statement.setString(5, productSell.getBenefit());

        return statement.executeUpdate() != 0;
    }

    public List<ProductSell> readProductSells() throws SQLException {
        List<ProductSell> productSells = new ArrayList<>();
        query = "SELECT id, foreignID_product, foreignID_sale, date, price_at_sale, benefit FROM saleproduct";
        statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            ProductSell productSell = new ProductSell(
                result.getInt("id"),
                result.getInt("foreignID_product"),
                result.getInt("foreignID_sale"),
                result.getString("date"),
                result.getString("price_at_sale"),
                result.getString("benefit")
            );
            productSells.add(productSell);
        }
        return productSells;
    }

    public boolean updateProductSell(ProductSell productSell) throws SQLException {
        query = "UPDATE saleproduct SET foreignID_product = ?, foreignID_sale = ?, date = ?, price_at_sale = ?, benefit = ? WHERE id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, productSell.getForeignIDProduct());
        statement.setInt(2, productSell.getForeignIDSale());
        statement.setString(3, productSell.getDate());
        statement.setString(4, productSell.getPriceAtSale());
        statement.setString(5, productSell.getBenefit());
        statement.setInt(6, productSell.getId());

        return statement.executeUpdate() != 0;
    }

    public boolean deleteProductSell(int id) throws SQLException {
        query = "DELETE FROM saleproduct WHERE id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        return statement.executeUpdate() != 0;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
