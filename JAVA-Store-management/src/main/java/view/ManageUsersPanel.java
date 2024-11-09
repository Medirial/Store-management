package view;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.User;
import controller.UserDAO;

public class ManageUsersPanel extends JPanel implements ActionListener {
    private final JPanel inputsPanel, tablePanel, bottomPanel;
    private final JLabel usernameLabel, fullNameLabel, passwordLabel, emailLabel, userAccessLevelLabel, instructionLabel = null;
    private final JTextField usernameTextField, fullNameTextField, passwordTextField, emailTextField;
    private final JComboBox<String> userAccessLevelComboBox;
    private final JButton backButton, createUserButton, updateUserButton, deleteUserButton;
    private final Dimension labelDimension = new Dimension(60, 20), inputBoxDimension = new Dimension(180, 20),
            tableDimension = new Dimension(700, 400), buttonsDimension = new Dimension(105, 25);
    private final Color mainColor = Color.white, inputColor = Color.black;
    private final DefaultTableModel tableModel;
    private final JTable userDataTable;
    private final JScrollPane scrollPane;
    private Object[][] userData;
    private final String[] tableColumns;
    private final UserDAO userDAO;

    public ManageUsersPanel() throws SQLException {
        userDAO = new UserDAO();
 Color backgroundColor = new Color(240, 240, 240);
        Color panelColor = new Color(255, 255, 255);
        Color buttonColor = new Color(52, 152, 219);
        Color textColor = new Color(44, 62, 80);
        // SplitPane to separate the table and input panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        this.setLayout(new BorderLayout());
        this.add(splitPane, BorderLayout.CENTER);

       // Setting up input panel with GridBagLayout for precise row and column control
inputsPanel = new JPanel(new GridBagLayout());
inputsPanel.setBackground(mainColor);
splitPane.setBottomComponent(inputsPanel);

GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(5, 5, 5, 5);  // Spacing between components
gbc.fill = GridBagConstraints.HORIZONTAL;

// Row 0 - Username
usernameLabel = new JLabel("Username");
usernameTextField =createStyledTextField();
setTextFieldDesign(usernameLabel, usernameTextField);
gbc.gridx = 0;
gbc.gridy = 0;
inputsPanel.add(usernameLabel, gbc);
gbc.gridx = 1;
inputsPanel.add(usernameTextField, gbc);

// Row 1 - Full Name
fullNameLabel = new JLabel("Full name");
fullNameTextField = createStyledTextField();
setTextFieldDesign(fullNameLabel, fullNameTextField);
gbc.gridx = 0;
gbc.gridy = 1;
inputsPanel.add(fullNameLabel, gbc);
gbc.gridx = 1;
inputsPanel.add(fullNameTextField, gbc);

// Row 2 - Password
passwordLabel = new JLabel("Password");
passwordTextField = createStyledTextField();
setTextFieldDesign(passwordLabel, passwordTextField);
gbc.gridx = 0;
gbc.gridy = 2;
inputsPanel.add(passwordLabel, gbc);
gbc.gridx = 1;
inputsPanel.add(passwordTextField, gbc);

// Row 3 - Email
emailLabel = new JLabel("E-mail");
emailTextField =  createStyledTextField();
setTextFieldDesign(emailLabel, emailTextField);
gbc.gridx = 0;
gbc.gridy = 3;
inputsPanel.add(emailLabel, gbc);
gbc.gridx = 1;
inputsPanel.add(emailTextField, gbc);

// Row 4 - Access Level
userAccessLevelLabel = new JLabel("Access");
userAccessLevelComboBox = new JComboBox<>(new String[]{null, "Administrator", "Seller"});
userAccessLevelComboBox.setPreferredSize(inputBoxDimension);
gbc.gridx = 0;
gbc.gridy = 4;
inputsPanel.add(userAccessLevelLabel, gbc);
gbc.gridx = 1;
inputsPanel.add(userAccessLevelComboBox, gbc);

// Row 5 - Button Row
gbc.gridwidth = 2;  // Span across two columns
gbc.gridx = 0;
gbc.gridy = 5;
gbc.fill = GridBagConstraints.CENTER;

// Creating button panel to add all buttons in the same row
JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
buttonPanel.setBackground(mainColor);

createUserButton = new JButton("Create User");
setButtonDesign(createUserButton);
buttonPanel.add(createUserButton);

updateUserButton = new JButton("Update User");
setButtonDesign(updateUserButton);
buttonPanel.add(updateUserButton);

deleteUserButton = new JButton("Delete User");
setButtonDesign(deleteUserButton);
buttonPanel.add(deleteUserButton);

inputsPanel.add(buttonPanel, gbc);


        // Table Panel
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(mainColor);
        splitPane.setTopComponent(tablePanel);

        tableColumns = new String[]{"Username", "Full name", "Password", "E-mail", "Access Level"};
        userData = userDAO.readUsersTableData();
        tableModel = new DefaultTableModel(userData, tableColumns);
 
        userDataTable = new JTable(tableModel) {
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };

          userDataTable.getTableHeader().setFont(new Font("poppinsFont", Font.BOLD, 12));
        userDataTable.getTableHeader().setOpaque(false);
        userDataTable.getTableHeader().setBackground(new Color(32, 136, 203));
        userDataTable.getTableHeader().setForeground(new Color(255,255,255));
        userDataTable.setRowHeight(25);
        // Add listener for row selection
        userDataTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && userDataTable.getSelectedRow() != -1) {
                    fillFieldsFromSelectedRow();
                }
            }
        });

        scrollPane = new JScrollPane(userDataTable);
        scrollPane.setPreferredSize(tableDimension);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 5));
        bottomPanel.setPreferredSize(new Dimension(0, 50));
        bottomPanel.setBackground(mainColor);
        this.add(bottomPanel, BorderLayout.SOUTH);

        backButton = new JButton("Back");
      
    }

    private void setTextFieldDesign(JLabel label, JTextField textField) {
        
        inputsPanel.add(label);

      
        inputsPanel.add(textField);
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

 
    private boolean isBoxesEmpty() {
        return fullNameTextField.getText().isBlank() || usernameTextField.getText().isBlank()
                || passwordTextField.getText().isBlank() || emailTextField.getText().isBlank()
                || userAccessLevelComboBox.getSelectedItem() == null;
    }

    private void emptyBoxes() {
        usernameTextField.setText(null);
        fullNameTextField.setText(null);
        passwordTextField.setText(null);
        emailTextField.setText(null);
        userAccessLevelComboBox.setSelectedIndex(0);
    }

    private void updateTable() {
        tableModel.setDataVector(userData, tableColumns);
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
        int selectedRow = userDataTable.getSelectedRow();
        if (selectedRow != -1) {
            usernameTextField.setText((String) tableModel.getValueAt(selectedRow, 0));
            fullNameTextField.setText((String) tableModel.getValueAt(selectedRow, 1));
            passwordTextField.setText((String) tableModel.getValueAt(selectedRow, 2));
            emailTextField.setText((String) tableModel.getValueAt(selectedRow, 3));
            userAccessLevelComboBox.setSelectedItem((String) tableModel.getValueAt(selectedRow, 4));
        }
    }

    private void dbCreateUser(User user) {
        if (isBoxesEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                if (userDAO.createUser(user)) {
                    userData = userDAO.readUsersTableData();
                    JOptionPane.showMessageDialog(null, "User created successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    emptyBoxes();
                    updateTable();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error creating user!",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void dbUpdateUser(User user) {
        if (isBoxesEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                if (userDAO.updateUser(user)) {
                    userData = userDAO.readUsersTableData();
                    JOptionPane.showMessageDialog(null, "User updated successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    emptyBoxes();
                    updateTable();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error updating user!",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void dbDeleteUser() {
        if (userDataTable.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a user!",
                    "Selection Error", JOptionPane.WARNING_MESSAGE);
        } else {
            String username = (String) userData[userDataTable.getSelectedRow()][0];
            try {
                if (userDAO.deleteUser(username)) {
                    userData = userDAO.readUsersTableData();
                    JOptionPane.showMessageDialog(null, "User deleted successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    emptyBoxes();
                    updateTable();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error deleting user!",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == createUserButton) {
            dbCreateUser(new User(usernameTextField.getText(), fullNameTextField.getText(),
                    passwordTextField.getText(), emailTextField.getText(),
                    (String) userAccessLevelComboBox.getSelectedItem()));
        } else if (source == updateUserButton) {
            dbUpdateUser(new User(usernameTextField.getText(), fullNameTextField.getText(),
                    passwordTextField.getText(), emailTextField.getText(),
                    (String) userAccessLevelComboBox.getSelectedItem()));
        } else if (source == deleteUserButton) {
            dbDeleteUser();
        } else if (source == backButton) {
            // Go back to previous screen
        }
    }
}
