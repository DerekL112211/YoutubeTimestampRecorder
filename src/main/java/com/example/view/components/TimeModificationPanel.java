package com.example.view.components;

import javax.swing.*;
import java.awt.*;
import java.util.function.IntConsumer;

/**
 * Specialized panel for time modification buttons
 * Encapsulates the creation of time adjustment controls
 */
public class TimeModificationPanel extends JPanel {
    
    private final IntConsumer timeModifierCallback;
    
    public TimeModificationPanel(IntConsumer timeModifierCallback) {
        super(new GridBagLayout());
        this.timeModifierCallback = timeModifierCallback;
        initializeComponents();
    }
    
    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Label
        gbc.insets = UIComponents.MEDIUM_INSETS;
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(UIComponents.createLabel("Time Adjust:"), gbc);
        
        // Subtract time buttons (first row)
        gbc.gridx = 1; 
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        int[] subtractTimes = {-30, -15, -10, -5};
        JPanel subtractPanel = createTimeButtonPanel(subtractTimes);
        add(subtractPanel, gbc);
        
        // Add time buttons (second row)
        gbc.gridx = 1; 
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        int[] addTimes = {5, 10, 15, 30};
        JPanel addPanel = createTimeButtonPanel(addTimes);
        add(addPanel, gbc);
    }
    
    private JPanel createTimeButtonPanel(int[] times) {
        JPanel panel = UIComponents.createFlowPanel(FlowLayout.LEFT, 5, 0);
        
        for (int seconds : times) {
            JButton btn = UIComponents.createTimeButton(seconds, e -> timeModifierCallback.accept(seconds));
            panel.add(btn);
        }
        
        return panel;
    }
}
