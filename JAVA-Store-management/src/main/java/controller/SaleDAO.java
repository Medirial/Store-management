package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.MySQLConnector;
import model.Sale;

public class SaleDAO {
    private final Connection connection;
    private String query;
    private PreparedStatement statement;
    private ResultSet result;
    private int insertedLines = 0;

    public SaleDAO() throws SQLException {
        connection = MySQLConnector.getConnection();
    }

  public int createSale(Sale sale) throws SQLException {
      
      
        query = "INSERT INTO Sale VALUES(?, ?, ?, ?)";
        statement = connection.prepareStatement(query);
        statement.setString(1, null);
        statement.setFloat(2, sale.getTotalCost());
        statement.setString(3, sale.getSellerUsername());
        statement.setString(4, sale.getDate());
        insertedLines = statement.executeUpdate();
        
        
    // Prepare the SQL query with RETURN_GENERATED_KEYS to get the new ID
        query = "INSERT INTO Sale VALUES(?, ?, ?, ?)";
    statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    
    statement.setString(1, null);
        statement.setFloat(2, sale.getTotalCost());
        statement.setString(3, sale.getSellerUsername());
        statement.setString(4, sale.getDate());
        
    // Execute the insert operation
    int insertedLines = statement.executeUpdate();

    // Check if the insertion was successful
    if (insertedLines != 0) {
        // Retrieve the generated keys
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            // Return the new sale ID
            return generatedKeys.getInt(1);
        }
    }
    
    // Return -1 if the sale could not be created
    return -1;
}


    public boolean updateSale(Sale sale) throws SQLException {
        query = "UPDATE Sale SET total_cost = ?, seller_username = ?, date = ? WHERE id = ?";
        statement = connection.prepareStatement(query);
        statement.setFloat(1, sale.getTotalCost());
        statement.setString(2, sale.getSellerUsername());
        statement.setString(3, sale.getDate());
        statement.setInt(4, sale.getId());
        insertedLines = statement.executeUpdate();
        return(insertedLines != 0);
    }

    public boolean deleteSale(int id) throws SQLException {
        query = "DELETE FROM Sale WHERE id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        insertedLines = statement.executeUpdate();
        return(insertedLines != 0);
    }

    public String[][] readSalesTableData(String startDate, String endDate) throws SQLException {
        int rows = 0, columns = 4, aux = 0;
        String dateFilter = String.format(" WHERE date BETWEEN '%s' AND '%s'", startDate, endDate);

        ResultSet resultRows = connection.prepareStatement("SELECT COUNT(*) FROM Sale" + dateFilter).executeQuery();
        if(resultRows.next()) rows = resultRows.getInt(1);

        query = "SELECT * FROM Sale" + dateFilter;
        statement = connection.prepareStatement(query);
        result = statement.executeQuery();
        List<String> salesList = new ArrayList<>();
        while(result.next()){
            for(int i = 1; i <= columns; i++){
                salesList.add(result.getString(i));
            }
        }

        String[][] sales = new String[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                sales[i][j] = salesList.get(aux++);
            }
        }
        return sales;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
