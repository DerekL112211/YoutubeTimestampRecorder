package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing timestamp operations and business logic
 */
public class TimestampService {
    private List<TimestampEntry> timestamps;
    
    public TimestampService() {
        this.timestamps = new ArrayList<>();
    }
    
    /**
     * Add a new timestamp entry
     */
    public boolean addTimestamp(String timestamp, String notes, TimestampType type) {
        try {
            // Validate timestamp format
            if (!isValidTimestamp(timestamp)) {
                return false;
            }
            
            TimestampEntry entry = new TimestampEntry(timestamp, notes, type);
            timestamps.add(entry);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding timestamp: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Remove timestamp at specified index
     */
    public boolean removeTimestamp(int index) {
        try {
            if (index >= 0 && index < timestamps.size()) {
                timestamps.remove(index);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error removing timestamp: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update note for timestamp at specified index
     */
    public void updateNote(int index, String newNote) {
        if (index >= 0 && index < timestamps.size()) {
            timestamps.get(index).setNotes(newNote);
        }
    }
    
    /**
     * Clear all timestamps
     */
    public void clearAll() {
        timestamps.clear();
    }
    
    /**
     * Get all timestamps
     */
    public List<TimestampEntry> getTimestamps() {
        return new ArrayList<>(timestamps);
    }
    
    /**
     * Set timestamps list
     */
    public void setTimestamps(List<TimestampEntry> timestamps) {
        this.timestamps = new ArrayList<>(timestamps);
    }
    
    /**
     * Parse timestamp string to seconds
     */
    public int parseTimestampToSeconds(String timestamp) {
        try {
            String[] parts = timestamp.split(":");
            if (parts.length == 2) {
                // mm:ss format
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                return minutes * 60 + seconds;
            } else if (parts.length == 3) {
                // hh:mm:ss format
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                int seconds = Integer.parseInt(parts[2]);
                return hours * 3600 + minutes * 60 + seconds;
            }
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Add seconds to timestamp and return new timestamp string
     */
    public String addSecondsToTimestamp(String timestamp, int secondsToAdd) {
        try {
            int totalSeconds = parseTimestampToSeconds(timestamp) + secondsToAdd;
            if (totalSeconds < 0) totalSeconds = 0;
            
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;
            
            if (hours > 0) {
                return String.format("%d:%02d:%02d", hours, minutes, seconds);
            } else {
                return String.format("%02d:%02d", minutes, seconds);
            }
        } catch (Exception e) {
            return timestamp; // Return original if error
        }
    }
    
    /**
     * Validate timestamp format
     */
    private boolean isValidTimestamp(String timestamp) {
        if (timestamp == null || timestamp.trim().isEmpty()) {
            return false;
        }
        
        String[] parts = timestamp.split(":");
        if (parts.length != 2 && parts.length != 3) {
            return false;
        }
        
        try {
            for (String part : parts) {
                Integer.parseInt(part);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
