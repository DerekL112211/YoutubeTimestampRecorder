package com.example.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Factory class for creating standardized UI components
 * Reduces code duplication and ensures consistent styling
 */
public class UIComponents {
    
    // Standard button sizes
    public static final Dimension SMALL_BUTTON_SIZE = new Dimension(45, 30);
    public static final Dimension MEDIUM_BUTTON_SIZE = new Dimension(80, 30);
    public static final Dimension LARGE_BUTTON_SIZE = new Dimension(150, 30);
    
    // Standard colors
    public static final Color LIGHT_RED = new Color(255, 200, 200);
    public static final Color LIGHT_GREEN = new Color(200, 255, 200);
    
    // Custom font loading
    private static Font customFont = null;
    
    // Standard fonts
    public static final Font BUTTON_FONT = getCustomFont(Font.PLAIN, 14);
    public static final Font SMALL_BUTTON_FONT = getCustomFont(Font.PLAIN, 14);
    public static final Font LABEL_FONT = getCustomFont(Font.PLAIN, 16);
    public static final Font INPUT_FIELD_FONT = getCustomFont(Font.PLAIN, 16);
    
    // Standard insets
    public static final Insets SMALL_INSETS = new Insets(2, 2, 2, 2);
    public static final Insets MEDIUM_INSETS = new Insets(5, 5, 5, 5);
    
    /**
     * Loads and returns custom font, falls back to system font if loading fails
     */
    public static Font getCustomFont(int style, float size) {
        if (customFont == null) {
            try {
                // Load your custom font file from resources
                java.io.InputStream fontStream = UIComponents.class.getResourceAsStream("/fonts/MyFont.ttf");
                if (fontStream != null) {
                    customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                    // Register the font with the GraphicsEnvironment (optional but recommended)
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
                    fontStream.close();
                    System.out.println("Custom font loaded successfully: " + customFont.getName());
                } else {
                    System.err.println("Custom font file not found, using system default");
                }
            } catch (Exception e) {
                System.err.println("Failed to load custom font, using system default: " + e.getMessage());
            }
        }
        
        // If custom font loaded successfully, use it; otherwise fall back to system font
        if (customFont != null) {
            return customFont.deriveFont(style, size);
        } else {
            return new Font(Font.SANS_SERIF, style, (int) size);
        }
    }
    
