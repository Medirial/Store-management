package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Properties;
import model.MySQLConnector;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.text.SimpleDateFormat;

public class DashboardPanel extends JPanel {
    private final JPanel productsCard, salesCard, revenueCard, expensesCard;
    private final JLabel salesChangeLabel;
    private final Connection connection;
    private final JDatePickerImpl startDatePicker, endDatePicker;

    public DashboardPanel() throws SQLException {
        connection = MySQLConnector.getConnection();

        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(new Color(240, 240, 240));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel with date pickers
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startDatePicker = createDatePicker();
        endDatePicker = createDatePicker();

        // Add property change listeners to update the dashboard data on date change
        startDatePicker.getModel().addChangeListener(e -> updateDashboardData());
        endDatePicker.getModel().addChangeListener(e -> updateDashboardData());

        datePanel.add(new JLabel("Start Date:"));
        datePanel.add(startDatePicker);
        datePanel.add(new JLabel("End Date:"));
        datePanel.add(endDatePicker);

        // Main card panel
        JPanel cardPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        productsCard = createDashboardCard("Products", "0", new Color(41, 128, 185));
        salesCard = createDashboardCard("Sales", "$0", new Color(39, 174, 96));
        revenueCard = createDashboardCard("Net Revenue", "$0", new Color(231, 76, 60));
        expensesCard = createDashboardCard("Expenses", "$0", new Color(142, 68, 173));

        salesChangeLabel = createPercentageChangeLabel();
        revenueCard.add(salesChangeLabel, BorderLayout.SOUTH);

        cardPanel.add(productsCard);
        cardPanel.add(salesCard);
        cardPanel.add(revenueCard);
        cardPanel.add(expensesCard);

        this.add(datePanel, BorderLayout.NORTH);
        this.add(cardPanel, BorderLayout.CENTER);

        // Initial data load
        updateDashboardData();
    }

    private JPanel createDashboardCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(150, 150));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Poppins", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createPercentageChangeLabel() {
        JLabel changeLabel = new JLabel("0% from last month");
        changeLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        changeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        changeLabel.setForeground(Color.WHITE);

        return changeLabel;
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateComponentFormatter());
    }

    private void updateDashboardData() {
        String startDate = formatDate(startDatePicker);
        String endDate = formatDate(endDatePicker);

        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this, "Please select both start and end dates.");
            return;
        }

        updateSalesData(startDate, endDate);
        updateProductCount();
        updateExpensesData(startDate, endDate);
        updateRevenueData(startDate, endDate);
    }

    private String formatDate(JDatePickerImpl datePicker) {
        if (datePicker.getModel().getValue() == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(datePicker.getModel().getValue());
    }

    private void updateSalesData(String startDate, String endDate) {
        String salesQuery = "SELECT SUM(total_cost) AS total_sales FROM sale WHERE date BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(salesQuery)) {
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ((JLabel) salesCard.getComponent(1)).setText("$" + rs.getDouble("total_sales"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProductCount() {
        String productCountQuery = "SELECT COUNT(*) AS product_count FROM product";
        try (PreparedStatement stmt = connection.prepareStatement(productCountQuery)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ((JLabel) productsCard.getComponent(1)).setText(String.valueOf(rs.getInt("product_count")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

private void updateExpensesData(String startDate, String endDate) { 
    String expensesQuery = """
        SELECT 
            SUM(
                CASE 
                    WHEN type = 'Daily' THEN amount * (DATEDIFF(?, ?) + 1)
                    WHEN type = 'Monthly' THEN amount * (FLOOR(DATEDIFF(?, ?) / 30))
                                           WHEN type = 'One-time' AND date BETWEEN ? AND ? THEN amount
                    ELSE 0
                END
            ) AS total_expenses
        FROM charges 
        WHERE (date BETWEEN ? AND ? OR type = 'Daily' OR type = 'Monthly' OR type = 'One-time')
    """;

    try (PreparedStatement stmt = connection.prepareStatement(expensesQuery)) {
        // Bind date parameters for 'Daily' calculation
        stmt.setString(1, endDate);   // End date for daily calculation
        stmt.setString(2, startDate); // Start date for daily calculation
        
        // Bind date parameters for 'Monthly' calculation
        stmt.setString(3, endDate);   // End date for monthly calculation
        stmt.setString(4, startDate); // Start date for monthly calculation
        
        // Bind date parameters for 'One-time' check
        stmt.setString(5, startDate); // Start date for one-time check
        stmt.setString(6, endDate);   // End date for one-time check
        
        // Bind date parameters for WHERE clause
        stmt.setString(7, startDate); // Start date for WHERE clause
        stmt.setString(8, endDate);   // End date for WHERE clause

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            ((JLabel) expensesCard.getComponent(1)).setText("$" + rs.getDouble("total_expenses"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void updateRevenueData(String startDate, String endDate) {
        double expenses = Double.parseDouble(((JLabel) expensesCard.getComponent(1)).getText().replace("$", ""));
        String revenueQuery = "SELECT (SUM(benefit) - ?) AS net_revenue FROM saleproduct WHERE date BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(revenueQuery)) {
            stmt.setDouble(1, expenses);
            stmt.setString(2, startDate);
            stmt.setString(3, endDate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ((JLabel) revenueCard.getComponent(1)).setText("$" + rs.getDouble("net_revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
