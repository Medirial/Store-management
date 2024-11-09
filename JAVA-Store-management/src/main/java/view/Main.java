package view;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Load the Poppins font from the file
            Font poppinsFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Poppins-SemiBold.ttf"));
            // Register the font
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(poppinsFont);
            
            // Set the font size and style (optional, e.g., plain, bold)
            poppinsFont = poppinsFont.deriveFont(14f); // Set to desired size

            // Set a global font for all components
            UIManager.put("Button.font", poppinsFont);
            UIManager.put("Label.font", poppinsFont);
            UIManager.put("TextField.font", poppinsFont);
            UIManager.put("TextArea.font", poppinsFont);
            UIManager.put("ComboBox.font", poppinsFont);
            UIManager.put("Table.font", poppinsFont);
            UIManager.put("List.font", poppinsFont);
            UIManager.put("MenuItem.font", poppinsFont);

            // Create and show the LoginFrame
            SwingUtilities.invokeLater(() -> {
                try {
                    new LoginFrame();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            });
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
