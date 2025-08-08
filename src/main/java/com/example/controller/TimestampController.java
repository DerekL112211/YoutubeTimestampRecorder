package com.example.controller;

import com.example.model.TimestampModel;
import com.example.view.TimestampView;
import com.example.TimestampEntry;
import com.example.TimestampType;
import com.example.FileService;
import java.io.File;
import java.util.List;

/**
 * Controller class that coordinates between Model and View
 * Handles user interactions and business logic coordination
 */
public class TimestampController {
    private TimestampModel model;
    private TimestampView view;
    private FileService fileService;
    
    public TimestampController(TimestampModel model, TimestampView view) {
        this.model = model;
        this.view = view;
        this.fileService = new FileService();
        
        // Set up the controller as an observer of the model
        model.addObserver((o, arg) -> {
            view.refreshTable(model.getTimestamps());
        });
        
        // Initialize view with controller reference
        view.setController(this);
    }
    
    /**
     * Handle adding a new main timestamp
     */
    public void addTimestamp(String timestamp, List<String> notes) {
        if (timestamp == null || timestamp.trim().isEmpty()) {
            view.showError("Please enter a timestamp");
            return;
        }
        
        // Combine notes with pipe separator
        String combinedNotes = combineNotes(notes);
        
        if (model.addTimestamp(timestamp.trim(), combinedNotes, TimestampType.MAIN)) {
            view.clearAllNoteFields();
        } else {
            view.showError("Invalid timestamp format or duplicate timestamp. Use mm:ss or hh:mm:ss");
        }
    }
    
    /**
     * Handle adding a new sub-timestamp
     */
    public void addSubTimestamp(String timestamp, List<String> notes) {
        if (timestamp == null || timestamp.trim().isEmpty()) {
            view.showError("Please enter a timestamp");
            return;
        }
        
        // Combine notes with pipe separator
        String combinedNotes = combineNotes(notes);
        
        if (model.addTimestamp(timestamp.trim(), combinedNotes, TimestampType.SUB)) {
            view.clearAllNoteFields();
        } else {
            view.showError("Invalid timestamp format or duplicate timestamp. Use mm:ss or hh:mm:ss");
        }
    }
    
    /**
     * Handle timestamp modification (add/subtract seconds)
     */
    public void modifyTimestamp(String currentTimestamp, int seconds) {
        if (currentTimestamp == null || currentTimestamp.trim().isEmpty()) {
            view.showError("Please enter a timestamp first");
            return;
        }
        
        String modifiedTimestamp = model.modifyTimestamp(currentTimestamp, seconds);
        view.setTimestampField(modifiedTimestamp);
    }
    
    /**
     * Handle deleting a selected timestamp
     */
    public void deleteSelected(int selectedRow) {
        if (selectedRow >= 0) {
            if (model.removeTimestamp(selectedRow)) {
                view.showSuccess("Timestamp deleted successfully!");
            }
        } else {
            view.showError("Please select a timestamp to delete");
        }
    }
    
    /**
     * Handle clearing all timestamps
     */
    public void clearAll() {
        if (view.confirmClearAll()) {
            model.clearAll();
            view.showSuccess("All timestamps cleared!");
        }
    }
    
    /**
     * Handle updating a note in the table
     */
    public void updateNote(int row, String newNote) {
        model.updateNote(row, newNote);
    }
    
    /**
     * Handle loading timestamps from file
     */
    public void loadTimestamps(File file) {
        if (file != null) {
            List<TimestampEntry> loadedTimestamps = fileService.loadTimestamps(file);
            
            if (!loadedTimestamps.isEmpty()) {
                model.setTimestamps(loadedTimestamps);
                view.showSuccess("Timestamps loaded successfully! (" + loadedTimestamps.size() + " entries)");
            } else {
                view.showError("No valid timestamps found in file");
            }
        }
    }
    
    /**
     * Handle exporting timestamps to text file
     */
    public void exportToText(File file) {
        if (file != null) {
            List<TimestampEntry> timestamps = model.getTimestamps();
            
            if (fileService.exportToText(timestamps, file)) {
                view.showSuccess("Timestamps saved successfully!");
                
                // Auto-open the saved file with default application
                try {
                    if (java.awt.Desktop.isDesktopSupported()) {
                        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                        if (desktop.isSupported(java.awt.Desktop.Action.OPEN)) {
                            desktop.open(file);
                        }
                    }
                } catch (Exception e) {
                    // If auto-open fails, just show a message
                    view.showError("File saved but could not auto-open: " + e.getMessage());
                }
            } else {
                view.showError("Error saving timestamps to file");
            }
        }
    }
    
    /**
     * Get current timestamps for table display
     */
    public List<TimestampEntry> getTimestamps() {
        return model.getTimestamps();
    }
    
    /**
     * Helper method to combine notes with pipe separator
     */
    private String combineNotes(List<String> notes) {
        StringBuilder combinedNotes = new StringBuilder();
        for (String note : notes) {
            if (note != null && !note.trim().isEmpty()) {
                if (combinedNotes.length() > 0) {
                    combinedNotes.append(" | ");
                }
                combinedNotes.append(note.trim());
            }
        }
        return combinedNotes.toString();
    }
}
