package view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.*;
import model.User;
import controller.UserDAO;

public class MenuFrameNew extends JFrame implements ActionListener {
    private final JPanel mainPanel, logoPanel, cardsPanel; // Added cardsPanel for CardLayout
    private final JLabel iconLabel, helloLabel, logoLabel;
    private final String deniedUserAccessLevel = "Seller";
    private JButton selectedButton;

    private String loggedUserName = "Unknown user", loggedUserAccessLevel = deniedUserAccessLevel, fileData;
    private final JButton DispensesButton,newSaleButton, searchSaleButton, manageUsersButton, manageProductsButton, DashboardButton, logoutButton;
    private final Dimension buttonsDimension = new Dimension(200, 25), helloDimension = new Dimension(220, 25),
            mainPanelDimension = new Dimension((int) buttonsDimension.getWidth() + 40, 0);
    private final Color mainPanelColor = new Color(250, 250, 250, 255), logoPanelColor = new Color(27, 26, 57, 255), buttonColor = Color.black;
    private final ImageIcon helloIcon = new ImageIcon("images\\icons\\username_icon.png"),
            logoIcon = new ImageIcon("images\\icons\\logo.png"),
            newSaleIcon = new ImageIcon("images\\icons\\shopping_cart_icon.png"),
            searchSaleIcon = new ImageIcon("images\\icons\\search_icon.png"),
            manageUsersIcon = new ImageIcon("images\\icons\\user_icon.png"),
            manageProductsIcon = new ImageIcon("images\\icons\\product_icon.png"),
            settingsIcon = new ImageIcon("images\\icons\\settings_icon.png"),
            feesIcon = new ImageIcon("images\\icons\\fees.png"),
            logoutIcon = new ImageIcon("images\\icons\\logout_icon.png");
    private final User currentlyLoggedUser;
    private final UserDAO userDAO;
    private final File settingsFile;
    private FileReader fileReader;
    private final HashMap<String, String> settingsMap = new HashMap<>();

    MenuFrameNew() throws SQLException, IOException {
        /*************************************** Setup ****************************************/
        userDAO = new UserDAO();
        fileData = "";
        StringBuilder stringBuilder = new StringBuilder(fileData);
        settingsFile = new File("app_local_settings.txt");
        if (settingsFile.exists()) {
            if (settingsFile.isFile()) {
                fileReader = new FileReader(settingsFile);
                int data = fileReader.read();
                while (data != -1) {
                    stringBuilder.append((char) data);
                    data = fileReader.read();
                }
                fileData = stringBuilder.toString();
                fileReader.close();
            }
        }

        String[] settingsStrings = fileData.split(",");
        for (String part : settingsStrings) {
            String[] partArray = part.split(":");
            settingsMap.put(partArray[0].trim(), partArray[1].trim());
        }
        currentlyLoggedUser = userDAO.readUser(settingsMap.get("currentlyLoggedUser"));
        if (currentlyLoggedUser != null) {
            loggedUserName = currentlyLoggedUser.getFullName();
            loggedUserAccessLevel = currentlyLoggedUser.getAccessLevel();
        }
        /*************************************** Setup ****************************************/
        /*************************************** Frame ***************************************/

        this.setTitle("Menu");
        this.setSize(1000, 720);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
        mainPanel.setPreferredSize(mainPanelDimension);
        mainPanel.setBackground(mainPanelColor);
        this.add(mainPanel, BorderLayout.WEST);

        logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setBackground(logoPanelColor);
        this.add(logoPanel, BorderLayout.CENTER);

        cardsPanel = new JPanel(new CardLayout()); // CardLayout for different views
        this.add(cardsPanel, BorderLayout.CENTER);

        /*************************************** Frame ***************************************/
        /*************************************** Icon ****************************************/

        iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon(helloIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        mainPanel.add(iconLabel);

        helloLabel = new JLabel("Hello, " + loggedUserName, SwingConstants.CENTER);
        helloLabel.setPreferredSize(helloDimension);
        mainPanel.add(helloLabel);

        /*************************************** Icon ****************************************/
        /*************************************** Logo ****************************************/

        logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(logoIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH)));
        logoPanel.add(logoLabel, new GridBagConstraints());

        /*************************************** Logo ****************************************/
        /************************************* Buttons **************************************/

        DashboardButton = new JButton("Dashboard");
        setButtonDesign(DashboardButton, loggedUserAccessLevel.equals(deniedUserAccessLevel) ? null : settingsIcon);
        if (loggedUserAccessLevel.equals(deniedUserAccessLevel)) {
            DashboardButton.setVisible(false); // Hide button if user is a "Seller"
        }

        newSaleButton = new JButton("New sale");
        setButtonDesign(newSaleButton, newSaleIcon);

