package com.example.view;

import com.example.controller.TimestampController;
import com.example.TimestampEntry;
import java.util.List;

/**
 * Interface defining the contract for the timestamp view
 * This allows for different view implementations (Swing, JavaFX, etc.)
 */
public interface TimestampView {
    
    /**
     * Set the controller for this view
     */
    void setController(TimestampController controller);
    
    /**
     * Refresh the timestamp table with new data
     */
    void refreshTable(List<TimestampEntry> timestamps);
    
    /**
     * Clear all note fields in the input area
     */
    void clearAllNoteFields();
    
    /**
     * Set the timestamp field value
     */
    void setTimestampField(String timestamp);
    
    /**
     * Show an error message to the user
     */
    void showError(String message);
    
    /**
     * Show a success message to the user
     */
    void showSuccess(String message);
    
    /**
     * Show confirmation dialog for clearing all timestamps
     * @return true if user confirms, false otherwise
     */
    boolean confirmClearAll();
    
    /**
     * Show the main window/interface
     */
    void show();
    
    /**
     * Hide/dispose the main window
     */
    void hide();
}
