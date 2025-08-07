package com.example;

import javax.swing.SwingUtilities;

/**
 * Main application entry point for YouTube Timestamp Recorder
 */
public class App {
    public static void main(String[] args) {
        // Create and show GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            TimestampRecorderGUI gui = new TimestampRecorderGUI();
            gui.createAndShowGUI();
        });
    }
}