        searchSaleButton = new JButton("Search sale");
        setButtonDesign(searchSaleButton, searchSaleIcon);

        manageUsersButton = new JButton("Manage users");
        setButtonDesign(manageUsersButton, loggedUserAccessLevel.equals(deniedUserAccessLevel) ? null : manageUsersIcon);
      

        manageProductsButton = new JButton("Manage products");
        setButtonDesign(manageProductsButton, manageProductsIcon);

        DispensesButton = new JButton("Fees");
        setButtonDesign(DispensesButton, loggedUserAccessLevel.equals(deniedUserAccessLevel) ? null : feesIcon);

        logoutButton = new JButton("Logout");
        setButtonDesign(logoutButton, logoutIcon);

          if (loggedUserAccessLevel.equals(deniedUserAccessLevel)) {
            manageUsersButton.setVisible(false); // Hide button if user is a "Seller"
                        DispensesButton.setVisible(false); // Hide button if user is a "Seller"
                          DashboardButton.setVisible(false); // Hide button if user is a "Seller"

        }
          
        /************************************* Buttons **************************************/
        // Set up cards
        setupCards();

        this.setVisible(true);
    }

    private void setupCards() throws SQLException {
        // Create instances of the panels for each card
        JPanel newSalePanel = new NewSalePanel();
        JPanel searchSalePanel = new SearchSalePanel();
        JPanel manageUsersPanel = new ManageUsersPanel();
        JPanel manageProductsPanel = new ManageProductsPanel();
        JPanel DashboardPanel = new DashboardPanel();
        JPanel DispensesPanel = new ManageChargesPanel();

        cardsPanel.add(DashboardPanel, "Dashboard");

        // Add the panels to the cardsPanel
        cardsPanel.add(newSalePanel, "NewSale");
        cardsPanel.add(searchSalePanel, "SearchSale");
        cardsPanel.add(manageUsersPanel, "ManageUsers");
        cardsPanel.add(manageProductsPanel, "ManageProducts");
        cardsPanel.add(DispensesPanel, "DispensesPanel");

        // Optionally, display the new sale panel first
        CardLayout cl = (CardLayout) (cardsPanel.getLayout());
        cl.show(cardsPanel, "NewSale");
    }

    private void setButtonDesign(JButton button, ImageIcon icon) {
        button.setPreferredSize(buttonsDimension);
        button.setFocusable(false);
        RoundedBorder r1 = new RoundedBorder(20, mainPanelColor);

        button.setBackground(new Color(255, 255, 255, 0)); // Transparent background
        button.setForeground(Color.BLACK); // Black text

        if (icon != null) {
            JLabel label = new JLabel();
            label.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
            mainPanel.add(label);
        }

        button.addActionListener(this);

        // Add mouse listener for hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                if (button != selectedButton) {
                    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    button.setBackground(Color.BLACK); // Change background to black on hover
                    button.setForeground(Color.WHITE); // Change text to white on hover
                }
            }

            @Override
            public void mouseExited(MouseEvent event) {
                if (button != selectedButton) {
                    button.setCursor(Cursor.getDefaultCursor());
                    button.setBackground(mainPanelColor); // Reset background to transparent
                    button.setForeground(Color.BLACK); // Reset text color to black
                }
            }
        });

        button.setMargin(new Insets(10, 20, 10, 20)); // Top, Left, Bottom, Right padding
        mainPanel.add(button);
    }

    // Modify actionPerformed to set the clicked button to stay black
    @Override
    public void actionPerformed(ActionEvent event) {
        CardLayout cl = (CardLayout) (cardsPanel.getLayout());
        JButton sourceButton = (JButton) event.getSource();

        // Change the selected button's style and reset others
        if (selectedButton != null && selectedButton != sourceButton) {
            selectedButton.setBackground(mainPanelColor);
            selectedButton.setForeground(Color.BLACK);
        }

        selectedButton = sourceButton;
        sourceButton.setBackground(Color.BLACK);
        sourceButton.setForeground(Color.WHITE);

        try {
            if (sourceButton.equals(DashboardButton)) {
                cl.show(cardsPanel, "Dashboard");
            } else if (sourceButton.equals(newSaleButton)) {
                cl.show(cardsPanel, "NewSale");
            } else if (sourceButton.equals(searchSaleButton)) {
                cl.show(cardsPanel, "SearchSale");
            } else if (sourceButton.equals(manageUsersButton)) {
                cl.show(cardsPanel, "ManageUsers");
            } else if (sourceButton.equals(manageProductsButton)) {
                cl.show(cardsPanel, "ManageProducts");
            } else if (sourceButton.equals(DispensesButton)) {
                cl.show(cardsPanel, "DispensesPanel");
            } else if (sourceButton.equals(logoutButton)) {
                System.exit(0); // Implement logout functionality here
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
