package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Charge;
import model.MySQLConnector;

public class ChargeDAO {
    private final Connection connection;
    private String query;
    private PreparedStatement statement;

    public ChargeDAO() throws SQLException {
        connection = MySQLConnector.getConnection();
    }

    // Add a new charge
    public boolean addCharge(Charge charge) throws SQLException {
        query = "INSERT INTO charges (description, date, type, amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, charge.getDesc());
            statement.setString(2, charge.getDate());
            statement.setString(3, charge.getType());
            statement.setDouble(4, charge.getAmount());
            return statement.executeUpdate() > 0;
        }
    }

    // Update an existing charge
    public boolean updateCharge(Charge charge) throws SQLException {
        query = "UPDATE charges SET description = ?, date = ?, type = ?, amount = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, charge.getDesc());
            statement.setString(2, charge.getDate());
            statement.setString(3, charge.getType());
            statement.setDouble(4, charge.getAmount());
            statement.setInt(5, charge.getId());
            return statement.executeUpdate() > 0;
        }
    }

    // Delete a charge by ID
    public boolean deleteCharge(int id) throws SQLException {
        query = "DELETE FROM charges WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    // Retrieve a charge by ID
    public Charge getChargeById(int id) throws SQLException {
        query = "SELECT * FROM charges WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Charge(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getString("date"),
                    rs.getString("type"),
                    rs.getDouble("amount")
                );
            }
        }
        return null;
    }

    // Retrieve all charges
 public String[][] readChargesTableData() throws SQLException {
        // Number of rows and columns to display
        int rows = 0;
        int columns = 5; // Adjust according to the actual number of fields you want to display

        // Get the total count of charges
        ResultSet resultRows = connection.prepareStatement("SELECT COUNT(*) FROM charges").executeQuery();
        if (resultRows.next()) {
            rows = resultRows.getInt(1);
        }

        // Select specific columns from the charges table
        String query = "SELECT id, description, date, type, amount FROM charges"; 
        statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();

        // List to hold charge details
        List<String[]> chargesList = new ArrayList<>();

        // Store charge information in a list
        while (result.next()) {
            // Create an array to hold charge details
            String[] chargeDetails = new String[columns];
            chargeDetails[0] = result.getString("id");          // Charge ID
            chargeDetails[1] = result.getString("description");        // descriptionription
            chargeDetails[2] = result.getDate("date").toString(); // Date (as string)
            chargeDetails[3] = result.getString("type");        // Type
            chargeDetails[4] = String.valueOf(result.getDouble("amount")); // Amount

            chargesList.add(chargeDetails); // Add details to the list
        }

        // Convert the list to a 2D array
        String[][] charges = new String[chargesList.size()][columns];
        for (int i = 0; i < chargesList.size(); i++) {
            charges[i] = chargesList.get(i);
        }

        return charges;
    }

    public void close() throws SQLException {
        connection.close();
    }
}

