package view;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.Charge;
import controller.ChargeDAO;

public class ManageChargesPanel extends JPanel implements ActionListener {
    private final JPanel inputsPanel, tablePanel;
    private final JLabel descriptionLabel, amountLabel, dateLabel, typeLabel;
    private final JTextField descriptionTextField, amountTextField, dateTextField;
    private final JComboBox<String> typeComboBox;  

    private final JButton createChargeButton, updateChargeButton, deleteChargeButton;
    private final DefaultTableModel tableModel;
    private final JTable chargeDataTable;
    private final JScrollPane scrollPane;
    private Object[][] chargeData;
    private final String[] tableColumns = {"ID", "Description", "Date", "Type","Amount"};
    private final ChargeDAO chargeDAO;

    private int selectedChargeId = -1;  // Track the selected Charge ID

    public ManageChargesPanel() throws SQLException {
        chargeDAO = new ChargeDAO();

        // Color configurations
        Color backgroundColor = new Color(240, 240, 240);
        Color panelColor = new Color(255, 255, 255);
        Color buttonColor = new Color(52, 152, 219);
        Color textColor = new Color(44, 62, 80);

        this.setLayout(new BorderLayout());

        // Table Panel
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(backgroundColor);

        chargeData = chargeDAO.readChargesTableData();
        tableModel = new DefaultTableModel(chargeData, tableColumns);

        chargeDataTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        chargeDataTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && chargeDataTable.getSelectedRow() != -1) {
                    fillFieldsFromSelectedRow();
                }
            }
        });
        chargeDataTable.setBackground(panelColor);
        chargeDataTable.setForeground(textColor);
        chargeDataTable.setGridColor(Color.LIGHT_GRAY);
        chargeDataTable.getTableHeader().setFont(new Font("poppinsFont", Font.BOLD, 12));
        chargeDataTable.getTableHeader().setOpaque(false);
        chargeDataTable.getTableHeader().setBackground(new Color(32, 136, 203));
        chargeDataTable.getTableHeader().setForeground(Color.WHITE);
        chargeDataTable.setRowHeight(25);
        scrollPane = new JScrollPane(chargeDataTable);
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

        // Description Label and Text Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        descriptionLabel = createStyledLabel("Description", textColor);
        inputsPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        descriptionTextField = createStyledTextField();
        inputsPanel.add(descriptionTextField, gbc);

        // Amount Label and Text Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        amountLabel = createStyledLabel("Amount (USD)", textColor);
        inputsPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        amountTextField = createStyledTextField();
        inputsPanel.add(amountTextField, gbc);

        // Date Label and Text Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        dateLabel = createStyledLabel("Date (yyyy-mm-dd)", textColor);
        inputsPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        dateTextField = createStyledTextField();
        inputsPanel.add(dateTextField, gbc);

        // Type Label and Combo Box
        gbc.gridx = 0;
        gbc.gridy = 3;
        typeLabel = createStyledLabel("Type", textColor);
        inputsPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        typeComboBox = new JComboBox<>(new String[]{"One-Time", "Monthly", "Daily"});
        typeComboBox.setPreferredSize(new Dimension(180, 20));
        inputsPanel.add(typeComboBox, gbc);

        // Button Panel
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(panelColor);

        createChargeButton = new JButton("Create");
        setButtonDesign(createChargeButton);
        buttonPanel.add(createChargeButton);

        updateChargeButton = new JButton("Update");
        setButtonDesign(updateChargeButton);
        buttonPanel.add(updateChargeButton);

        deleteChargeButton = new JButton("Delete");
        setButtonDesign(deleteChargeButton);
        buttonPanel.add(deleteChargeButton);

        inputsPanel.add(buttonPanel, gbc);

        // Split Pane: now the order is switched
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputsPanel, tablePanel); // Inputs at the bottom
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private void setButtonDesign(JButton button) {
        button.setPreferredSize(new Dimension(105, 25));
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
        });
    }

    private JLabel createStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Sans Serif", Font.BOLD, 14));
        label.setForeground(color);
        label.setPreferredSize(new Dimension(100, 35));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(180, 20));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.DARK_GRAY);
        textField.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        return textField;
    }

    private void fillFieldsFromSelectedRow() {
        int selectedRow = chargeDataTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedChargeId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            descriptionTextField.setText((String) tableModel.getValueAt(selectedRow, 1));
            amountTextField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
            dateTextField.setText((String) tableModel.getValueAt(selectedRow, 2));
            typeComboBox.setSelectedItem((String) tableModel.getValueAt(selectedRow, 3));
        }
    }

    private boolean isBoxesEmpty() {
        return (descriptionTextField.getText().isBlank() || amountTextField.getText().isBlank()
                || dateTextField.getText().isBlank());
    }

    private void emptyBoxes() {
        descriptionTextField.setText(null);
        amountTextField.setText(null);
        dateTextField.setText(null);
        typeComboBox.setSelectedIndex(-1); // Reset the ComboBox
    }

    private void updateTable() throws SQLException {
        chargeData = chargeDAO.readChargesTableData(); // Refresh charge data
        tableModel.setDataVector(chargeData, tableColumns);
    }

    private void dbCreateCharge(Charge charge) {
        if (isBoxesEmpty()) {
            JOptionPane.showMessageDialog(null, "You must fill all text fields!", "Input error", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                if (chargeDAO.addCharge(charge)) {
                    JOptionPane.showMessageDialog(null, "Charge created successfully!", "Charge created", JOptionPane.INFORMATION_MESSAGE);
                    emptyBoxes();
                    updateTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to create charge", "Creation error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void dbUpdateCharge(Charge charge) {
        if (isBoxesEmpty()) {
            JOptionPane.showMessageDialog(null, "You must fill all text fields!", "Input error", JOptionPane.WARNING_MESSAGE);
        } else if (selectedChargeId == -1) {
            JOptionPane.showMessageDialog(null, "Select a charge to update", "Selection error", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                charge.setId(selectedChargeId); // Set ID for update
                if (chargeDAO.updateCharge(charge)) {
                    JOptionPane.showMessageDialog(null, "Charge updated successfully!", "Charge updated", JOptionPane.INFORMATION_MESSAGE);
                    emptyBoxes();
                    updateTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update charge", "Update error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void dbDeleteCharge() {
        if (selectedChargeId == -1) {
            JOptionPane.showMessageDialog(null, "Select a charge to delete", "Selection error", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                if (chargeDAO.deleteCharge(selectedChargeId)) {
                    JOptionPane.showMessageDialog(null, "Charge deleted successfully!", "Charge deleted", JOptionPane.INFORMATION_MESSAGE);
                    emptyBoxes();
                    updateTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete charge", "Deletion error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Charge charge = new Charge();
        charge.setDesc(descriptionTextField.getText());
        charge.setAmount(Double.parseDouble(amountTextField.getText()));
        charge.setDate(dateTextField.getText());
        charge.setType((String) typeComboBox.getSelectedItem());
        if (source == createChargeButton) {
            dbCreateCharge(charge);
        } else if (source == updateChargeButton) {
            charge.setId(selectedChargeId);  // Set ID for the update operation
            dbUpdateCharge(charge);
        } else if (source == deleteChargeButton) {
            dbDeleteCharge();
        }
    }
}
