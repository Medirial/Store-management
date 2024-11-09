package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.ProductSell;
import model.Sale;
import controller.ProductDAO;
import controller.ProductSellDAO;
import controller.SaleDAO;
import controller.UserDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.awt.Desktop;
import java.io.File;


public class NewSalePanel extends JPanel implements ActionListener {
    private final JPanel bottomPanel, inputsPanel;
    private final JLabel sellerUsernameLabel, totalCostLabel;
    private final JTextField totalCostTextField;
    private final JComboBox<String> sellerUsernameComboBox;
    private final JButton backButton, addItemButton, printButton, removeItemButton, saveSaleButton;
    private final Dimension labelDimension = new Dimension(65, 20), inputBoxDimension = new Dimension(180, 20),
            inputPanelDimension = new Dimension((int) (labelDimension.getWidth() + inputBoxDimension.getWidth()) + 20, 0),
            tableDimension = new Dimension(0, 310), buttonsDimension = new Dimension(100, 25);
    private final Color mainColor = Color.white, inputColor = Color.black;
    private final DefaultTableModel saleTableModel, productsTableModel;
    private final JTable saleTable, productsTable;
    private final JScrollPane saleScrollPane, productsScrollPane;
    private Object[][] saleData;
    private final Object[][] productsData;
    private final String[] attendantsArray, tableColumns = new String[]{"Id", "Name", "Category", "Price"};
    private final List<String> itemsList = new ArrayList<>();
    private int tableRowsNumber = 0;
    private final int tableColumnsNumber = 4;
    private Float totalCost = 0.0f;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime now;
    private final SaleDAO saleDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    public NewSalePanel() throws SQLException {
        saleDAO = new SaleDAO();
        productDAO = new ProductDAO();
        userDAO = new UserDAO();

           Color backgroundColor = new Color(240, 240, 240);
        Color panelColor = new Color(255, 255, 255);
        Color buttonColor = new Color(52, 152, 219);
        Color textColor = new Color(44, 62, 80);

        
        /****************************** Panel ******************************/
        this.setLayout(new BorderLayout());

        inputsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        inputsPanel.setPreferredSize(inputPanelDimension);
        inputsPanel.setBackground(mainColor);
        this.add(inputsPanel, BorderLayout.WEST);

        JPanel tablesPanel = new JPanel(new BorderLayout());
        tablesPanel.setBackground(mainColor);
        this.add(tablesPanel, BorderLayout.CENTER);

        /****************************** Input ******************************/
        sellerUsernameLabel = new JLabel("Seller");
        sellerUsernameLabel.setPreferredSize(labelDimension);
        sellerUsernameLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        inputsPanel.add(sellerUsernameLabel);

        attendantsArray = userDAO.readAllAttendants();
        sellerUsernameComboBox = new JComboBox<>(attendantsArray);
        sellerUsernameComboBox.setPreferredSize(inputBoxDimension);
        sellerUsernameComboBox.setFocusable(false);
        inputsPanel.add(sellerUsernameComboBox);

        totalCostLabel = new JLabel("Total cost");
        totalCostLabel.setPreferredSize(labelDimension);
        totalCostLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        inputsPanel.add(totalCostLabel);

        totalCostTextField = new JTextField(String.format("$ %.2f", totalCost));
        totalCostTextField.setPreferredSize(inputBoxDimension);
        totalCostTextField.setForeground(inputColor);
        totalCostTextField.setBorder(BorderFactory.createLineBorder(inputColor));
        inputsPanel.add(totalCostTextField);

        /****************************** Input ******************************/
        /***************************** Buttons *****************************/

//        instructionLabel = new JLabel("<html>Select from tables<br>to add or remove</html>");
//        instructionLabel.setFont(new Font("Calibri", Font.BOLD, 12));
//        instructionLabel.setPreferredSize(buttonsDimension);
//        inputsPanel.add(instructionLabel);

        
       
        
        addItemButton = new JButton("Add item");
        setButtonDesign(addItemButton);
        inputsPanel.add(addItemButton);

        
        removeItemButton = new JButton("Remove Item");
        setButtonDesign(removeItemButton);
        inputsPanel.add(removeItemButton);

        saveSaleButton = new JButton("Save sale");
        setButtonDesign(saveSaleButton);
        inputsPanel.add(saveSaleButton);
          printButton = new JButton("Print");
        setButtonDesign(printButton);
        inputsPanel.add(printButton);

        /***************************** Buttons *****************************/
        /************************** Products Table **************************/

        productsData = productDAO.readProductsTableData();
        productsTableModel = new DefaultTableModel(productsData, tableColumns);

      
        productsTable = new JTable(productsTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        
        productsTable.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent event) {
                productsTable.clearSelection();
            }
        });

         productsTable.setBackground(panelColor);
        productsTable.setForeground(textColor);
        productsTable.setGridColor(Color.LIGHT_GRAY);
        productsTable.getTableHeader().setFont(new Font("poppinsFont", Font.BOLD, 12));
        productsTable.getTableHeader().setOpaque(false);
        productsTable.getTableHeader().setBackground(new Color(32, 136, 203));
        productsTable.getTableHeader().setForeground(Color.WHITE);
        productsTable.setRowHeight(25);
        productsTable.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        // Check if the event is a double-click (click count of 2)
        if (e.getClickCount() == 2) {
            // Call the addItem method directly on double-click
            addItem();
        }
    }
});
        productsScrollPane = new JScrollPane(productsTable);
        productsScrollPane.setPreferredSize(tableDimension);
        
                printButton.addActionListener(e -> printInvoice()); // Add action to the print button

        
        tablesPanel.add(productsScrollPane, BorderLayout.NORTH);

        /************************** Products Table **************************/
        /**************************** Sale Table ****************************/

        
       saleTableModel = new DefaultTableModel(null, tableColumns) {
    @Override
    public boolean isCellEditable(int row, int column) {
        // Only allow editing the Price column (index 3)
        return column == 3;
    }
};
  
      saleTable = new JTable(saleTableModel) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 3; // Only the Price column (index 3) should be editable
    }
};

        

        
               saleTable.setBackground(panelColor);
        saleTable.setForeground(textColor);
        saleTable.setGridColor(Color.LIGHT_GRAY);
        saleTable.getTableHeader().setFont(new Font("poppinsFont", Font.BOLD, 12));
        saleTable.getTableHeader().setOpaque(false);
        saleTable.getTableHeader().setBackground(new Color(32, 136, 203));
        saleTable.getTableHeader().setForeground(Color.WHITE);
        saleTable.setRowHeight(25);
        saleTableModel.addTableModelListener(new TableModelListener() {
    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        
        // Check if the edited cell is in the Price column
        if (column == 3) {
            // Update total cost based on edited price in the saleTable
            updateTotalCost();
        }
    }
});
        saleTable.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent event) {
                saleTable.clearSelection();
            }
        });

        saleScrollPane = new JScrollPane(saleTable);
        saleScrollPane.setPreferredSize(tableDimension);
        tablesPanel.add(saleScrollPane, BorderLayout.SOUTH);

        /**************************** Sale Table ****************************/
        /****************************** Frame ******************************/

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        bottomPanel.setPreferredSize(new Dimension(0, 50));
        bottomPanel.setBackground(mainColor);
        this.add(bottomPanel, BorderLayout.SOUTH);

        backButton = new JButton("Back");
        setButtonDesign(backButton);
        
    }
 // Define the printInvoice method to create and open a PDF invoice
    private void printInvoice() {
        Document document = new Document();
        try {
            // Specify the file path and name
            String filePath = "Invoice.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add title
            document.add(new Paragraph("Invoice"));
            document.add(new Paragraph("Date: " + LocalDateTime.now().format(dateTimeFormatter)));
            document.add(new Paragraph("Seller: " + sellerUsernameComboBox.getSelectedItem()));
            document.add(new Paragraph("\n")); // Blank line

            // Create table for sale items
            PdfPTable table = new PdfPTable(tableColumnsNumber);
            for (String column : tableColumns) {
                PdfPCell header = new PdfPCell(new Phrase(column));
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(header);
            }

            // Add sale items to the table
            for (int i = 0; i < saleTableModel.getRowCount(); i++) {
                for (int j = 0; j < saleTableModel.getColumnCount(); j++) {
                    String cellData = (String) saleTableModel.getValueAt(i, j);
                    table.addCell(cellData != null ? cellData : "");
                }
            }

            // Add table and total cost
            document.add(table);
            document.add(new Paragraph("\nTotal Cost: " + totalCostTextField.getText()));

            document.close();
            JOptionPane.showMessageDialog(null, "Invoice PDF created successfully!");

            // Open the PDF file
            Desktop.getDesktop().open(new File(filePath));
        } catch (FileNotFoundException | DocumentException e) {
            JOptionPane.showMessageDialog(null, "Error creating PDF: " + e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error opening PDF: " + e.getMessage());
        }
    }
private void updateTotalCost() {
    totalCost = 0.0f;
    
    for (int i = 0; i < saleTableModel.getRowCount(); i++) {
        String priceText = (String) saleTableModel.getValueAt(i, 3);
        float price = Float.parseFloat(priceText.replaceAll(",", "."));
        totalCost += price;
    }
    
    totalCostTextField.setText(String.format("$ %.2f", totalCost));
}

        private void setButtonDesign(JButton button) {
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

            @Override
            public void mouseExited(MouseEvent event) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });
    }
    private void updateTable() {
        int currentRowCount = saleTableModel.getRowCount();
        saleTableModel.setRowCount(0);
        saleTableModel.setRowCount(currentRowCount);
        saleTableModel.setDataVector(saleData, tableColumns);
    }

    private String[][] getSaleItems() {
        int aux = 0;

        String[][] items = new String[tableRowsNumber][tableColumnsNumber];
        for (int i = 0; i < tableRowsNumber; i++) {
            for (int j = 0; j < tableColumnsNumber; j++) {
                items[i][j] = itemsList.get(aux++);
            }
        }
        return items;
    }

    private void addItem() {
        if (productsTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "You must pick a line from the products table!",
                    "Selection error", JOptionPane.WARNING_MESSAGE);
        } else {
            tableRowsNumber++;

            totalCost += Float.parseFloat(((String) productsData[productsTable
                    .getSelectedRow()][3]).replaceAll(",", "."));
            totalCostTextField.setText(String.format("$ %.2f", totalCost));

            itemsList.add((String) productsData[productsTable.getSelectedRow()][0]);
            itemsList.add((String) productsData[productsTable.getSelectedRow()][1]);
            itemsList.add((String) productsData[productsTable.getSelectedRow()][2]);
            itemsList.add((String) productsData[productsTable.getSelectedRow()][3]);

            saleData = getSaleItems();
            updateTable();
        }
    }

    private void removeItem() {
        if (saleTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "You must pick a line from the sale table!",
                    "Selection error", JOptionPane.WARNING_MESSAGE);
        } else {
            tableRowsNumber--;

            totalCost -= Float.parseFloat(((String) saleData[saleTable
                    .getSelectedRow()][3]).replaceAll(",", "."));
            totalCostTextField.setText(String.format("$ %.2f", totalCost));

            int index = saleTable.getSelectedRow() * tableColumnsNumber;
            for (int i = 0; i < tableColumnsNumber; i++) {
                itemsList.remove(index);
            }

            saleData = getSaleItems();
            updateTable();
        }
    }

    private void saveSale() {
        if (sellerUsernameComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "You must pick a user from the attendant box!",
                    "Selection error", JOptionPane.WARNING_MESSAGE);
        } else {
            now = LocalDateTime.now();
            Sale sale = new Sale(0,
                    totalCost,
                    (String) (sellerUsernameComboBox.getSelectedItem()),
                    dateTimeFormatter.format(now));
            try {
                int foreignIDSale =saleDAO.createSale(sale);
                if (foreignIDSale != -1) {
                       for (int i = 0; i < saleTableModel.getRowCount(); i++) {
                           
                     int foreignIDProduct =       Integer.parseInt((String) saleTableModel.getValueAt(i, 0));
        String date = now.format(dateTimeFormatter); // Use current date and time
        String priceAtSale = (String) saleTableModel.getValueAt(i, 3); // Assuming the price is in the last column
        String benefit = "2"; // You need to define how to calculate this

        ProductSell productSell = new ProductSell(foreignIDProduct, foreignIDSale, date, priceAtSale, benefit);
        ProductSellDAO productSellDAO = new ProductSellDAO();
        productSellDAO.createProductSell(productSell);
    }
                    JOptionPane.showMessageDialog(null, "This sale has been saved successfully!");
//                    backToMenu();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == addItemButton) {
            addItem();
        } else if (source == removeItemButton) {
            removeItem();
        } else if (source == saveSaleButton) {
            saveSale();
        } else if (source == backButton) {
            // Back button logic here
        }
    }
}
