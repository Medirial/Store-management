package view;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.Product;
import controller.ProductDAO;




import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.Product;
import controller.ProductDAO;

public class ManageProductsPanel extends JPanel implements ActionListener {
    private final JPanel bottomPanel = null, inputsPanel, tablePanel;
    private final JLabel nameLabel, priceLabelbuy, priceLabelsell, categoryLabel, instructionLabel = null;
    private final JTextField nameTextField, priceFieldbuy, priceFieldsell;
    private final JComboBox<String> categoryComboBox;
    private final JButton createProductButton, updateProductButton, deleteProductButton;
    private final Dimension labelDimension = new Dimension(100, 35), inputBoxDimension = new Dimension(180, 20),
            buttonsDimension = new Dimension(105, 25);
    private final DefaultTableModel tableModel;
    private final JTable productDataTable;
    private final JScrollPane scrollPane;
    private Object[][] productData;
    private final String[] tableColumns = {"id", "Name", "Category", "Price-Sell", "Price-Buy"};
    private final ProductDAO productDAO;

    private int selectedProductId = -1;  // Track the selected Product ID without a text field

    public ManageProductsPanel() throws SQLException {
        productDAO = new ProductDAO();

        // Color configurations
        Color backgroundColor = new Color(240, 240, 240);
        Color panelColor = new Color(255, 255, 255);
        Color buttonColor = new Color(52, 152, 219);
        Color textColor = new Color(44, 62, 80);

        this.setLayout(new BorderLayout());

        // Table Panel
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(backgroundColor);

        productData = productDAO.readProductsTableData();
        tableModel = new DefaultTableModel(productData, tableColumns);

        productDataTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productDataTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && productDataTable.getSelectedRow() != -1) {
                    fillFieldsFromSelectedRow();
                }
            }
        });
        productDataTable.setBackground(panelColor);
        productDataTable.setForeground(textColor);
        productDataTable.setGridColor(Color.LIGHT_GRAY);
        productDataTable.getTableHeader().setFont(new Font("poppinsFont", Font.BOLD, 12));
        productDataTable.getTableHeader().setOpaque(false);
        productDataTable.getTableHeader().setBackground(new Color(32, 136, 203));
        productDataTable.getTableHeader().setForeground(Color.WHITE);
        productDataTable.setRowHeight(25);
        scrollPane = new JScrollPane(productDataTable);
        scrollPane.setPreferredSize(new Dimension(690, 300));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Inputs Panel
        inputsPanel = new JPanel(new GridBagLayout());
        inputsPanel.setBackground(panelColor);
        inputsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Layout for Input Components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Name Label and Text Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        nameLabel = createStyledLabel("Product Name", textColor);
        inputsPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameTextField = createStyledTextField();
        inputsPanel.add(nameTextField, gbc);

        // Price Buy Label and Text Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        priceLabelbuy = createStyledLabel("Price - Buy (USD)", textColor);
        inputsPanel.add(priceLabelbuy, gbc);

        gbc.gridx = 1;
        priceFieldbuy = createStyledTextField();
        inputsPanel.add(priceFieldbuy, gbc);

        // Price Sell Label and Text Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        priceLabelsell = createStyledLabel("Price - Sell (USD)", textColor);
        inputsPanel.add(priceLabelsell, gbc);

        gbc.gridx = 1;
        priceFieldsell = createStyledTextField();
        inputsPanel.add(priceFieldsell, gbc);

        // Category Label and ComboBox
        gbc.gridx = 0;
        gbc.gridy = 3;
        categoryLabel = createStyledLabel("Category", textColor);
        inputsPanel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        categoryComboBox = new JComboBox<>(new String[]{null, "Télephone","Manette", "Laptop","Accessoire auto", "Ecouteurs", "Montre", "Chargeur"});
        categoryComboBox.setPreferredSize(inputBoxDimension);
        categoryComboBox.setBackground(panelColor);
        categoryComboBox.setBorder(BorderFactory.createLineBorder(textColor, 1));
        inputsPanel.add(categoryComboBox, gbc);

        // Button Panel
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(panelColor);

        createProductButton = new JButton("Create");
        setButtonDesign(createProductButton);
        buttonPanel.add(createProductButton);

        updateProductButton = new JButton("Update");
        setButtonDesign(updateProductButton);
        buttonPanel.add(updateProductButton);

        deleteProductButton = new JButton("Delete");
        setButtonDesign(deleteProductButton);
        buttonPanel.add(deleteProductButton);

        inputsPanel.add(buttonPanel, gbc);

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, inputsPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);
        this.add(splitPane, BorderLayout.CENTER);
    }

    
  private void setButtonDesign(JButton button) 
 
 {
        button.setPreferredSize(buttonsDimension);
        button.setFocusable(false);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFont(new Font("Sans Serif", Font.BOLD, 12));
        button.addActionListener(this);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        
                });}
    private JLabel createStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Sans Serif", Font.BOLD, 14));
        label.setForeground(color);
        label.setPreferredSize(labelDimension);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(inputBoxDimension);
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.DARK_GRAY);
        textField.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        return textField;
    }

    private void fillFieldsFromSelectedRow() {
        int selectedRow = productDataTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedProductId = Integer.parseInt((String) tableModel.getValueAt(selectedRow, 0));
            nameTextField.setText((String) tableModel.getValueAt(selectedRow, 1));
            categoryComboBox.setSelectedItem((String) tableModel.getValueAt(selectedRow, 2));
            priceFieldbuy.setText((String) tableModel.getValueAt(selectedRow, 4));
            priceFieldsell.setText((String) tableModel.getValueAt(selectedRow, 3));
        }
    }

      private boolean isBoxesEmpty() {
        return (priceFieldbuy.getText().isBlank() || nameTextField.getText().isBlank()
                || priceFieldsell.getText().isBlank() || categoryComboBox.getSelectedItem() == null);
    }

    private void emptyBoxes() {
       
        nameTextField.setText(null);
        priceFieldbuy.setText(null);
                priceFieldsell.setText(null);

        categoryComboBox.setSelectedIndex(0);
    }
     private void updateTable() {
        int currentRowCount = tableModel.getRowCount();
        tableModel.setRowCount(0);
        tableModel.setRowCount(currentRowCount);
        tableModel.setDataVector(productData, tableColumns);
    }
     private void dbCreateProduct(Product product) {
        if (isBoxesEmpty()) {
            JOptionPane.showMessageDialog(null, "You must fill all text fields!",
                    "Input error", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                if (productDAO.createProduct(product)){
                    productData = productDAO.readProductsTableData();
                    JOptionPane.showMessageDialog(null, "This product has been created successfully!",
                            "Product created", JOptionPane.INFORMATION_MESSAGE);
                    emptyBoxes();
                    updateTable();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                JOptionPane.showMessageDialog(null, "Someting went wrong!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void dbUpdateProduct(Product product) {
        if (isBoxesEmpty()) {
            JOptionPane.showMessageDialog(null, "You must fill all text fields!",
                    "Input error", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                
                if (productDAO.updateProduct(product)){
                    productData = productDAO.readProductsTableData();
                    JOptionPane.showMessageDialog(null, "This product has been updated successfully!",
                            "Product updated", JOptionPane.INFORMATION_MESSAGE);
                    emptyBoxes();
                    updateTable();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                JOptionPane.showMessageDialog(null, "Someting went wrong!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

 private void dbDeleteProduct() {
    if (productDataTable.getSelectionModel().isSelectionEmpty()) {
        JOptionPane.showMessageDialog(null, "Please select a product!",
                "Selection Error", JOptionPane.WARNING_MESSAGE);
    } else {
        // Assuming ProductID is in the first column of the productData array
 String productIdString = (String) productData[productDataTable.getSelectedRow()][0];
 System.out.println(productIdString);
        int productId;
        try {
            productId = Integer.parseInt(productIdString); // Convert String to int
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Product ID format!",
                    "Format Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if parsing fails
        }        
        try {
            // Call deleteProduct method in your DAO, passing the ProductID
            if (productDAO.deleteProduct(productId)) {
                productData = productDAO.readProductsTableData(); // Refresh the product data
                JOptionPane.showMessageDialog(null, "Product deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                emptyBoxes(); // Clear input fields if necessary
                updateTable(); // Refresh the table view
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting product!",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    
  
  @Override
    public void actionPerformed(ActionEvent event) {
        Product product = new Product(
                selectedProductId,
                nameTextField.getText(),
                (String) categoryComboBox.getSelectedItem(),
                priceFieldsell.getText(),
                priceFieldbuy.getText()
        );

        if (event.getSource().equals(createProductButton)) {
            dbCreateProduct(product);
        } else if (event.getSource().equals(updateProductButton)) {
            dbUpdateProduct(product);
        } else if (event.getSource().equals(deleteProductButton)) {
            dbDeleteProduct();
        }
    }
        
    }

 

        