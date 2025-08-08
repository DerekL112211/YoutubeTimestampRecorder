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
    
    // Standard fonts
    public static final Font BUTTON_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    public static final Font SMALL_BUTTON_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    
    // Standard insets
    public static final Insets SMALL_INSETS = new Insets(2, 2, 2, 2);
    public static final Insets MEDIUM_INSETS = new Insets(5, 5, 5, 5);
    
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
     * Creates a titled panel with border
     */
    public static JPanel createTitledPanel(String title, LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }
    
    /**
     * Creates a standard text field with tooltip
     */
    public static JTextField createTextField(int columns, String tooltip, String defaultText) {
        JTextField field = new JTextField(columns);
        if (tooltip != null) {
            field.setToolTipText(tooltip);
        }
        if (defaultText != null) {
            field.setText(defaultText);
        }
        return field;
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