    /**
     * Creates a standardized button with common properties
     */
    public static JButton createButton(String text, ActionListener action, Dimension size, String tooltip) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setPreferredSize(size);
        button.setFont(size.equals(SMALL_BUTTON_SIZE) ? SMALL_BUTTON_FONT : BUTTON_FONT);
        button.setMargin(SMALL_INSETS);
        if (tooltip != null) {
            button.setToolTipText(tooltip);
        }
        return button;
    }
    
    /**
     * Creates a small control button (+ or -)
     */
    public static JButton createControlButton(String text, ActionListener action, String tooltip) {
        return createButton(text, action, SMALL_BUTTON_SIZE, tooltip);
    }
    
    /**
     * Creates a time adjustment button with color coding
     */
    public static JButton createTimeButton(int seconds, ActionListener action) {
        String text = (seconds > 0 ? "+" : "") + seconds + "s";
        String tooltip = (seconds > 0 ? "Add " : "Subtract ") + Math.abs(seconds) + " seconds";
        Color backgroundColor = seconds > 0 ? LIGHT_GREEN : LIGHT_RED;
        
        JButton button = createButton(text, action, MEDIUM_BUTTON_SIZE, tooltip);
        button.setBackground(backgroundColor);
        return button;
    }
    
    /**
     * Creates a standard action button
     */
    public static JButton createActionButton(String text, ActionListener action) {
        return createButton(text, action, LARGE_BUTTON_SIZE, null);
    }
    
    /**
     * Creates a panel with FlowLayout
     */
    public static JPanel createFlowPanel(int alignment, int hgap, int vgap) {
        return new JPanel(new FlowLayout(alignment, hgap, vgap));
    }
    
    /**
     * Creates a panel with BorderLayout
     */
    public static JPanel createBorderPanel() {
        return new JPanel(new BorderLayout());
    }
    
    /**
     * Creates a panel with GridBagLayout
     */
    public static JPanel createGridBagPanel() {
        return new JPanel(new GridBagLayout());
    }
    
    /**
     * Creates a titled panel with border and custom font
     */
    public static JPanel createTitledPanel(String title, LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        
        // Create a titled border with custom font
        javax.swing.border.TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleFont(getCustomFont(Font.BOLD, 14)); // Bold 14pt for titles
        titledBorder.setTitleColor(new Color(60, 60, 60)); // Dark gray color
        
        panel.setBorder(titledBorder);
        return panel;
    }
    
    /**
     * Creates a custom titled border with specified font size and style
     */
    public static javax.swing.border.TitledBorder createCustomTitledBorder(String title, int fontStyle, float fontSize) {
        javax.swing.border.TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleFont(getCustomFont(fontStyle, fontSize));
        titledBorder.setTitleColor(new Color(60, 60, 60)); // Dark gray color
        return titledBorder;
    }
    
    /**
     * Creates a custom titled border with specified font size, style, and color
     */
    public static javax.swing.border.TitledBorder createCustomTitledBorder(String title, int fontStyle, float fontSize, Color titleColor) {
        javax.swing.border.TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleFont(getCustomFont(fontStyle, fontSize));
        titledBorder.setTitleColor(titleColor);
        return titledBorder;
    }
    
    /**
     * Applies custom styling to a JTable
     */
    public static void styleTable(JTable table) {
        // Set custom fonts
        table.setFont(INPUT_FIELD_FONT); // 16pt font for table cells
        table.getTableHeader().setFont(getCustomFont(Font.BOLD, 14)); // Bold headers
        
        // Set row height to accommodate larger font
        table.setRowHeight(26);
        
        // Optional: Set colors for better readability
        table.setGridColor(new Color(230, 230, 230)); // Light gray grid
        table.getTableHeader().setBackground(new Color(245, 245, 245)); // Light header background
        table.setSelectionBackground(new Color(184, 207, 229)); // Light blue selection
    }
    
    /**
     * Creates a standard text field with tooltip
     */
    public static JTextField createTextField(int columns, String tooltip, String defaultText) {
        JTextField field = new JTextField(columns);
        field.setFont(INPUT_FIELD_FONT);
        if (tooltip != null) {
            field.setToolTipText(tooltip);
        }
        if (defaultText != null) {
            field.setText(defaultText);
        }
        return field;
    }
    
    /**
     * Creates a standard label with custom font
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }
    
    /**
     * Creates GridBagConstraints with common settings
     */
    public static GridBagConstraints createGridBagConstraints(int gridx, int gridy, int gridwidth, 
                                                             int fill, double weightx, double weighty, 
                                                             int anchor, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.anchor = anchor;
        gbc.insets = insets;
        return gbc;
    }
    
    /**
     * Creates a standard note row panel with field and buttons
     */
    public static JPanel createNoteRowPanel(JTextField noteField, boolean showAddButton, 
                                          boolean showRemoveButton, ActionListener addAction, 
                                          ActionListener removeAction) {
        JPanel noteRowPanel = new JPanel(new BorderLayout(5, 0));
        noteRowPanel.add(noteField, BorderLayout.CENTER);
        
        JPanel buttonPanel = createFlowPanel(FlowLayout.LEFT, 2, 0);
        
        if (showAddButton) {
            JButton addButton = createControlButton("+", addAction, "Add another note");
            buttonPanel.add(addButton);
        }
        
        if (showRemoveButton) {
            JButton removeButton = createControlButton("-", removeAction, "Remove this note");
            buttonPanel.add(removeButton);
        }
        
        noteRowPanel.add(buttonPanel, BorderLayout.EAST);
        return noteRowPanel;
    }
}
