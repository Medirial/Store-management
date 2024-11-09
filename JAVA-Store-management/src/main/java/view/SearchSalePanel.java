package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdatepicker.impl.DateComponentFormatter; // Use DateComponentFormatter
import controller.SaleDAO;

public class SearchSalePanel extends JPanel implements ActionListener {
    private final JPanel topPanel, bottomPanel;
    private final JLabel startDateLabel, endDateLabel;
    private final JDatePickerImpl startDatePicker, endDatePicker;
    private final JButton backButton, generateCSVButton;
    private final Dimension inputBoxDimension = new Dimension(100, 20),
            tableDimension = new Dimension(800, 500), buttonsDimension = new Dimension(300, 35);
    private final Color mainColor = Color.white, inputColor = Color.black;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JScrollPane scrollPane;
    private Object[][] tableData;
    private final String[] tableColumns = {"ID", "Total cost", "Seller", "Date"};
    private final SaleDAO saleDAO;

    public SearchSalePanel() throws SQLException {
        saleDAO = new SaleDAO();
        setLayout(new BorderLayout());

          Color backgroundColor = new Color(240, 240, 240);
        Color panelColor = new Color(255, 255, 255);
        Color buttonColor = new Color(52, 152, 219);
        Color textColor = new Color(44, 62, 80);
        // Create top panel with GridBagLayout
        topPanel = new JPanel(new GridBagLayout());
        topPanel.setPreferredSize(new Dimension(0, 120));
        topPanel.setBackground(mainColor);
        add(topPanel, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for components

        startDateLabel = new JLabel("Start date");
        startDateLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        gbc.gridx = 0; // Column for start date label
        gbc.gridy = 0; // Row for start date label
        topPanel.add(startDateLabel, gbc);

        // Setup the date picker for start date
        UtilDateModel startDateModel = new UtilDateModel();
        Properties startDateProperties = new Properties();
        JDatePanelImpl startDatePanel = new JDatePanelImpl(startDateModel, startDateProperties);
        DateComponentFormatter startDateFormatter = new DateComponentFormatter();
        startDatePicker = new JDatePickerImpl(startDatePanel, startDateFormatter);
        gbc.gridx = 1; // Column for start date picker
        topPanel.add(startDatePicker, gbc);

        endDateLabel = new JLabel("End date");
        endDateLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        gbc.gridx = 2; // Column for end date label
        gbc.gridy = 0; // Row for end date label
        topPanel.add(endDateLabel, gbc);

        // Setup the date picker for end date
        UtilDateModel endDateModel = new UtilDateModel();
        Properties endDateProperties = new Properties();
        JDatePanelImpl endDatePanel = new JDatePanelImpl(endDateModel, endDateProperties);
        DateComponentFormatter endDateFormatter = new DateComponentFormatter();
        endDatePicker = new JDatePickerImpl(endDatePanel, endDateFormatter);
        gbc.gridx = 3; // Column for end date picker
        topPanel.add(endDatePicker, gbc);

        // Create a panel for buttons (only for the CSV button now)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        gbc.gridx = 0; // Reset column for button panel
        gbc.gridy = 1; // Next row for button panel
        gbc.gridwidth = 4; // Span all columns
        topPanel.add(buttonPanel, gbc);

        generateCSVButton = new JButton("Generate CSV");
        setButtonDesign(generateCSVButton);
        buttonPanel.add(generateCSVButton);

        // Set button panel size to match input panel
        buttonPanel.setPreferredSize(new Dimension(400, 30)); // Adjust as needed for uniform width

        tableModel = new DefaultTableModel(null, tableColumns);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
             table.setBackground(panelColor);
        table.setForeground(textColor);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setFont(new Font("poppinsFont", Font.BOLD, 12));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(32, 136, 203));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(tableDimension);
        add(scrollPane, BorderLayout.CENTER);

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        bottomPanel.setBackground(mainColor);
        add(bottomPanel, BorderLayout.SOUTH);

        backButton = new JButton("Back");
        setButtonDesign(backButton);
      

        // Add ChangeListeners to date pickers
        startDatePicker.addActionListener(e -> checkAndSearch());
        endDatePicker.addActionListener(e -> checkAndSearch());
    }

  
    private void setButtonDesign(JButton button) {
        button.setPreferredSize(buttonsDimension);
        button.setFocusable(false);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            button.setFont(button.getFont().deriveFont(12f)); // Set font size to 18

        button.addActionListener(this);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent event) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    private void updateTable() {
        int currentRowCount = tableModel.getRowCount();
        tableModel.setRowCount(0); // Clear existing rows
        tableModel.setRowCount(currentRowCount); // Reset row count
        tableModel.setDataVector(tableData, tableColumns); // Populate new data
    }

    private void checkAndSearch() {
        Date startDateDate = (Date) startDatePicker.getModel().getValue();
        Date endDateDate = (Date) endDatePicker.getModel().getValue();

        // Only search if both dates are filled
        if (startDateDate != null && endDateDate != null) {
            searchTableData();
        }
    }

    private void searchTableData() {
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date startDateDate = (Date) startDatePicker.getModel().getValue();
        Date endDateDate = (Date) endDatePicker.getModel().getValue();

        String startDateString = sqlFormat.format(startDateDate);
        String endDateString = sqlFormat.format(endDateDate);

        try {
            tableData = saleDAO.readSalesTableData(startDateString, endDateString);
            updateTable();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving data: " + sqlException.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateCSVFromTable() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            public String getDescription() {
                return "CSV Files";
            }

            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    String filename = file.getName().toLowerCase();
                    return filename.endsWith(".csv");
                }
            }
        });
        int fileChooserResponse = fileChooser.showSaveDialog(null);

        if (fileChooserResponse == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write("ID,Total cost,Seller,Date\n"); // Write header
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < tableColumns.length; j++) {
                        if (j > 0) fileWriter.append(","); // Add comma for CSV
                        fileWriter.append(String.valueOf(tableData[i][j])); // Write data
                    }
                    fileWriter.append("\n"); // New line for each row
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error writing CSV: " + ioException.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(generateCSVButton)) {
            generateCSVFromTable();
        } else if (event.getSource().equals(backButton)) {
            // Handle back action
        }
    }
}
