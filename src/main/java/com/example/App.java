package com.example;

import com.example.model.TimestampModel;
import com.example.view.SwingTimestampView;
import com.example.controller.TimestampController;
import javax.swing.SwingUtilities;

/**
 * Main application class that sets up and starts the Timestamp Recorder
 * Uses MVC architecture pattern
 */
public class App {
    public static void main(String[] args) {
        // Ensure GUI creation happens on EDT
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better OS integration
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Fallback to default look and feel
                System.err.println("Could not set system look and feel: " + e.getMessage());
            }
            
            // Create MVC components
            TimestampModel model = new TimestampModel();
            SwingTimestampView view = new SwingTimestampView();
            TimestampController controller = new TimestampController(model, view);
            
            // Wire them together
            view.setController(controller);
            
            // Show the application
            view.show();
            
            System.out.println("Timestamp Recorder v3.0 (MVC Architecture) started successfully!");
        });
    }
}